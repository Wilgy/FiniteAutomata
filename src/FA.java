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

	public boolean inLanguage(String s, State current)
	{
		return true;
	}
}