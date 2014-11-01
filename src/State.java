import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
* State class used by DFA.java to repreent a deterministic state
**/
public class State extends JComponent
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
	
}