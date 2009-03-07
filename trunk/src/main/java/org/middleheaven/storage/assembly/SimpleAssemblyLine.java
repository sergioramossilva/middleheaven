package org.middleheaven.storage.assembly;

import java.util.HashMap;
import java.util.Map;

public class SimpleAssemblyLine implements AssemblyLineService {

	Map<ClassReference,Assembler> assemblers = new HashMap<ClassReference,Assembler>();
	
	public SimpleAssemblyLine(){

		assemblers.put(new ClassReference(), new BeanAssembler());
		
		this.addAssembler(new MoneyAssembler());
		this.addAssembler(new CurrencyAssembler());
		this.addAssembler(new NumericAssembler());
		
		
	}

	public Assembler getAssembler (Class<?> type){
		Assembler assembler  = this.assemblers.get(getClassReference(type)); 

		if (assembler==null){
			assembler = this.assemblers.get(new ClassReference());
		}

		return assembler;
	}

	
	private class ClassReference{
		Class<?> type;
		
		public ClassReference(Class<?> type) {
			super();
			this.type = type;
		}

		public ClassReference() {
			this(ClassReference.class);
		}

		public int hashCode(){
			return 0;
		}
		
		public boolean equals(Object other){
			return equals((ClassReference)other);
		}
		
		public boolean equals(ClassReference other){
			return other.type.isAssignableFrom(type);
		}

	}
	private ClassReference getClassReference(Class<?> type){
		return type==null ? new ClassReference() :  new ClassReference(type);
	}

	public void addAssembler(Assembler assembler){

		assemblers.put(getClassReference(assembler.getAssembleType()), assembler);
	}

	public void removeAssembler(Assembler assembler){
		assemblers.remove(getClassReference(assembler.getAssembleType()));
	}

	@Override
	public Object assemble(AssemblyContext ctx) {
		Data data = new BeanData (ctx.assemblyTargetType().getName(),null,ctx.assemblyTargetType());
		
		Assembler a = this.getAssembler(data.getValueType());
		a.assemble(this, ctx,data);
		
		return data.getValue();
	}

	@Override
	public void unAssemble(AssemblyContext ctx) {
		boolean repeat = false;
		do{
			repeat = false;
			for (Data d: ctx){
				
				if (d.getValue()!=null && !ctx.isPrimitive(d.getValue())){
					ctx.remove(d);
					Assembler a = this.getAssembler(d.getValueType());
					a.unAssemble(this, ctx,d);
					repeat = true;
				}
			}
		}while(repeat);
	}

	
}
