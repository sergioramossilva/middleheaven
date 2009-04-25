package org.middleheaven.ui.web.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.text.GlobalLabel;
import org.middleheaven.global.text.LocalizationService;


public class MenuTag extends AbstractBodyTagSupport {

	Iterator<MenuItem> masterIterator;
	private String varName = "menu"; 

	LinkedList<Iterator<MenuItem>> iteratorStack = new LinkedList<Iterator<MenuItem>>();
	private String rootClass;

	public void setVar(String varName){
		this.varName = varName;
	}

	public void setRootClass(String rootClass){
		this.rootClass = rootClass;
	}

	public MenuItem getCurrent(){
		return this.current;
	}

	public void setRoot(MenuItem menu){
		masterIterator = menu!=null ? menu.getChildren().iterator() : null;
	}

	private LocalizationService localizationService;
	
	public int doStartTag() throws JspException{

		if (masterIterator==null){
			return SKIP_BODY;
		} 

		this.localizationService = ServiceRegistry.getService(LocalizationService.class);
		
		buffer.delete(0, buffer.length());
		write("<ul ");
		if (id!=null){
			writeAttribute("id", id);
		}
		if (!(rootClass==null || rootClass.isEmpty())){
			writeAttribute("class", rootClass);
		}
		write(">");
		if (masterIterator.hasNext()){ // primeiro
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}

	public void doInitBody (){
		iteratorStack.addFirst(masterIterator);
		current = masterIterator.next();
		exposeMenu(current);
	}

	private void exposeMenu(MenuItem item){
		if(item.isTitleLocalized()){
			Culture culture = new TagContext(pageContext).getCulture();
			localizationService.getMessage(culture, new GlobalLabel(item.getTitle()), false);
		} else {
			item.setLabel(item.getTitle());
		}
		pageContext.setAttribute(varName, item);
	}

	MenuItem current;

	StringBuilder buffer = new StringBuilder();

	public int doAfterBody() throws JspException{

		try {
			BodyContent bc = getBodyContent();
			// get the bc as string
			buffer.append(bc.getString());
			// clean up
			bc.clearBody();

			getBodyContent().writeOut(getPreviousOut());
			getBodyContent().clear();

			if (current!=null  && !current.getChildren().isEmpty()){
				buffer.append("<ul>");
				iteratorStack.addFirst(current.getChildren().iterator());

			}

			// enquanto o stack de iteradores não está vazio
			while (!iteratorStack.isEmpty()){
				Iterator<MenuItem> top = iteratorStack.getFirst();

				if (top.hasNext()){
					// processa proximo item do mesmo menu
					current = top.next();
					exposeMenu(current);
					return EVAL_BODY_BUFFERED;

				} else {
					// menu não tem mais itens. termina menu
					buffer.append("</ul>");
					// remove iterador 
					iteratorStack.removeFirst();
				}
			}
			return SKIP_BODY;

	} catch (IOException e) {
		throw new JspTagException(e.getMessage());
	}

}

public int doEndTag() throws JspTagException{

	writeLine(processBuffer(buffer));
	writeLine("</ul>");
	return EVAL_PAGE;
}

private static String processBuffer(StringBuilder buffer) {

	BufferedReader reader = new BufferedReader(new StringReader(buffer.toString()));
	String line;
	RMenuItem root = new RMenuItem();
	RMenuItem current =null;
	RMenuItem parent = root;
	try {
		String[] stoppers = {"<li", "</li" , "<ul" , "</ul" };
		
		while ( (line = reader.readLine())!=null){
			if (line.trim().isEmpty()){
				continue;
			}
			
			int[] res = find(line, stoppers,0);
			
			if (res[0]>0){
				do {
					if (res[1]==0){ //<li
						// start 
						current = new RMenuItem();
						current.addLine(line);
					} else if (res[1]==1){ //</li
						current.addLine(line);
						parent.addChild(current);
					} else if (res[1]==2){ //<ul
	
						parent = current;
	
					} else if (res[1]==3){ //</ul
						parent = parent.parent;
					}
					res = find(line, stoppers,res[0]);
				} while (res[0]>0);
			} else {
				if (current!=null){
					current.addLine(line);
				}
			}
			
		}
	} catch (IOException e) {
		// no-op
	}

	StringBuilder buffer2 = new StringBuilder();
	printMenu(buffer2, root);
	return buffer2.toString();
}

private static int[] find(String s , String[] options , int fromIndex){
	for (int i=0; i < options.length; i++){
		int pos = s.indexOf(options[i], fromIndex+1);
		if (pos>0){
			return new int[]{pos,i};
		}
	}
	return new int[]{-1,-1};
}

private static void printMenu (StringBuilder builder , RMenuItem item ){

	if (!item.children.isEmpty()){
		StringBuilder subItens = new StringBuilder();
		for (RMenuItem sub : item.children){
			printMenu(subItens, sub);
		}
		int pos = item.builder.indexOf("<submenu/>");
		if (pos>0){
			item.builder.replace(pos, pos+"<submenu/>".length(),"<ul>\n"+ subItens.toString() + "</ul>");
		} else {
			item.builder.append(subItens.toString());
		}

	} 

	builder.append(item.builder);


}
public void release(){
	this.masterIterator = null;
	this.buffer.delete(0, buffer.length());
}

private static class RMenuItem {
	StringBuilder builder = new StringBuilder();
	RMenuItem parent;
	List<RMenuItem> children = new LinkedList<RMenuItem>();

	public RMenuItem(){}

	public void addLine(String line){
		builder.append(line).append("\n");
	}
	public void addChild(RMenuItem item){
		item.parent = this;
		children.add(item);
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		toString(builder,this,0);
		return builder.toString();
	}

	private static void toString(StringBuilder builder , RMenuItem item, int tabs){
		for (int i=0; i < tabs; i++){
			builder.append("---");
		}
		builder.append(item.builder.toString().trim()).append("\n");
		for (RMenuItem m : item.children){
			toString(builder,m, tabs+1);
		}
	}
}
}
