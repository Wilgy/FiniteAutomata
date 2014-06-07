public class NFA
{
	private NState start;

	private NState[] machine;

	private char aTransition;

	private char bTransition;

	public NFA(NState s, NState[] m, char a, char b)
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
	public NState[] getMachine()
	{
		return machine;
	}

	/**
	* getStart accessor
	* @return start state of DFA
	**/
	public NState getStart()
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
	* inLanguage determines if the given string is accepted by this NFA
	* @param String s the string being tested
	* @param NState current the current state that we are at in the machine
	* 						(will initially be at the start state)
	* @return true if s is accepted, else false
	**/
	public boolean inLanguage(String s, NState current)
	{	
		//check all of the episilon stransitions from this state
		for(int i = 0; i < current.getEpsilon().length; i++)
		{
			if(inLanguage(s, current.getEpsilon()[i]))
			{
				return true;
			}
		}

		//if we have eaten through all of the string for this branch,
		//then we can see if we are in an accepting state
		if(s.length() == 0)
		{
			if(current.isAccepting())
			{
				return true;	
			}
			
			return false;
		}

		//still have some string to go through
		else
		{

			//go through all of the 'a' transitions
			if(s.charAt(0) == aTransition)
			{
				for(int i = 0; i < current.getA().length; i++)
				{
					if(inLanguage(s.substring(1, s.length()), current.getA()[i]))
					{
						return true;
					}
				}

				return false;

			}

			//go through all of the 'b' transistions
			else
			{
				for(int i = 0; i < current.getB().length; i++)
				{
					if(inLanguage(s.substring(1, s.length()), current.getB()[i]))
					{
						return true;
					}	
				}

				return false;
			}
		}
	}


	/**
	* main function used for testing
	* @param String[] args command line arguments; unused
	**/
	public static void main(String[] args)
	{
		NState[] zero = new NState[0];

		NState A = new NState("A", zero, zero, zero, false);
		NState B = new NState("B", zero, zero, zero, false);
		NState C = new NState("C", zero, zero, zero, true);

		NState[] AATransitions = {B};
		NState[] AETransitions = {C};
		NState[] BBTransitions = {C};
		NState[] CBTransitions = {C};

		A.setA(AATransitions);
		A.setEpsilon(AETransitions);
		B.setB(BBTransitions);
		C.setB(CBTransitions);

		NState[] testMachine = {A,B,C};

		NFA test = new NFA(A, testMachine, 'a', 'b');

	}
}