import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

public class NFA
{
	//the start state
	private NState start;

	//machine holding all of the states
	private NState[] machine;

	//character representing the 'a' transition
	private char aTransition;

	//character representing the 'b' transition
	private char bTransition;

	private ArrayList<NState> acceptStates;

	/**
	* constructor
	* @param s the start state
	* @param m arraay holding all states
	* @param a a transition
	* @param b b transition
	**/
	public NFA(NState s, NState[] m, char a, char b)
	{
		start = s;
		machine = m;
		aTransition = a;
		bTransition = b;
		acceptStates = new ArrayList<NState>();
		for(int i = 0; i < machine.length; i++)
		{
			if(machine[i].isAccepting())
			{
				acceptStates.add(machine[i]);
			}
		}
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

	public ArrayList<NState> getAcceptStates()
	{
		return acceptStates;
	}

	public void addAcceptState(NState s)
	{
		acceptStates.add(s);
	}

	public void removeAcceptState(NState s)
	{
		acceptStates.remove(s);
	}

	public String toString()
	{
		String result = "NFA with start state: " + this.getStart().getName() + ":\n";
		result += "------------------------------------------\n";

		for(int i = 0; i < this.getMachine().length; i++)
		{
			result += "State: " + this.getMachine()[i].getName() + " Is accepting: " + this.getMachine()[i].isAccepting() + "\n";
			result += "     A transitions: ";
			for(int j = 0; j < this.getMachine()[i].getA().length; j++)
			{
				result += this.getMachine()[i].getA()[j].getName() + ", "; 
			}
			result += "\n";
			result += "     B transitions: ";
			for(int j = 0; j < this.getMachine()[i].getB().length; j++)
			{
				result += this.getMachine()[i].getB()[j].getName() + ", "; 
			}
			result += "\n";
			result += "     Epsilon transitions: ";
			for(int j = 0; j < this.getMachine()[i].getEpsilon().length; j++)
			{
				result += this.getMachine()[i].getEpsilon()[j].getName() + ", "; 
			}
			result += "\n";
		}
		return result;
	}

	/**
	* inLanguage determines if the given string is accepted by this NFA
	* @param s the string being tested
	* @param current the current state that we are at in the machine
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
	* getDFA takes this NFA and converts is to its corresponding DFA
	* @return the DFA that corresponds to this NFA
	**/
	public DFA getDFA()
	{
		//create powerset of all states (regular State)
		ArrayList<State> DFAmachine = new ArrayList<State>((int)(Math.pow(2, machine.length)));
		ArrayList<ArrayList<NState>> power = powerSet(machine);
		
		//will be used to determine the state transitions
		//NOTE: could optimize with a wrapper class to hold both
		HashMap<State, ArrayList<NState>> stateMap = new HashMap<State, ArrayList<NState>>();
		HashMap<ArrayList<NState>, State> nstateMap = new HashMap<ArrayList<NState>, State>();

		NState newStartState = start;
		NState[] nssEpsilon = newStartState.getEpsilon();
		HashSet<NState> newStartStateSet = new HashSet<NState>();

		//the set that represents our new start state
		newStartStateSet.add(newStartState);
		for(int i = 0; i < nssEpsilon.length; i++)
		{
			newStartStateSet.add(nssEpsilon[i]);
		}

		State nullState = new State("NULL", null, null, false);
		nullState.setA(nullState);
		nullState.setB(nullState);
		DFAmachine.add(nullState);
		

		ArrayList<NState> oldStartState = new ArrayList<NState>();
		oldStartState.add(start);
		ArrayList<NState> SSS = new ArrayList<NState>();
		SSS.addAll(newStartStateSet);
		
		// System.out.println("SSS: " + SSS);
		// if(SSS.get(0) instanceof NState)
		// {
		// 	System.out.println("Is a NState object");
		// }

		//add all states in power to DFAmachine and populate state map
		for(ArrayList<NState> item : power)
		{
			Collections.sort(item, NState.NStateComparator);
			//The null state
			if(item.size() != 0)
			{
				String name = "";
				boolean anyStateAccept = false;
				for(NState state : item)
				{
					name += state.getName();

					//make sure that accepting states are correct
					if(state.isAccepting())
					{
						anyStateAccept = true;
					}
				}


				State newState = new State(name, null, null, anyStateAccept);
				if(item.equals(oldStartState))
				{
					stateMap.put(newState, oldStartState);
					nstateMap.put(oldStartState, newState);
				}
				else if(item.equals(SSS))
				{
					stateMap.put(newState, SSS);
					nstateMap.put(SSS, newState);
				}
				else
				{
					stateMap.put(newState, item);
					nstateMap.put(item, newState);	
				}
				DFAmachine.add(newState);
			}

			else
			{
				stateMap.put(nullState, item);
				nstateMap.put(item, nullState);

			}
		}

		boolean flag = false;
		State finalStartState = nstateMap.get(oldStartState);
		//no epsilon transitions
		if(oldStartState.size() == SSS.size())
		{
			Collections.sort(oldStartState, NState.NStateComparator);
			finalStartState = nstateMap.get(oldStartState);
			//System.out.println("finalStartSte is : " + finalStartState);
		}

		//there are epsilon transitions; update start state and
		//remove the old start state
		else
		{
			Collections.sort(SSS, NState.NStateComparator);
			Collections.sort(oldStartState, NState.NStateComparator);
			System.out.println(oldStartState);
			System.out.println(SSS);
			if(SSS.equals(oldStartState))
			{
				//System.out.println("SSS does equal oldStartState");
				finalStartState = nstateMap.get(oldStartState);	
			}
			else
			{
				flag = true;
				System.out.println("SSS doe snot equal oldStartState " + nstateMap.get(SSS));
				if(nstateMap.get(SSS) != null)
				{
					finalStartState = nstateMap.get(SSS);
				}
				else
				{
					finalStartState = nstateMap.get(oldStartState);
				}
				
			}
			//System.out.println("finalStartState is: " + finalStartState);
			DFAmachine.remove(oldStartState.get(0));
			nstateMap.remove(oldStartState);
			stateMap.remove(oldStartState.get(0));
		}

		//System.out.println(flag);

		//System.out.println("Start state: " + finalStartState.getName());
		
		//set all the transistions in the machine
		for(State s : DFAmachine)
		{
			ArrayList<NState> currentNStates = stateMap.get(s);

			HashSet<NState> allStatesATransitions = new HashSet<NState>();
			HashSet<NState> allStatesBTransitions = new HashSet<NState>();

			//for the current state, grab all of the a, b, and epsilon transistions that
			//the NStates can go to and put them in sets
			for(NState currentNS : currentNStates)
			{
				NState[] cnsATrans = currentNS.getA();
				NState[] cnsBTrans = currentNS.getB();

				for(int i = 0; i < cnsATrans.length; i++)
				{
					allStatesATransitions.add(cnsATrans[i]);
				}

				for(int j = 0; j < cnsBTrans.length; j++)
				{
					allStatesBTransitions.add(cnsBTrans[j]);
				}

			}

			//after we grab all the a and b transitions, we check those
			//transitions for epsilon transitions, and add that to our set
			HashSet<NState> allStatesEpsilonTrans = new HashSet<NState>();
			for(NState currentAA : allStatesATransitions)
			{
				NState[] asETrans = currentAA.getEpsilon();
				for(int k = 0; k < asETrans.length; k++)
				{
					allStatesEpsilonTrans.add(asETrans[k]);
				}
				
			}
			allStatesATransitions.addAll(allStatesEpsilonTrans);
			allStatesEpsilonTrans = new HashSet<NState>();

			for(NState currentAB : allStatesBTransitions)
			{
				NState[] asETrans = currentAB.getEpsilon();
				for(int k = 0; k < asETrans.length; k++)
				{
					allStatesEpsilonTrans.add(asETrans[k]);
				}
				
			}
			allStatesBTransitions.addAll(allStatesEpsilonTrans);

			ArrayList<NState> a = new ArrayList<NState>();
			ArrayList<NState> b = new ArrayList<NState>();
			a.addAll(allStatesATransitions);
			b.addAll(allStatesBTransitions);
			Collections.sort(a, NState.NStateComparator);
			Collections.sort(b, NState.NStateComparator);

			/*
			System.out.print(s.getName() + ": a tran: ");
			for(NState i : a)
			{
				System.out.print(i.getName());
			}

			System.out.print(" b tran: ");
			for(NState i : b)
			{
				System.out.print(i.getName());
			}
			System.out.println();
			*/

			//make all the connections; if the state we are trying to get to is not in
			//the map(i.e. a removed start state) we have this state point to the 
			//null state
			State stateATran = nstateMap.containsKey(a) ? nstateMap.get(a) : nullState;
			State stateBTran = nstateMap.containsKey(b) ? nstateMap.get(b) : nullState;
			s.setA(stateATran);
			s.setB(stateBTran);
			
		}	
		//remove all unreachable states
		ArrayList<State> toRemove = new ArrayList<State>();
		for(State r : DFAmachine)
		{
			ArrayList<State> visited = new ArrayList<State>();
			if(r == null)
			{
				System.out.println("State we are checking is null");
			}
			if(finalStartState == null)
			{
				System.out.println("finalStartState we have is null");
			}
			if(!DFA.isReachable(r, finalStartState, visited, DFAmachine.size()))
			{
				toRemove.add(r);
			}
		}
		for(State t : toRemove)
		{
			DFAmachine.remove(t);
		}
		State[] stateArray = new State[DFAmachine.size()];
		DFA finalMachine = new DFA(finalStartState, DFAmachine.toArray(stateArray), aTransition, bTransition);
		return finalMachine;
	}


	/**
     * Returns the power set from the given set by using a binary counter
     * Example: S = {a,b,c}
     * P(S) = {[], [c], [b], [b, c], [a], [a, c], [a, b], [a, b, c]}
     * @param set String[]
     * @return LinkedHashSet
     */
   	private <T> ArrayList<ArrayList<T>> powerSet(T[] set) 
   	{
     
       	//create the empty power set
    	ArrayList<ArrayList<T>> power = new ArrayList<ArrayList<T>>();
     
       	//get the number of elements in the set
       	int elements = set.length;
     
		//the number of members of a power set is 2^n
		int powerElements = (int) Math.pow(2,elements);

		//run a binary counter for the number of power elements
		for (int i = 0; i < powerElements; i++) 
		{

			//convert the binary number to a string containing n digits
			String binary = intToBinary(i, elements);

			//create a new set
			ArrayList<T> innerSet = new ArrayList<T>();

			//convert each digit in the current binary number to the corresponding element
			//in the given set
			for (int j = 0; j < binary.length(); j++) 
			{
			   if (binary.charAt(j) == '1')
			       innerSet.add(set[j]);
			}

			//add the new set to the power set
			power.add(innerSet);

		}

		return power;
	}
 
   /**
     * Converts the given integer to a String representing a binary number
     * with the specified number of digits
     * For example when using 4 digits the binary 1 is 0001
     * @param binary int
     * @param digits int
     * @return String
     */
   	private String intToBinary(int binary, int digits) 
   	{
     
       	String temp = Integer.toBinaryString(binary);
       	int foundDigits = temp.length();
       	String returner = temp;
       	for (int i = foundDigits; i < digits; i++) 
       	{
           	returner = "0" + returner;
       	}
     
       	return returner;
   	} 

	/**
	* main function used for testing
	* @param args command line arguments; unused
	**/
	public static void main(String[] args)
	{
		NState[] zero = new NState[0];

		NState A = new NState("A", zero, zero, zero, true);
		NState B = new NState("B", zero, zero, zero, false);
		NState C = new NState("C", zero, zero, zero, false);

		NState[] ABTransitions = {B};
		NState[] AETransitions = {C};
		NState[] BATransitions = {B, C};
		NState[] BBTransitions = {C};
		NState[] CATransitions = {A};

		A.setB(ABTransitions);
		A.setEpsilon(AETransitions);
		B.setA(BATransitions);
		B.setB(BBTransitions);
		C.setA(CATransitions);

		NState[] testMachine = {A,B,C};

		NFA testNFA = new NFA(A, testMachine, 'a', 'b');

		System.out.println("Testing NFA");
		System.out.println("---------------------------------------");
		for(int i = 0; i < testNFA.getMachine().length; i++)
		{
			NState[] at = testNFA.getMachine()[i].getA();
			NState[] bt = testNFA.getMachine()[i].getB();
			NState[] et = testNFA.getMachine()[i].getEpsilon();
			String a = "[";
			String b = "[";
			String e = "[";
			if(at.length != 0)
			{
				for(NState j : at)
				{	
					a += j.getName() + ", ";
				}

				a += "]";	
			}

			else
			{
				a += "NONE]";	
			}

			if(bt.length != 0)
			{
				for(NState j : bt)
				{	
					b += j.getName() + ", ";
				}	

				b += "]";
			}

			else
			{
				b += "NONE]";	
			}

			if(et.length != 0)
			{
				for(NState j : et)
				{	
					e += j.getName() + ", ";
				}	
			}

			else
			{
				e += "NONE]";	
			}

			


			System.out.println("State: " + testNFA.getMachine()[i].getName());
			System.out.println("	a transitions: " + a);
			System.out.println("	b transitions: " + b);
			System.out.println("	e transitions: " + e);
		}
		System.out.println("---------------------------------------");

		String[] test = {"", "a", "aa", "aaaa", "ba", "b", "bb", "bbb", "bbbb", "baab", "baaaab", "babb"};
		
		
		for(int i = 0; i < test.length; i++)
		{
			System.out.println(test[i]+ ": " + testNFA.inLanguage(test[i], testNFA.getStart()));
		}

		DFA testDFA = testNFA.getDFA();
		System.out.println("Testing DFA");
		System.out.println("---------------------------------------");
		for(int i = 0; i < testDFA.getMachine().length; i++)
		{
			System.out.println("State: " + testDFA.getMachine()[i].getName());
			System.out.println("	a transition: " + testDFA.getMachine()[i].getA().getName());
			System.out.println("	b transition: " + testDFA.getMachine()[i].getB().getName());
		}
		System.out.println("---------------------------------------");

		
		for(int i = 0; i < test.length; i++)
		{
			System.out.println(test[i]+ ": " + testDFA.inLanguage(test[i], testDFA.getStart()));
		}


	}
}