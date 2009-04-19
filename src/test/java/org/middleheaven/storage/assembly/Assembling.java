package org.middleheaven.storage.assembly;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.middleheaven.quantity.money.Money;
import org.middleheaven.storage.assembly.AssemblyContext;
import org.middleheaven.storage.assembly.AssemblyLineService;
import org.middleheaven.storage.assembly.DataContext;
import org.middleheaven.storage.assembly.SimpleAssemblyLine;


public class Assembling {

	
	@Test
	public void disassemble(){
		
		Money salary = Money.money("100000", "USD");
		
		TestClass test = new TestClass();
		
		test.setName("Ana");
		test.setBirthday(new Date());
		test.setSalary(salary);
		test.setMale(false);
		
		AssemblyLineService line = new SimpleAssemblyLine();
		
		AssemblyContext context = AssemblyContext.contextualize(test);
		line.unAssemble(context);
		
		assertEquals("Ana",context.get(test.getClass().getName() + ".name"));
		assertEquals(false,context.get(test.getClass().getName()  + ".male"));
		assertEquals(salary.amount().asNumber(),context.get(test.getClass().getName()  + ".salary.amount"));
		assertEquals(salary.unit().toString(),context.get(test.getClass().getName()  + ".salary.currency"));
		
	}
	
	@Test
	public void goandback(){
		
		Money salary = Money.money("100000", "USD");
		
		TestClass test = new TestClass();
		
		test.setName("Ana");
		test.setBirthday(new Date());
		test.setSalary(salary);
		test.setMale(false);
		
		AssemblyLineService line = new SimpleAssemblyLine();
		
		AssemblyContext context = AssemblyContext.contextualize(test);
		line.unAssemble(context);
		
		assertEquals("Ana",context.get(test.getClass().getName() + ".name"));
		assertEquals(false,context.get(test.getClass().getName()  + ".male"));
		assertEquals(salary.amount().asNumber(),context.get(test.getClass().getName()  + ".salary.amount"));
		assertEquals(salary.unit().toString(),context.get(test.getClass().getName()  + ".salary.currency"));
		
		
		TestClass result = (TestClass)line.assemble(context);
		
		assertEquals("Ana",result.getName());
		assertEquals(false,result.isMale());
		assertEquals(salary, result.getSalary());

		
	}
	
	
	public static class TestClass {
		
		private String name;
		private Date birthday;
		private Money salary;
		private boolean isMale;
		
		public TestClass (){
			
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public Money getSalary() {
			return salary;
		}
		public void setSalary(Money salary) {
			this.salary = salary;
		}
		public boolean isMale() {
			return isMale;
		}
		
		public void setMale(boolean isMale) {
			this.isMale = isMale;
		}
		
	}
}
