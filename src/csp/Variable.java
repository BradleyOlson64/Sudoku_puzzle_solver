package csp;
import java.util.ArrayList;
import java.util.Arrays;

//Tested!
/**
 * @author Bradley Olson
 * A variable in a CSP that takes on an integer value between 1 and 9.
 * The value of the variable may be specified by the original problem.
 */
public class Variable {
	private int value;
	private int[] domain;
	
	public Variable(Variable another) {
		this.value = another.value;
		this.domain = another.domain.clone();
	}
	
	/**
	 * Creates a new variable with a full domain of possible values.
	 */
	public Variable() {
		this.value = -1;
		domain = new int[9];
		for(int i=1;i<=9;i++) {
			domain[i-1] = i;
		}
	}
	
	/**
	 * Creates a new variable holding a specific value 
	 * @param value
	 */
	public Variable(int value) {
		domain = new int[9];
		this.value = value;
		domain[value - 1] = value;
	}
	
	/**
	 * Adds one value to the domain, removing any number in value if it was filled.
	 * @param value
	 */
	public void addOne(int addVal) {
		if(value != addVal) {
			domain[addVal - 1] = addVal;
			value = -1;
		}
	}
	
	/**
	 * Adds many numbers to the domain, removing any number from value if it was filled.
	 * @param addVal
	 */
	public void addMany(ArrayList<Integer> addVal) {
		for(Integer val:addVal) {
			domain[val - 1] = val;
		}
		value = -1;
	}

	/**
	 * Removes one number from the domain, filling value if only one domain entry remains.
	 * @param remove
	 */
	public void removeOne(int remove) {
		domain[remove - 1] = 0;
		if(domainSize(domain) == 1) setValue(domain);
	}
	
	/**
	 * Removes many numbers from the domain, filling value if only one domain entry remains.
	 * @param removeVal
	 */
	public void removeMany(ArrayList<Integer> removeVal) {
		for(Integer val:removeVal) {
			domain[val -1] = 0;
		}
		if(domainSize(domain) == 1) setValue(domain);
	}
	
	/**
	 * Sets the variable equal to the given value. Sets domain to include only that value.
	 * @param entry
	 */
	public void setValue(int entry) {
		value = entry;
		for(int i = 0; i<domain.length;i++) {
			if(i != entry - 1) domain[i] = 0;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public ArrayList<Integer> getDomain(){
		ArrayList<Integer> theDomain = new ArrayList<>();
		for(int i = 0;i<domain.length;i++) {
			if(domain[i] != 0) theDomain.add(domain[i]);
		}
		return theDomain;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Variable) {
			Variable theObj = (Variable) obj;
			if(this.getDomain().equals(theObj.getDomain()) && this.getValue() == theObj.getValue()) {
				return true;
			}
		}
		return false;
	}
	
 	private int domainSize(int[] domain) {
		int count = 0;
		for(int i=0; i<domain.length;i++) {
			if(domain[i] != 0) count ++;
		}
		return count;
	}
	private void setValue(int[] domain) {
		for(int i =0;i<domain.length;i++) {
			if(domain[i] != 0) value = domain[i];
		}
	}
	@Override
	public String toString() {
		return Arrays.toString(domain);
	}

	public static void main(String[] args) {
		ArrayList<Integer> ex = new ArrayList<>();
		ex.add(2);
		ex.add(4);
		ex.add(7);
		Variable determined = new Variable(9);
		Variable determined2 = new Variable(9);
		Variable determined3 = new Variable(3);
		determined.addMany(ex);
		determined.removeMany(ex);
		System.out.println(Arrays.toString(determined.domain));
		System.out.println(determined.domainSize(determined.domain));
	}
}
