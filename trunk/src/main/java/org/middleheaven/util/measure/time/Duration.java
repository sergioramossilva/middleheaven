package org.middleheaven.util.measure.time;

import java.util.EnumMap;
import java.util.Map;

import org.middleheaven.util.measure.Dimension;
import org.middleheaven.util.measure.IncompatibleUnitsException;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Unit;

public class Duration extends ElapsedTime implements Comparable<Duration>{

	private enum DurationType{
		MILISECONDS(null),
		MINUTES(MILISECONDS),
		HOURS(MINUTES),
		DAYS(HOURS), 
		MONTHS(DAYS),
		YEARS(MONTHS);

		private DurationType next;
		private DurationType(DurationType next){
			this.next = next;
		}
		
		public DurationType getNext(){
			return next;
		}
	}
	
	private EnumMap<DurationType,Number> fields = new EnumMap<DurationType,Number>(DurationType.class);

	protected Duration() {
		for (DurationType t : DurationType.values()){
			fields.put(t, 0);
		}
	}
	
	public Duration(DurationType type, Number value) {
		this();
		fields.put(type,value);
	}
	
	
	protected Duration(Duration other) {
		this();
		for (Map.Entry<DurationType,Number> e : other.fields.entrySet()){
			this.fields.put(e.getKey(), e.getValue()); 
		}
	}

	
	public static Duration weeks(int weeks){
		return new Duration ( DurationType.DAYS, 7*weeks);
	}
	
	public static Duration days(int days){
		return new Duration ( DurationType.DAYS, days);
	}
	
	public static Duration date(int years, int months,int days){
		Duration d = new Duration(DurationType.YEARS,years);
		d.fields.put(DurationType.MONTHS, months);
		d.fields.put(DurationType.DAYS, days);
		return d;
	}

	public static Duration time(int hours, int minutes,int seconds){
		Duration d = new Duration(DurationType.HOURS,hours);
		d.fields.put(DurationType.MINUTES, minutes);
		d.fields.put(DurationType.MILISECONDS, seconds * 1000);
		return d;
	}

	public static Duration years(int ammount){
		return new Duration(DurationType.YEARS,ammount);
	}

	public static Duration months(int ammount){
		return new Duration(DurationType.MONTHS,ammount);
	}


	public static Duration hours(int ammount){
		return new Duration(DurationType.HOURS,ammount);
	}

	public static Duration minutes(int ammount){
		return new Duration(DurationType.MINUTES,ammount);
	}

	public static Duration seconds(int ammount){
		return new Duration(DurationType.MILISECONDS,ammount*1000);
	}

	public static Duration miliseconds(long ammount){
		return new Duration(DurationType.MILISECONDS,ammount);
	}

	


	public int years(){
		return fields.get(DurationType.YEARS).intValue();
	}

	public int months(){
		return fields.get(DurationType.MONTHS).intValue();
	}

	public int days(){
		return fields.get(DurationType.DAYS).intValue();
	}

	public int hours(){
		return fields.get(DurationType.HOURS).intValue();
	}

	public int minutes(){
		return fields.get(DurationType.MONTHS).intValue();
	}

	public double secounds(){
		return fields.get(DurationType.MILISECONDS).longValue() / 1000d;
	}

	public double miliseconds(){
		return fields.get(DurationType.MILISECONDS).longValue();
	}

	@Override
	public Unit unit() {
		int count =0;
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				count++;
			}
		}
		
		if (count==1){
			for (DurationType t : DurationType.values()){
				if (fields.get(t)==null){
					switch (t){
					case YEARS:
						return Unit.unit(Dimension.TIME,"year");
					case MONTHS:
						return Unit.unit(Dimension.TIME,"month");
					case DAYS:
						return Unit.unit(Dimension.TIME,"day");
					case HOURS:
						return SI.HOUR;
					case MINUTES:
						return Unit.unit(Dimension.TIME,"minute");
					case MILISECONDS:
						return SI.MILISECOND;
					}
				}
			}
		}
		return Unit.unit(Dimension.TIME,"Duration");

	}

	public Duration negate() {
		Duration d = new Duration();
		for (Map.Entry<DurationType,Number> e : this.fields.entrySet()){
			if (e.getValue()!=null){
				d.fields.put(e.getKey(), -1*e.getValue().longValue());
			}
		}
		return d;
	}

	public org.middleheaven.util.measure.Integer amount() {
		return reduce();
	}
	
	protected org.middleheaven.util.measure.Integer reduce (){
		int count =0;
		DurationType field=null;
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				count++;
				field = t;
			}
		}
		
		if (count==1){
			return  org.middleheaven.util.measure.Integer.valueOf(fields.get(field).longValue());
		} else {
			throw new IllegalStateException("Cannot reduce a multipart duration");
		}
	}


	public Duration times(org.middleheaven.util.measure.Number other) {
		long factor = other.asNumber().longValue();
		Duration d = new Duration(this);
		for (Map.Entry<DurationType,Number> e : this.fields.entrySet()){
			if (e.getValue()!=null){
				d.fields.put(e.getKey(), factor*e.getValue().longValue());
			}
		}
		return d;
	}

	public ElapsedTime over(org.middleheaven.util.measure.Number other) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Number sum (Number a , Number b){
		if (a!=null){
			if (b!=null){
				return a.longValue() + b.longValue();
			} else {
				return a;
			}
		}
		return b;
	}
	
	public Duration plus(Duration other) throws IncompatibleUnitsException {
		Duration r = new Duration();
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				r.fields.put(t, sum(this.fields.get(t) , other.fields.get(t)));
			}
		}
		return r;
	}

	public Duration plus(Period other) throws IncompatibleUnitsException {
		Duration r = new Duration(this);
		Long p = new Long(other.miliseconds);
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				r.fields.put(t, sum(this.fields.get(t) , p ));
			}
		}
		return r;
	}


	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (DurationType t : DurationType.values()){
			if (this.fields.get(t)!=null){
				builder.append(fields.get(t)).append(' ');
				switch (t){
				case YEARS:
					builder.append("years");
					break;
				case MONTHS:
					builder.append("months");
					break;
				case DAYS:
					builder.append("days");
					break;
				case HOURS:
					builder.append("h");
					break;
				case MINUTES:
					builder.append("minute");
					break;
				case MILISECONDS:
					builder.append("ms");
					break;
				}
				builder.append(' ');
			}
		}
		return builder.toString().trim();
	}

	@Override
	public Object clone() {
		return new Duration(this);
	}

	/**
	 * Given a TimePoint t , duration x > y iff t + x > t + y and duration x < y iff t + x < t + y 
	 *  
	 * {@link http://www.w3.org/TR/xmlschema-2/#duration-order}
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Duration other) { 
		// The underliing chronology is not important as t is a generic time point
		TimePoint t1 = TimeContext.getTimeContext().getChronology().add(CalendarDateTime.origin(), this);
		TimePoint t2 = TimeContext.getTimeContext().getChronology().add(CalendarDateTime.origin(), other);
		
		return (int)(t2.milliseconds()-t1.milliseconds());
	}

	



}
