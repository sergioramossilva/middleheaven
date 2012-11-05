package org.middleheaven.quantity.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.quantity.math.structure.GroupAdditive;
import org.middleheaven.quantity.measure.Amount;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.Hash;

public class Money implements Amount<Money, org.middleheaven.quantity.measurables.Currency>, Comparable<Money> {

	private static int TEN = 10;
	private static int ONE_HUNDRED = 100;
	private static int TEN_CUBED= 1000;
	
	/**
	 * Power of 10 has the value at position n in the arrays equals 10 to nth power. 
	 * This is for internal use only.
	 */
	private static int[] powers = new int[]{1,TEN,ONE_HUNDRED,TEN_CUBED};

	private static int getPower(int n){
		if (powers.length<=n){
			// if the needed power does not yet exist, calculate it and add it to 
			// the powers array. This code is a safety it will rarely be runned
			int[] newpowers = new int[n+1];
			System.arraycopy(powers , 0 , newpowers , 0 , 0 );

			for (int i = powers.length; i < newpowers.length; i++){
				newpowers[i] = (int)Math.pow(TEN, i);
			}
			powers = newpowers;
		}
		return powers[n];
	}


	private long amount;
	private Currency currency;

	public static Money money (Real value, Currency currency){
		return new Money (value , currency);
	}
	
	public static Money money (String value, Currency currency){
		return new Money (Real.valueOf(value) , currency);
	}

	public static Money money (String value, String isoCode){
		return new Money (Real.valueOf(value) , Currency.currency(isoCode));
	}

	public static Money money (java.lang.Number value, String isoCode){
		return new Money (Real.valueOf(value) , Currency.currency(isoCode));
	}

	/**
	 * Internal utility method to transform numbers to internal representation
	 * Amounts are expressed as decimal numbers, but this is not the best way to 
	 * represent amount values. The best way is to use a long as it enhances accuracy. 
	 * However ,each currency has its own set of fraction digits so this operation depends on the currency used.
	 * 
	 * @param number
	 * @return a long representing the amount
	 */
	private long toInternalAmount(BigDecimal number){
		return toInternalAmount(number.doubleValue());
	}  

	private long toInternalAmount(double number){
		return Math.round(number * getPower(currency.getDefaultFractionDigits()));
	}  

	protected Money(Real amount, Currency currency) {
		this.currency = currency;
		this.amount = toInternalAmount(new BigDecimal(amount.toString()));
	}

	protected Money (Money other , long amount ){
		this.currency = other.currency;
		this.amount = amount;
	}

	private Money(long amount, Currency currency) {
		this.currency = currency;
		this.amount = amount;
	}

	public Money negate() {
		return new Money (-amount,this.currency);
	}

	protected void assertCompatible(Quantity<org.middleheaven.quantity.measurables.Currency> other) throws IncompatibleUnitsException {
		if (other instanceof Money && !this.unit().isCompatible(other.unit())){
			throw new IncompatibleUnitsException(this.unit(),other.unit());
		}
	}


	public Money plus(Money other) throws IncompatibleUnitsException {
		assertCompatible(other);
		return new Money (this.amount + ((Money)other).amount,this.currency);
	}

	public Money minus(Money other) throws IncompatibleUnitsException {
		assertCompatible(other);
		return new Money (this.amount - ((Money)other).amount,this.currency);
	}

	public Money times(Real other) {
		return new Money(this.amount().times(other), currency);
	}

	public Money over(Real other) {
		return new Money(this.amount().over(other), currency);
	}

	public Real amount() {
		return Real.valueOf(BigDecimal.valueOf(amount, this.currency.getDefaultFractionDigits()));
	}

	public Unit<org.middleheaven.quantity.measurables.Currency> unit() {
		return currency;
	}

	public String toString(){
		return amount().toString() + " " + currency.toString();
	}

	public boolean equals(Object other){
		return other instanceof Money && equalsMoney((Money)other);
	}

	private boolean equalsMoney(Money other){
		return this.unit().equals(other.unit()) && this.amount == other.amount;
	}

    public int hashCode(){
    	return Hash.hash(amount).hashCode();
    }
    
