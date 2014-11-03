/**
* Class represents a Finite Automata
* Subclasses are DFA and NFA(planned)
**/
public abstract class FA
{
	//start state
	private State start;

	// array holding all states in the machine
	protected State[] machine;

	//character that moves on a transition
	private char aTransition;

	//character that moves on b transition
	private char bTransition;

	/**
	* FA constructor
	* @param s start state
	* @param m array holding all states in machine
	* @param a character that moves on a transition
	* @param b character that moves on b transition
	**/
	public FA(State s, State[] m, char a, char b)
	{
		start = s;
		machine = m;
		aTransition = a;
		bTransition = b;
	}

	/**
	* getMachine accessor
	* @return array containing all states
	**/
	public State[] getMachine()
	{
		return machine;
	}

	/**
	* getStart accessor
	* @return start state of DFA
	**/
	public State getStart()
	{
		return start;
	}

	/**
	* getATransition accessor
	* @return character on a transition
	**/
	public char getATransition()
	{
		return aTransition;
	}

	/**
	* getBTransition accessor
	* @return character on B transition
	**/
	public char getBTransition()
	{
		return bTransition;
	}

	/**
	* getState returns the state in the machine that has a name matching the name passed in
	* @param stateName the name of the state that will be returned
	* @return the State with the same name as state name, otherwise null
	**/
	public State getState(String stateName)
	{
		for(int i = 0; i < machine.length; i++)
		{
			if(machine[i].getName().equals(stateName))
			{
				return machine[i];
			}
		}
		return null;
	}

	/**
	* sets the start state to a new value
	* @param s the new start state
	**/
	public void setStart(State s)
	{
		this.start = s;
	}

	/**
	* inLanguage determines if string is in the language or not
	* @param s the string that is being checked
	* @param current that state that we are currently at in the machine (usually starts at the start state)
	* @return true if the string is part of the language, otherwise false
	**/
	public boolean inLanguage(String s, State current)
	{
		return true;
	}
}