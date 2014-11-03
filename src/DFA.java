import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


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

	/**
	* returns a string representation of this DFA; includes the start state, 
	* the number of states, and info about each state
	* @return a string that represents the DFA
	**/
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
	* @param machineSize the total number of states in the machine
	* @return true if state can be reached, else false
	**/
	public static boolean isReachable(State s, State current, ArrayList<State> visited, int machineSize)
	{
		//we've found the state we're looking for, return true
		if(current == s)
		{
			return true;
		}

		//if we've visited every state machine and still haven't found our state,
		//then is can't be reachable
		if(visited.size() >= machineSize)
		{
			return false;
		}

		else
		{
			boolean aValid = false;
			boolean bValid = false;
			//check if current state has already been checked, if so we do not 
			//want to rechceck that state in later calls
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
	* prints a 2d array of boolean values
	* @param arr the array to print
	**/
	public static void printAbstractArray(boolean[][] arr) 
	{
		for(int i = 0; i < arr.length; i++)
		{
			System.out.print("Row #" + i + ": [");
			for(int j = 0; j < arr[i].length; j++ )
			{
				System.out.print(" " + arr[i][j] + ", ");
			}
			System.out.println("]");
		}

	}

	/**
	* removeUnreachableStates removes any unreachable states in a given state machine
	* @param m the machine that is being searched for states that can be removed
	* @param start the start state of the machine
	* @return the new machine
	**/
	public static State[] removeUnreachableStates(State[] m, State start)
	{
		ArrayList<State> updateMachine = new ArrayList<State>();
		for(int y = 0; y < m.length; y++)
		{
			ArrayList<State> visited = new ArrayList<State>();
			if(isReachable(m[y], start, visited, m.length))
			{
				updateMachine.add(m[y]);
			}
		}

		if(m.length != updateMachine.size())
		{
			updateMachine.toArray(m);
			m = Arrays.copyOf(m, updateMachine.size());
		}
		return m;
	}

	/**
	* grabs all states the make a transition to the state passed in
	* @param m the machine of states
	* @param s the state that is being transitioned to
	* @return an ArrayList of States that make a transition to s
	**/
	private static ArrayList<State> getStatesWithTransitions(ArrayList<State> m, State s)
	{
		ArrayList<State> states = new ArrayList<State>();
		for(State current : m)
		{
			//will ocmpare states by reference, not sure if this is a good idea (may want to use equals())
			if(current.getA() == s || current.getB() == s)
			{
				states.add(current);
			}
		}

		return states;
	}


	/**
	* createDistinguishTable creates a 'table' of values where pairs of states 
	* are mapped to a boolean value that represents whether or not those two 
	* states are indistinguishable (FALSE) or distinguishable (TRUE)
	* @param m the machine that we are used to generate the table (is unmodifed)
	* @return the table of distinguishable states
	**/
	public static HashMap<State, HashMap<State, Boolean>> createDistinguishTable(State[] m)
	{
		HashMap<State, HashMap<State, Boolean>> isDistinguish = new HashMap<State, HashMap<State, Boolean>>();
		//Create the initial map with all the values set to false	
		for(int i = 0; i < m.length; i++)
		{
			State firstState = m[i];
			isDistinguish.put(firstState, new HashMap<State, Boolean>());
			for(int j = i + 1; j < m.length; j++)
			{	
				if(i != j)
				{
					State secondState = m[j];
					isDistinguish.get(firstState).put(secondState, Boolean.FALSE);
				}
			}
		}

		Set<State> pairings = isDistinguish.keySet();
		int numChanges = 0;
		for(State firstState : pairings)
		{
			Set<State> pairedStates = isDistinguish.get(firstState).keySet();
			for(State secondState : pairedStates)
			{
				//if one of the states is accepting and the other isnt, then they are
				//consider distinguishable
				if(firstState.isAccepting() != secondState.isAccepting())
				{
					isDistinguish.get(firstState).put(secondState, Boolean.TRUE);

					// System.out.println("Found a pair that are distinguishable in FIRST LOOP!");
					// System.out.println(firstState.toString());
					// System.out.println(secondState.toString());
					numChanges++;
				}
			}
		}

		while(numChanges > 0)
		{
			numChanges = 0;
			for(State firstState : pairings)
			{
				Set<State> pairedStates = isDistinguish.get(firstState).keySet();
				for(State secondState : pairedStates)
				{
					if(!isDistinguish.get(firstState).get(secondState))
					{
						State currentA = firstState.getA();
						State compareA = secondState.getA();

						State currentB = firstState.getB();
						State compareB = secondState.getB();

						//If either (currentA, compareA) or (compareA, currentA) are in isDistinguish and
						//have already been confirmed as distinguish, we need to set (firstState, secondState)
						//pair as distinguished as well and increment the numChanges we have made to isDistinguish
						if( isDistinguish.get(currentA) != null && isDistinguish.get(currentA).get(compareA) != null 
							&& isDistinguish.get(currentA).get(compareA) )
						{
							isDistinguish.get(firstState).put(secondState, Boolean.TRUE);
							numChanges++;
						}

						else if( isDistinguish.get(compareA) != null && isDistinguish.get(compareA).get(currentA) != null 
							&& isDistinguish.get(compareA).get(currentA) )
						{
							isDistinguish.get(firstState).put(secondState, Boolean.TRUE);
							numChanges++;
						}

						//Also need to check the (currentB, compareB) and (compareB, currentB) pairs as well
						else if( isDistinguish.get(currentB) != null && isDistinguish.get(currentB).get(compareB) != null 
							&& isDistinguish.get(currentB).get(compareB) )
						{
							isDistinguish.get(firstState).put(secondState, Boolean.TRUE);
							numChanges++;
						}

						else if( isDistinguish.get(compareB) != null && isDistinguish.get(compareB).get(currentB) != null 
							&& isDistinguish.get(compareB).get(currentB) )
						{
							isDistinguish.get(firstState).put(secondState, Boolean.TRUE);
							numChanges++;
						}
					}
				}
			}
		}

		return isDistinguish;
	}

	/**
	* mergerIndistinguishableStates merges all states that are marked indistinguichable and generates a new DFA
	* to represent the new (smaller) machine.
	* @param m the old state machine
	* @param isDistinguish the distinguishability table that holds the info on which states are indinstinguishable
	* @param startState the old startState; if the old startState is merged with another state, we need to update it
	* @param a the a transtion, used to generate a DFA
	* @param b the b transtion, used to generate a DFA
	* @return a new DFA that has merged all indistinguishable states
	**/
	private static DFA mergeIndistinguishableStates( State[] m, HashMap<State, HashMap<State, Boolean>> isDistinguish, State startState, char a, char b )
	{
		ArrayList<State> newMachine = new ArrayList<State>();
		State newStartState = null;
		Set<State> pairings = isDistinguish.keySet();

		int newIndex = 0;

		//first add all of the old states
		for(int i = 0; i < m.length; i++)
		{
			newMachine.add(m[i]);
			newIndex++;
		}

		HashMap<State, State> oldStatesToNewStates = new HashMap<State, State>();

		//now, go through the disitinguishability table, and find the pairs of states that are
		//indistinguishable.  Then merge the two states by doing the following:
		// 		1. Create new state with merged name and correct acceptibilty
		//		2. Find all of the old states that made a refrence to either of the states
		//		3. Have these states transition point to the new state
		//		4. Create the newStaes A nd B transitions
		//			a. If the two states point to each other in some way, have the newState point to itself
		//			b. If its not pointing to itself, it must be pointing to a similar state
		//		5. Remove the old states from the newMachine.  We may have a problem where the isDistinguish 
		//		   refers to states that have been removed and we may get bad data.
		for(State firstState : pairings)
		{
			Set<State> pairedStates = isDistinguish.get(firstState).keySet();
			for( State secondState : pairedStates)
			{
				//if there not distinguishable, then firstState and secondState will need to be merged
				if(!isDistinguish.get(firstState).get(secondState) )
				{
					//if the firstState or secondState refer to states that have been removed,
					//we need to update what state they are talking about so as grab correct data
					if(oldStatesToNewStates.get(firstState) != null)
					{
						firstState = oldStatesToNewStates.get(firstState);
					}
					if(oldStatesToNewStates.get(secondState) != null)
					{
						secondState = oldStatesToNewStates.get(secondState);
					}

					String newName = firstState.getName().compareTo(secondState.getName()) <= 0 ? firstState.getName() + secondState.getName() : secondState.getName() + firstState.getName();
					State newState = new State(newName, null, null, (firstState.isAccepting() || secondState.isAccepting()));
					if(firstState == startState || secondState == startState)
					{
						newStartState = newState;
					}

					oldStatesToNewStates.put(firstState, newState);
					oldStatesToNewStates.put(secondState, newState);

					State newStateA;
					State newStateB;

					//new state loops on a transtition
					if( (firstState.getA() == secondState && secondState.getA() == firstState) || (firstState.getA() == firstState && secondState.getA() == secondState)
						|| (firstState.getA() == secondState && secondState.getA() == secondState) || (firstState.getA() == firstState && secondState.getA() == firstState) )
					{
						newStateA = newState;
					}

					//both merge states point to some other similar state
					else
					{
						newStateA = firstState.getA();
					}

					//new state loops on b transition
					if( (firstState.getB() == secondState && secondState.getB() == firstState) || (firstState.getB() == firstState && secondState.getB() == secondState)
						|| (firstState.getB() == secondState && secondState.getB() == secondState) || (firstState.getB() == firstState && secondState.getB() == firstState) )
					{
						newStateB = newState;
					}

					//both merge states point to some other similar state
					else
					{
						newStateB = firstState.getB();
					}

					newState.setA(newStateA);
					newState.setB(newStateB);


					ArrayList<State> statesTransistionToFirst = getStatesWithTransitions(newMachine, firstState);
					ArrayList<State> statesTransistionToSecond = getStatesWithTransitions(newMachine, secondState);

					//rewrite the old state transitions for the firstState
					for(State s : statesTransistionToFirst)
					{
						if(s.getA() == firstState)
						{
							s.setA(newState);
						}
						else
						{
							s.setB(newState);
						}
					}
					//rewrite the old state transitions for the secondState
					for(State s : statesTransistionToSecond)
					{
						if(s.getA() == firstState)
						{
							s.setA(newState);
						}
						else
						{
							s.setB(newState);
						}
					}

					//add the new state to the newMachine and remove the old two states
					newMachine.add(newState);
					newMachine.remove(firstState);
					newMachine.remove(secondState);
					newIndex--;

				}
			}
		}

		DFA newDFA;
		State[] newMachineArr = new State[newMachine.size()];
		if(newStartState != null)
		{
			newDFA = new DFA(newStartState, newMachine.toArray(newMachineArr), a, b);
		}
		else
		{
			newDFA = new DFA(startState, newMachine.toArray(newMachineArr), a, b);
		}

		return newDFA;

	}


	/**
	* DFAMinimization tries to reduce the total number of states
	* that are contained in our machine by merging states that are 
	* indistiguishable and removing states that are unreachable 
	* by the start state.
	* Process:
	* 	1. Remove all states that are unreachable from the start state
	* 	2. For all pairs of states {p,q} such that p is accepting and q is not,
	*	   mark the equivalent cell in the table (see below)
	* 	3. Then consider each pair {p,q} that is not marked:
			a. Determine r = d(p,a) and s = d(q,a) for each a in the alphabet
			b. If {r,s} is marked in the table, then mark {p,q} in the table
		4. Repeat step 3 until no further cells are marked during an entire iteration
		   of an algorithm
		5. Once the table is complete, all unmarked cells represent two states 
		   that are indistinguishable (i.e equivalent)
		   	a. Combine these equivalent states into a single state and make sure 
		   	   their transitions map to the correct states
	* 
	* Example of table used for DFA minimization.  Let the DFA have states {A,B,C,D}
	*    _
	* B| _ | _
	* C| _ | _ | _
	* D| _ | _ | _ |
	*    A   B   C
	*
	* NOTE: We assume that the DFA has at least 2 states
	**/
	public void DFAMinimization()
	{
		//we cannot do a minimization of a machine that only has one state
		if(machine.length < 2)
		{
			return;
		}

		//Step 1. remove any unreachable states
		machine = DFA.removeUnreachableStates(machine, getStart());

		//create the initial distinguishability array (the table)
		//Step 2. set the initial accept states that are distinguished to true
		//we mark each item in the table where one state is accepting and one is not
		//therefore the two states must be distinguishable
		//Step 3 & 4. determine which states are distinguishable and which are not
		HashMap<State, HashMap<State, Boolean>> isDistinguish = createDistinguishTable(machine);

		//Step5. merge all indistinguishable states
		DFA newDFA = mergeIndistinguishableStates(this.machine, isDistinguish, getStart(), getATransition(), getBTransition());

		//update the machine
		this.machine = newDFA.getMachine();
		//update the start state
		this.setStart(newDFA.getStart());
	}

	/**
	* getDFAFromRegex takes a regular expression and converts it into a DFA
	* @param regex the string to be converted
	* @return a DFA that corresponds to the given regular expression
	**/
	public static DFA getDFAFromRegex(String regex, char aTran, char bTran)
	{
		NFA result = getNFAFromRegex(regex, 1, aTran, bTran);
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
	private static NFA getNFAFromRegex(String regex, int nameIndex, char aTran, char bTran)
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

			NFA right = getNFAFromRegex(regex.substring(1, rightParenIndex), nameIndex, aTran, bTran);
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
				return concat(right, getNFAFromRegex(regex.substring(index + 1, regex.length()), nameIndex, aTran, bTran));
			}
		}

		//there is no parenthesis to go into but there is a '|' found for union
		else if(barLocation != -1)
		{
			NFA left = getNFAFromRegex(regex.substring(0, barLocation), nameIndex, aTran, bTran);
			NFA right = getNFAFromRegex(regex.substring(barLocation + 1, regex.length()), nameIndex, aTran, bTran);

			return union(left, right, nameIndex);

		}

		//first character is a valid character that is to be concatenated onto our state machine
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
				return concat(right, getNFAFromRegex(regex.substring(1, regex.length()), nameIndex, aTran, bTran));
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

		State[] m1 = {A, B};
		State[] m2 = {B, A};
		State[] m3 = {A, B};

		Arrays.sort(m1);
		Arrays.sort(m2);
		Arrays.sort(m3);

		System.out.println(Arrays.deepEquals(m1, m2));
		System.out.println(Arrays.deepEquals(m1, m3));

		DFA M = new DFA(A, machine1, 'a', 'b');

		System.out.println(M.toString());

		M.DFAMinimization();

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

		/*
		String[] test = {"a", "a*", "(a)", "(a)*", "ab", "ab*", "a|b", "(a|b)"};
		DFA[] r = new DFA[test.length];
		for(int i = 0; i < test.length; i++)
		{
			r[i] = getDFAFromRegex(test[i], 'a', 'b');
			System.out.println("TEST STRING:" + test[i]);
			System.out.println(r[i].toString());
		}
		*/


	}
}