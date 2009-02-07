package org.middleheaven.storage.assembly;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class AssemblyLine {


	Map<String,Assembler> assemblers = new TreeMap<String,Assembler>();

	Object item;
	String name;
	
	public AssemblyLine(Object object){
		this(object,object.getClass().getName());
	}

	
	public AssemblyLine(Object object, String name){
		this.item = object;
		this.name= name;
		
		assemblers.put("", new BeanAssembler());
		
		this.addAssembler(new MoneyAssembler());
		this.addAssembler(new NumericAssembler());
		
		
	}

	public Object getObject(){
		return item;
	}


	public String getNameFor(String string){
		return string;
	}

	public boolean isDisassemblable(Object object){
		return !(
				object.getClass().isPrimitive() || 
				String.class.isInstance(object)|| 
				Date.class.isInstance(object) ||
				Number.class.isInstance(object) ||
				Boolean.class.isInstance(object)
		)
		;
	}

	public boolean isAssembled(Object object){
		return !object.getClass().isPrimitive();
	}

	public void unAssemble(DataContext context, Object propertyValue, String name){
		AssemblyLine line = this;
		if (propertyValue!=null){
			line = new AssemblyLine(propertyValue, name);
		}

		Assembler assembler = getAssembler(line.getObject().getClass());
		if (assembler==null){
			throw new AssemblyException("No assembler found for " + line.getObject().getClass());
		}
		assembler.unAssemble(context, line);

	}

	public Object assemble(DataContext context, Object propertyValue, String name){
		return null;
	}

	private Assembler getAssembler (Class<?> type){
		Assembler assembler  = this.assemblers.get(getClassReference(type)); 

		if (assembler==null){
			assembler = this.assemblers.get("");
		}

		return assembler;
	}

	private String getClassReference(Class<?> type){
		return type==null ? "" : type.getName();
	}

	public void addAssembler(Assembler assembler){

		assemblers.put(getClassReference(assembler.getAssembleType()), assembler);
	}

	public void removeAssembler(Assembler assembler){
		assemblers.remove(getClassReference(assembler.getAssembleType()));
	}

	public String getName() {
		return name;
	}
}
