import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

/**
* Class that represents a Deterministic Finite Automata
* Has some functions that determine whether a string is in
* a language, as well as minimizing a DFA
**/
public class DFA extends FA
{

	/**
	* constructor for DFA
	* @param s starting state for DFA 
	* @param m array holding all states
	* @param a character on a transition
	* @param b character on b transition
	**/
	public DFA(State s, State[] m, char a, char b)
	{
		super(s, m, a, b);
	}

	public String toString()
	{
		String result = "DFA with start state: " + this.getStart().getName() + " :\n";
			  result += "DFA number of states: " + this.getMachine().length + "\n";
		result += "------------------------------------------\n";

		for(int i = 0; i < this.getMachine().length; i++)
		{
			result += this.getMachine()[i].toString();
		}
		return result;
	}

	/**
	* inLanguage determines if a certain string is in the language
	* @param s the string that is being tested
	* @param current the current state we are at in the machine
	* @return true if s is in language, else false
	**/
	public boolean inLanguage(String s, State current)
	{
		if(s.length() <= 0)
		{
			if(current.isAccepting())
			{
				return true;
			}

			return false;
		}

		else
		{
			if(s.charAt(0) == getATransition())
			{
				return inLanguage(s.substring(1, s.length()), current.getA());
			}

			else
			{
				return inLanguage(s.substring(1, s.length()), current.getB());
			}
		}

	}

	/**
	* isReachable checks if a certain state is reachable from the start state
	* @param s the state that is being checked
	* @param current the current state we are at (starts at start state)
	* @param visited the list of already visited states (initially empty)
	* @return true if state can be reached, else false
	**/
	public static boolean isReachable(State s, State current, ArrayList<State> visited, int machineSize)
	{
		if(current == s)
		{
			return true;
		}

		if(visited.size() >= machineSize)
		{
			return false;
		}

		else
		{
			boolean aValid = false;
			boolean bValid = false;
			//check if current state has already been checked
			if(!visited.contains(current))
			{
				visited.add(current);
			}

			//check if a transition has already been visited
			if(!visited.contains(current.getA()))
			{
				aValid = isReachable(s, current.getA(), visited, machineSize);
			}

			//check if b transition has already been visited
			if(!visited.contains(current.getB()))
			{
				bValid = isReachable(s, current.getB(), visited, machineSize);
			}

			return aValid || bValid;

		}
	}

