import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

/**
* Class that represents a Deterministic Finite Automata
* Has some functions that determine whether a string is in
* a language, as well as minimizing a DFA
**/
public class DFA extends FA
{

	/**
	* constructor for DFA
	* @param State s starting state for DFA 
	* @param State[] m array holding all states
	* @param char a character on a transition
	* @param char b character on b transition
	**/
	public DFA(State s, State[] m, char a, char b)
	{
		super(s, m, a, b);
	}


	/**
	* inLanguage determines if a certain string is in the language
	* @param String s the string that is being tested
	* @param State current the current state we are at in the machine
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
	* @param State s the state that is being checked
	* @param State current the current state we are at (starts at start state)
	* @param ArrayList<State> visited the list of already visited states (initially empty)
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
	* main used for testing
	* @param String[] args command line arguments unused
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

	}
}