	/**
	 * Allocate <code>this</code> money amount in n installments.
	 * All money is divided. If the devision remainder is not zero it is added to the first amount
	 * 
	 * @param percentages an int arrays containing the percentages. Example int[]{30,30,40}  
	 * @param installments the quantity of required installments
	 * @return an arrays of Money each of the same currency as <code>this</code> and containing the percentage of the amount
	 */
	public Money[] distribute(int installments){

		BigInteger[] result = new BigInteger(Long.toString(amount))
		.divideAndRemainder(new BigInteger(Integer.toString(installments)));


		final long even = result[0].longValue();
		long  r = result[1].longValue();

		Money[] moneys = new Money[installments]; 
		Arrays.fill(moneys,new Money(this, even));

		int i=0;
		while (r>0){
			moneys[i] = new Money(this, even + 1);
			r--;
			i++;
			if (i==moneys.length){
				i=0;
			}
		}

		return moneys;
	}

	/**
	 * Allocate <code>this</code> money amount in determinate number of installments.
	 * All money is distributed. If the devision remainder is not zero it is added to the 
	 * informed installment
	 * 
	 * @param installments the number of instalments to distribute into.
	 * @param incrementedInstallment The instalment to increment. 1 is the first, 2 is the second, etc...
	 * @return an arrays of Money each of the same currency as <code>this</code> and containing the percentage of the amount
	 * 
	 */
	public Money[] distribute(int installments , int incrementedInstallment){
		Money[] qr = divideAndRemainder(installments);

		Money[] result = new Money[installments]; 
		Arrays.fill(result,qr[0]);

		if (qr[1].amount!=0){
			result[incrementedInstallment-1] = (Money)result[incrementedInstallment-1].plus(qr[1]);
		}

		return result;
	}

	/**
	 * Allocate <code>this</code> money amount according to a percentage distribution
	 * All money is divided. If the devision remainder is not zero it is distributed among the first
	 * installments
	 * 
	 * @param percentages an variable array of int, each representing a  percentages. allocate(30,30,40). The sum must be 100  
	 * @return an arrays of Money each of the same currency as <code>this</code> and containing the percentage of the amount
	 * @throws IllegalArgumentException if percentages is empty or it does not sum 100.
	 */
	public Money[] allocate(int ... percentages){
		if (percentages.length==0){
			throw new IllegalArgumentException ("Percentages not provided");
		}

		// the sum of all percentages must be 100
		int total = 0;
		for (int i = 0; i < percentages.length ; i++){
			total += percentages[i];
		}
		if (total != ONE_HUNDRED){
			throw new IllegalArgumentException ("Percentages does not sum 100");
		}

		Money[] results = new Money[percentages.length];
		long remainder = this.amount;
		for (int i = 0; i < results.length; i++) {
			results[i] = new Money(this,this.amount * percentages[i] / this.currency.getDefaultFractionDigits()); // integer division
			remainder -= results[i].amount;
		}
		for (int i = 0; i < remainder; i++) {
			results[i].amount++;
		}
		return results;
	}

	/**
	 * Divides the amount equally between <code>n</code> installments
	 * So 100 divided by 3 will return 33,33 for a 2 fraction digits currency
	 * @param n number of installments
	 * @return the operation result <code>Money</code>
	 * @see #divideAndRemainder(int,int)
	 */
	public Money divide (int n) {
		return divideAndRemainder(n)[0]; //return the quotient
	}
	/**
	 * Divides the amount equally between n installments
	 * returning the quotient and rest of that division.
	 * 
	 * @param n number of installments
	 * @return the operation result <code>Money</code>
	 * @see #divide(int) 
	 * @see java.math.BigInteger#divideAndRemainder(java.math.BigInteger);
	 */
	public Money[] divideAndRemainder(int n){
		BigInteger[] result = new BigInteger(Long.toString(amount))
		.divideAndRemainder(new BigInteger(Integer.toString(n)));

		return new Money[] {  
				new Money(this, result[0].longValue())  , 
				new Money(this, result[1].longValue()) 
		}; 

	}


	public boolean greaterThan(Money other) {
		return (compareTo(other) > 0);
	}

	public boolean lesserThan(Money other) {
		return (compareTo(other) < 0);
	}

	@Override
	public int compareTo(Money other) {
		if (!this.currency.equals(other.currency)){
			throw new IllegalArgumentException("Cannot compare money in different currencies. Use compareTo(Money,MoneyConverter)");
		}
		return (int)(this.amount - other.amount);
	}

	public Currency currency(){
		return currency;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupAdditive<Money> getAlgebricStructure() {
		return new GroupAdditive<Money>(){

			@Override
			public boolean isGroupAdditive() {
				return true;
			}

			@Override
			public boolean isRing() {
				return false;
			}

			@Override
			public boolean isField() {
				return false;
			}

			@Override
			public Money zero() {
				return new Money(RealField.getInstance().zero(), currency);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZero() {
		return this.amount == 0L;
	}

	
}