	/**
	* DFAMinimization tries to reduce the total number of states
	* that are contained in our machine by merging states that are 
	* indistiguishable and roemovig states that are unreachable 
	* by the start state.
	**/
	public void DFAMinimization()
	{
		//remove any unreachable states
		ArrayList<State> updateMachine = new ArrayList<State>();
		for(int y = 0; y < machine.length; y++)
		{
			ArrayList<State> visited = new ArrayList<State>();
			if(isReachable(getMachine()[y], getStart(), visited, machine.length))
			{
				updateMachine.add(machine[y]);
			}
		}

		if(machine.length != updateMachine.size())
		{
			updateMachine.toArray(machine);
			machine = Arrays.copyOf(machine, updateMachine.size());
		}

		//create the initial distinguishability array
		boolean[][] isDistinguish = new boolean[machine.length - 1][];

		//number of changes for each iteration
		int numChanges = 0;

		for(int x = 0; x < machine.length - 1; x++)
		{
			isDistinguish[x] = new boolean[machine.length - x - 1];
		}

		//set the initial accept states that are distinguished to true
		for(int i = 0; i < machine.length - 1; i++)
		{
			State currentState = machine[i];

			//assume that DFA has a size of at least two
			for(int j = i + 1; j < machine.length; j++)
			{
				if(i != j)
				{
					if(currentState.isAccepting() != machine[j].isAccepting())
					{
						isDistinguish[i][j - (i + 1)] = true;
						numChanges++;
					}

				}
			}
		}

		//determine which states are distinguishable and which are not
		while(numChanges > 0)
		{
			numChanges = 0;
			for(int i = 0; i < machine.length - 1 ; i++)
			{
				State currentState = machine[i];

				//assume that DFA has a size of at least two
				for(int j = i + 1; j < machine.length; j++)
				{
					//make sure we haven't already set this node pair as distinguished
					if(i != j && !isDistinguish[i][j - (i + 1)])
					{
						State currentA = currentState.getA();
						State compareA = machine[j].getA();

						State currentB = currentState.getB();
						State compareB = machine[j].getB();

						//this pair is distiguishable
						if(currentA.isAccepting() != compareA.isAccepting())
						{
							isDistinguish[i][j - (i + 1)] = true;
							numChanges++;
						}

						else if(currentB.isAccepting() != compareB.isAccepting())
						{
							isDistinguish[i][j - (i + 1)] = true;
							numChanges++;
						}


					}
				}
			}
		}

		State[] mergeStates = new State[2*machine.length];
		int mergeIndex = 0;

		State[] newMachine = new State[machine.length];
		int newIndex = 0;

		//merge classes that are indistinguished
		for(int i = 0; i < machine.length - 1; i++)
		{
			for(int j = i + 1; j < machine.length; j++)
			{
				if(!isDistinguish[i][j - (i + 1)])
				{
					State mergeState1 = machine[i];
					State mergeState2 = machine[j];

					State newState = new State(mergeState1.getName() + mergeState2.getName(), 
						null, null, mergeState1.isAccepting());

					State newStateA;
					State newStateB;

					//new state loops on a transtition
					if(mergeState1.getA() == mergeState2 && mergeState2.getA() == mergeState1)
					{
						newStateA = newState;
					}

					//both merge states point to some other similar state
					else
					{
						newStateA = mergeState1.getA();
					}

					//new state loops on b transition
					if(mergeState1.getB() == mergeState2 && mergeState2.getB() == mergeState1)
					{
						newStateB = newState;
					}

					//both merge states point to some other similar state
					else
					{
						newStateB = mergeState1.getB();
					}

					newState.setA(newStateA);
					newState.setB(newStateB);


					mergeStates[mergeIndex] = mergeState1;
					mergeStates[mergeIndex+1] = mergeState2;
					newMachine[newIndex] = newState;
					mergeIndex+=2;
					newIndex++;

					//have old states in machine point to newState when they point to mergeState 1 or 2
					for(int x = 0; x < machine.length; x++)
					{
						State currentUpdateState = machine[x];
						if(currentUpdateState.getA() == mergeState1 || currentUpdateState.getA() == mergeState2)
						{
							currentUpdateState.setA(newState);
						}

						if(currentUpdateState.getB() == mergeState1 || currentUpdateState.getB() == mergeState2)
						{
							currentUpdateState.setB(newState);
						}
					}

				}
			}
		}

		if(mergeStates.length != 0)
		{
			//remove old 'merged' states
			for(int x = 0; x < machine.length; x++)
			{
				boolean found = false;
				State currentState;
				for(int y = 0; y < mergeStates.length; y++)
				{
					currentState = mergeStates[y];
					if(machine[x] == mergeStates[y])
					{
						found = true;
						break;
					}
				}

				if(!found)
				{
					newMachine[newIndex] = machine[x];
					newIndex++;
				}
			}
			this.machine = Arrays.copyOf(newMachine, newIndex);	
		}
		
	}

	/**
	* getDFA takes a regular expression and converts it into a DFA
	* @param regex the string to be converted
	* @return a DFA that corresponds to the given regular expression
	**/
	public static DFA getDFA(String regex, char aTran, char bTran)
	{
		NFA result = getNFA(regex, 1, aTran, bTran);
		//System.out.println(result.toString());
		DFA convert = result.getDFA();
		//convert.DFAMinimization();
		return convert;
	}


