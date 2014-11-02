import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Comparator;

/**
* State class used by DFA.java to repreent a deterministic state
**/
public class State implements Comparable<State>
{

	//The name of this state
	private String name;

	//the 'a' transistion
	private State a;

	//the 'b' transition
	private State b;

	//whether this state is accepting or not
	boolean isAccept;

	/** 
	* constructor for State
	* @param name name of the state
	* @param a the a transition 
	* @param b the b transition
	* @param isAccept boolean whether this state is accepting or not
	**/
	public State(String name, State a, State b, boolean isAccept)
	{
		this.name = name;
		this.a = a;
		this.b = b;
		this.isAccept = isAccept;

	}

	/**
	* getName get name of the state
	* @return the name of the state
	**/
	public String getName()
	{
		return name;
	}

	/**
	* getA grabs the a transition
	* @return the a transition
	**/
	public State getA()
	{
		return a;
	}

	/**
	* getB grabs the b transistion
	* @return the b transition
	**/
	public State getB()
	{
		return b;
	}

	/**
	* isAccepting returns whether this state is accepting or not
	* @return the isAccept field
	**/
	public boolean isAccepting()
	{
		return isAccept;
	}

	/**
	* setA resets the a transition
	* @param s the new a transition
	**/
	public void setA(State s)
	{
		a = s;
	}

	/**
	* setB resets the b transition
	* @param s the new b transition
	**/
	public void setB(State s)
	{
		b = s;
	}

	@Override
	public int hashCode()
	{
		int hash=7;
		for (int i=0; i < name.length(); i++) 
		{
    		hash = hash * 31 + name.charAt(i);
		}
		return hash;

	}

	public String toString()
	{
		String aName = "null";
		String bName = "null";
		if(a != null)
		{
			aName = a.getName();
		}

		if(b != null)
		{
			bName = b.getName();
		}
		String result = "State: " + this.getName() + " is accepting: " + isAccept + "\n";
			  result += "	A transition: " + aName + "\n";
			  result += "	B transition: " + bName + "\n";
		return result;
	}

	public boolean equals(State n)
	{
		return (n.getName().equals(name)) && (n.isAccepting() == isAccept) && (n.getA() == a) && (n.getB() == b);
	}

	/**
	* compareTo used by the StateComparator class 
	* to sort classes lexigraphically
	* @param compareState the state that
	* this state is being compared to
	* @return positive value if this > compareState
	*		  negative value if this < compareState
	*		  otherwise 0
	**/
	public int compareTo(State compareState)
	{
		return this.name.compareTo(compareState.getName());
	}

	/**
	* static class used by the Collections class for sorting
	**/
	public static Comparator<State> StateComparator = new Comparator<State>()
	{
		/**
		* compare used to compare two States
		* @param n1 first state to compare
		* @param n2 second state to compare
		* @return positive value if n1 > n2
		*		  negative value if n1 < n2
		*		  otherwise 0
		**/
		public int compare(State n1, State n2)
		{
			return n1.compareTo(n2);
		}
	};
	
}