import java.util.ArrayList;
import java.util.HashMap;

public class GNFA
{
	private ArrayList<GState> machine;

	private ArrayList<GState> acceptStates;
	
	private GState start;

	private char aTransition;

	private char bTransition;

	public GNFA(DFA d)
	{
		aTransition = d.getATransition();
		bTransition = d.getBTransition();
		machine = new ArrayList<GState>();
		acceptStates = new ArrayList<GState>();

		State[] dMachine = d.getMachine();
		HashMap<State, GState> mapTransition = new HashMap<State, GState>();
		for(int i = 0; i < dMachine.length; i++)
		{
			State curState = dMachine[i];
			GState curGState = new GState(curState.getName(), null, null, curState.isAccepting());
			machine.add(curGState);
			mapTransition.put(curState, curGState);
			if(curState.isAccepting())
			{
				acceptStates.add(curGState);
			}
		}

		for(int j = 0; j < dMachine.length; j++)
		{
			State curState = dMachine[j];
			GState curGState = mapTransition.get(curState);
			State curA = curState.getA();
			State curB = curState.getB();

			curGState.addGoingTo(mapTransition.get(curA), Character.toString(aTransition));
			curGState.addGoingTo(mapTransition.get(curB), Character.toString(bTransition));

			GState GAState = mapTransition.get(curA);
			GState GBState = mapTransition.get(curB);

			if(!GAState.getComingFrom().containsValue(curGState))
			{
				GAState.addComingFrom(curGState, Character.toString(aTransition));
			}

			if(!GBState.getComingFrom().containsValue(curGState))
			{
				GBState.addComingFrom(curGState, Character.toString(bTransition));
			}

		}

		start = mapTransition.get(d.getStart());
		
	}

	public GState getStart()
	{
		return start;
	}

	public ArrayList<GState> getMachine()
	{
		return machine;
	}

	public char getATransition()
	{
		return aTransition;
	}

	public char getBTransition()
	{
		return bTransition;
	}

	public ArrayList<GState> getAcceptStates()
	{
		return acceptStates;
	}

	public String getRegularExpression()
	{
		GState startState = new GState("START", null, null, start.isAccepting());
		startState.addGoingTo(start, "");
		start.addComingFrom(startState, "");

		GState endState = new GState("END", null, null, true);
		for(GState g : acceptStates)
		{
			g.addGoingTo(endState, "");
			endState.addComingFrom(g, "");
		}
	
		ArrayList<GState> newMachine = new ArrayList<GState>();
		newMachine.addAll(machine);

		for(int i = 0; i < machine.size(); i++)
		{
			GState curState = newMachine.get(i);
			String middleStr = "";
			if(curState.getComingFrom().get(curState) != null)
			{
				middleStr = curState.getComingFrom().get(curState);
				if(middleStr.length() > 1)
				{
					middleStr = "(" + middleStr + ")*";
				}

				else
				{
					middleStr += "*";
				}
			}

			for(GState c : curState.getComingFrom().keySet())
			{
				if(c != curState)
				{
					for(GState g : curState.getGoingTo().keySet())
					{
						if(g != curState)
						{
							// Used to view connections being removed
							// System.out.print(c.getName() + "- " + curState.getComingFrom().get(c) + " ->");
							// System.out.print(curState.getName() + "[" + middleStr + "]" + " - " +  curState.getGoingTo().get(g));
							// System.out.print("-> " + g.getName()+ ": ");
							// System.out.println(c.getName() + "- " + curState.getComingFrom().get(c) + middleStr +  curState.getGoingTo().get(g) + " ->" + g.getName());							
							String start = curState.getComingFrom().get(c);
							String end = curState.getGoingTo().get(g);
							boolean addParen = false;
							for(int j = 0; j < start.length(); j++)
							{
								if(start.charAt(j) == '|')
								{
									addParen = true;
									break;
								}

								if(start.charAt(j) == '(')
								{
									j = start.indexOf(')');
								}
							}

							if(addParen)
							{
								start = "(" + start + ")";
							}

							addParen = false;

							for(int j = 0; j < end.length(); j++)
							{
								if(end.charAt(j) == '|')
								{
									addParen = true;
									break;
								}

								if(end.charAt(j) == '(')
								{
									j = end.indexOf(')');
								}
							}
							if(addParen)
							{
								end = "(" + end + ")";
							}

							String mapping = start + middleStr + end;
							if(c.getGoingTo().get(g) != null)
							{
								String originalStr = c.getGoingTo().get(g);
								mapping = "(" + originalStr + "|" + mapping + ")";
								c.getGoingTo().remove(g);
								g.getComingFrom().remove(c);
							}
							c.addGoingTo(g, mapping);
							g.addComingFrom(c, mapping);
						}
					}
				}
			}
			curState.getComingFrom().clear();
			curState.getGoingTo().clear();
		}
		return startState.getGoingTo().get(endState);

	}



	public static void main(String[] args)
	{
		State A = new State("A", null, null, false);
		State B = new State("B", null, null, false);
		State C = new State("C", null, null, true);

		A.setA(B);
		A.setB(A);

		B.setA(C);
		B.setB(A);

		C.setA(A);
		C.setB(C);

		State[] dMachine = {A,B,C};

		DFA d = new DFA(A, dMachine, 'a', 'b');

		GNFA g = new GNFA(d);
	
		for(GState i : g.getMachine())
		{
			System.out.println("State: " + i.getName());
			System.out.print("	going to:   ");
			for(GState h : i.getGoingTo().keySet())
			{
				System.out.print(h.getName() + "  ");
			}
			System.out.println();

			System.out.print("	coming from:" );
			for(GState c : i.getComingFrom().keySet())
			{
				System.out.print(c.getName() + "  ");
			}
			System.out.println();

		}

		System.out.println("Regular expression: " + g.getRegularExpression());
	}

}