	/**
	* helper for getDFA that generates an NFA from the regular expression
	* which will be converted into a DFA in getDFA
	* @param regex the string to be converted
	* @param nameIndex the names of the new states (initially start at 1)
	* @return an NFA that represents the regular expression
	**/
	private static NFA getNFA(String regex, int nameIndex, char aTran, char bTran)
	{
		int index = 0;
		int barLocation = conatainsUnnestedBar(regex);
		//we have an empty string, we should return a single state NFA
		if(regex.length() == 0)
		{
			NState s = new NState(Integer.toString(nameIndex), null, null, null, true);
			NState[] m = {s};
			return new NFA(s, m, 'a', 'b');
		}

		if(regex.charAt(index) == '(')
		{
			int parenCount = 0;
			int rightParenIndex = 0;
			boolean found = false;
			for(int i = 0; i < regex.length(); i++)
			{
				if(regex.charAt(i) == '(')
				{
					parenCount++;
				}

				if(regex.charAt(i) == ')')
				{
					parenCount--;
					if(parenCount == 0)
					{
						rightParenIndex = i;
						found = true;
					}
				}

				if(found)
				{
					break;
				}
			}

			NFA right = getNFA(regex.substring(1, rightParenIndex), nameIndex, aTran, bTran);
			index = rightParenIndex;

			//this machine has a Kleene Star
			if(regex.length() > rightParenIndex + 1 && regex.charAt(rightParenIndex+1) == '*')
			{
				right = kleeneStar(right, nameIndex);
				//created a new state in kleeneStar, have to increment to the naming
				nameIndex++;
				index++;
			
			}

			//we've eaten through the entire string at this point, return our result
			if(index + 1 >= regex.length())
			{
				return right;
			}

			//there still is some of the regex to look through
			else
			{
				return concat(right, getNFA(regex.substring(index + 1, regex.length()), nameIndex, aTran, bTran));
			}
		}

		//there is no parenthesis to go into but there is a '|' found for union
		else if(barLocation != -1)
		{
			NFA left = getNFA(regex.substring(0, barLocation), nameIndex, aTran, bTran);
			NFA right = getNFA(regex.substring(barLocation + 1, regex.length()), nameIndex, aTran, bTran);

			return union(left, right, nameIndex);

		}

		//first character is a vlid character that is to be concatenated onto our state machine
		else
		{
			NState start = new NState(Integer.toString(nameIndex), null, null, null, false);
			NState transition = new NState(Integer.toString(nameIndex + 1), null, null, null,true);
			NState[] e = {transition};
			nameIndex += 2;
			if(regex.charAt(0) == aTran)
			{
				start.setA(e);
			}

			else
			{
				start.setB(e);
			}

			NState[] m = {start, transition};
			NFA right = new NFA(start, m, aTran, bTran);

			if(regex.length() > 1 && regex.charAt(1) == '*')
			{
				right = kleeneStar(right, nameIndex);
				nameIndex++;
				index++;
			}

			if(index + 1 >= regex.length())
			{
				return right;
			}

			else
			{
				return concat(right, getNFA(regex.substring(1, regex.length()), nameIndex, aTran, bTran));
			}
		}
	}

	/**
	* determine if string contains a '|' that is not nested insode of any parens
	* @param regex the string to search
	* @return index of bar, or if none found -1
	**/
	private static int conatainsUnnestedBar(String regex)
	{
		int parenCount = 0;
		for(int i = 0; i < regex.length(); i++)
		{
			if(regex.charAt(i) == '(')
			{
				parenCount++;
			}

			if(regex.charAt(i) == ')')
			{
				parenCount--;
			}

			if(regex.charAt(i) == '|' && parenCount == 0)
			{
				return i;
			}
		}

		return -1;
	}

	/**
	* concats two NFA's together
	* @param left the NFA on the left
	* @param right the NFA on the right
	* @return a concatenated NFA
	**/
	private static NFA concat(NFA left, NFA right)
	{
		//will point to the old accept states to the new non-accept in the new machine
		HashMap oldAccept = new HashMap<NState, NState>();
		NState newStart = left.getStart();
		NState[] newMachine = new NState[left.getMachine().length + right.getMachine().length];
		//Add all state from machine on the left
		for(int i = 0; i < left.getMachine().length; i++)
		{
			NState currentState = left.getMachine()[i];
			if(left.getAcceptStates().contains(currentState))
			{
				NState[] newEps = new NState[currentState.getEpsilon().length + 1];
				for(int k = 0; k < currentState.getEpsilon().length; k++)
				{
					newEps[k] = currentState.getEpsilon()[k];
				}
				newEps[currentState.getEpsilon().length] = right.getStart();
				currentState.setEpsilon(newEps);
				currentState.setIsAccepting(false);
			}

			newMachine[i] = currentState;
		}

		for(int j = 0; j < right.getMachine().length; j++)
		{
			newMachine[j + left.getMachine().length] = right.getMachine()[j];
		}

		return new NFA(newStart, newMachine, left.getATransition(), left.getBTransition());

	}

