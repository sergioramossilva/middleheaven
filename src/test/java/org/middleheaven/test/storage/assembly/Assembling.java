package org.middleheaven.test.storage.assembly;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.middleheaven.storage.assembly.AssemblyLine;
import org.middleheaven.storage.assembly.DataContext;
import org.middleheaven.storage.assembly.MapDataContext;
import org.middleheaven.util.measure.money.Money;


public class Assembling {

	
	@Test
	public void disassemble(){
		
		Money salary = Money.money("100000", "USD");
		
		TestClass test = new TestClass();
		
		test.setName("Ana");
		test.setBirthday(new Date());
		test.setSalary(salary);
		test.setMale(false);
		
		AssemblyLine line = new AssemblyLine(test);
		
		DataContext context = new MapDataContext();
		
		line.unAssemble(context, null, null);
		
		assertEquals("Ana",context.get(test.getClass().getName() + ".name"));
		assertEquals(false,context.get(test.getClass().getName()  + ".male"));
		assertEquals(salary.amount().asNumber(),context.get(test.getClass().getName()  + ".salary.amount"));
		assertEquals(salary.unit().toString(),context.get(test.getClass().getName()  + ".salary.currency"));
		
	}
	
	
	
	private class TestClass {
		
		private String name;
		private Date birthday;
		private Money salary;
		private boolean isMale;
		
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
