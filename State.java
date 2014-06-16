/**
* State class used by DFA.java to repreent a deterministic state
**/
public class State
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
	* @param String name name of the state
	* @param State a the a transition 
	* @param State b the b transition
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
	* @param State s the new a transition
	**/
	public void setA(State s)
	{
		a = s;
	}

	/**
	* setB resets the b transition
	* @param State s the new b transition
	**/
	public void setB(State s)
	{
		b = s;
	}
}