	/**
	* performs a kleene star operation on an NFA
	* @param star the NFA to be kleene starred
	* @param nameIndex the name to be given to the new start state
	* @return a new NFA
	**/
	private static NFA kleeneStar(NFA star, int nameIndex)
	{
		NState newStart = new NState(Integer.toString(nameIndex), null, null, null, false);
		NState[] newMachine = new NState[star.getMachine().length + 1];
		star.getStart().setIsAccepting(true);
		//need to add old start state to acceptStates in star
		NState[] e = {star.getStart()};
		newStart.setEpsilon(e);
		for(NState n : star.getAcceptStates())
		{
			NState[] newEps = new NState[n.getEpsilon().length + 1];
			for(int i = 0; i < n.getEpsilon().length; i++)
			{
				newEps[i] = n.getEpsilon()[i];
			}
			newEps[n.getEpsilon().length] = star.getStart();
			n.setEpsilon(newEps);
		}
		star.addAcceptState(star.getStart());

		for(int j = 0; j < star.getMachine().length; j++)
		{
			newMachine[j] = star.getMachine()[j];
		}
		newMachine[star.getMachine().length] = newStart;
		return new NFA(newStart, newMachine, star.getATransition(), star.getBTransition());
	}

	/**
	* performs a union operation on the two given NFA
	* @param left one of the NFAs to be unioned
	* @param right the other NFA
	* @return the new NFA that is the union of the two 
	**/
	private static NFA union(NFA left, NFA right, int nameIndex)
	{
		NState newStart = new NState(Integer.toString(nameIndex), null,null,null, false);
		NState[] e = {left.getStart(), right.getStart()};
		newStart.setEpsilon(e);
		NState[] newMachine = new NState[left.getMachine().length + right.getMachine().length + 1];
		for(int i = 0; i < left.getMachine().length; i++)
		{
			newMachine[i] = left.getMachine()[i];
		} 

		for(int j = 0; j < right.getMachine().length; j++)
		{
			newMachine[j + left.getMachine().length] = right.getMachine()[j];
		}
		newMachine[left.getMachine().length + right.getMachine().length + 1] = newStart;
		return new NFA(newStart, newMachine, left.getATransition(), left.getBTransition());
	}

	/**
	* main used for testing
	* @param args command line arguments unused
	**/
	public static void main(String[] args)
	{

		State A = new State("A", null, null, false);
		State B = new State("B", null, null, true);
		State C = new State("C", null, null, false);
		State D = new State("D", null, null, false);
		State E = new State("E", null, null, false);

		A.setA(B);
		A.setB(C);

		B.setA(B);
		B.setB(B);

		C.setA(D);
		C.setB(D);

		D.setA(C);
		D.setB(C);

		E.setA(E);
		E.setB(E);

		State[] machine1 = {A, B, C, D, E};

		DFA M = new DFA(A, machine1, 'a', 'b');

		/*
		for(State s : M.getMachine())
		{
			System.out.print(s.getName() + " ");	
		}

		System.out.println("\nMinimization");

		M.DFAMinimization();
	
		System.out.println("Machine size: " + M.getMachine().length);
		for(int x = 0; x < M.getMachine().length; x++)
		{
			System.out.print(M.getMachine()[x].getName());	
			System.out.println(" with A transition: " + M.getMachine()[x].getA().getName() +
				" and B transition: " +  M.getMachine()[x].getB().getName());
		}

		String[] test1 = {"a", "", "baa", "abba"};
		for(int i = 0; i < test1.length; i++)
		{
			System.out.println(test1[i] + " is in language: " + M.inLanguage(test1[i], M.getStart()));
		}
		*/

		String[] test = {"a", "a*", "(a)", "(a)*", "ab", "ab*", "a|b", "(a|b)"};
		DFA[] r = new DFA[test.length];
		for(int i = 0; i < test.length; i++)
		{
			r[i] = getDFA(test[i], 'a', 'b');
			System.out.println("TEST STRING:" + test[i]);
			System.out.println(r[i].toString());
		}


	}
}