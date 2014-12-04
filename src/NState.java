import java.util.Comparator;
import java.util.Arrays;

/**
* class NState used by NFA.java to represent a non-deterministic 
* state
**/
public class NState implements Comparable<NState>
{
	//The name of this state
	private String name;

	//all possible 'a' transitions
	private NState[] a;

	//all possible 'b' transitions
	private NState[] b;

	//all possible epsilon transitions
	private NState[] epsilon;

	//determine if this is an accepting state or not
	boolean isAccept;

	/**
	* constructor for NState
	* @param name the name of the state
	* @param a all 'a' transitions
	* @param b all 'b' transitions
	* @param epsilon all epsilon transitions
	* @param isAccept determines if this state is accepting or not
	**/
	public NState(String name, NState[] a, NState[] b, NState[] epsilon, boolean isAccept)
	{
		this.name = name;
		this.a = a;
		this.b = b; 
		this.epsilon = epsilon;
		this.isAccept = isAccept;
		if(a == null)
		{
			this.a = new NState[0];
		}

		if(b == null)
		{
			this.b = new NState[0];
		}

		if(epsilon == null)
		{
			this.epsilon = new NState[0];
		}

		Arrays.sort(this.a);
		Arrays.sort(this.b);
		Arrays.sort(this.epsilon);
	}

	/**
	* getName gets the name
	* @return the name of the state
	**/
	public String getName()
	{
		return name;
	}

	/**
	* getA gets a transitions
	* NOTE: returns a pointer to 'a' array; NOT a copy
	* @return all 'a' transitions
	**/
	public NState[] getA()
	{
		return a;
	}

	/**
	* getB gets b transitions
	* NOTE: returns a pointer to 'b' array; NOT a copy
	* @return all 'b' transitions
	**/
	public NState[] getB()
	{
		return b;
	}

	/**
	* getEpsilon gets epsilon transitions
	* NOTE: returns a pointer to 'epsilon' array; NOT a copy
	* @return all 'epsilon' transitions
	**/
	public NState[] getEpsilon()
	{
		return epsilon;
	}

	/**
	* isAccepting returns whether this state is accepting or not
	* @return the isAccept value
	**/
	public boolean isAccepting()
	{
		return isAccept;
	}

	/**
	* setA resets all 'a' transitions
	* @param a the new a transitions
	**/
	public void setA(NState[] a)
	{
		this.a = a;
		Arrays.sort(this.a);
	}

	/**
	* setB resets all 'b' transitions
	* @param b the new b transitions
	**/
	public void setB(NState[] b)
	{
		this.b = b;
		Arrays.sort(this.b);
	}

	/**
	* setEpsilon resets all 'epsilon' transitions
	* @param e the new epsilon transitions
	**/
	public void setEpsilon(NState[] e)
	{
		epsilon = e;
		Arrays.sort(this.epsilon);
	}

	public void setIsAccepting(boolean b)
	{
		isAccept = b;
	}

	public int hashCode()
	{
		int hash=7;
		for (int i=0; i < name.length(); i++) 
		{
    		hash = hash * 31 + name.charAt(i);
		}
		return hash;
	}

	public boolean equals(NState n)
	{
		return (n.getName().equals(name)) && (n.isAccepting() == isAccept) && Arrays.deepEquals(a, n.getA()) && Arrays.deepEquals(b, n.getB()) && Arrays.deepEquals(epsilon, n.getEpsilon());
	}

	/**
	* compareTo used by the NStateComparator class 
	* to sort classes lexigraphically
	* @param compareNState the state that
	* this state is being compared to
	* @return positive value if this > compareNstate
	*		  negative value if this < compareNstate
	*		  otherwise 0
	**/
	public int compareTo(NState compareNState)
	{
		return this.name.compareTo(compareNState.getName());
	}

	/**
	* static class used by the Collections class for sorting
	**/
	public static Comparator<NState> NStateComparator = new Comparator<NState>()
	{
		/**
		* compare used to compare two NStates
		* @param n1 first state to compare
		* @param n2 second state to compare
		* @return positive value if n1 > n2
		*		  negative value if n1 < n2
		*		  otherwise 0
		**/
		public int compare(NState n1, NState n2)
		{
			return n1.compareTo(n2);
		}
	};
}