import java.util.ArrayList

public class NStateArrayToState
{
	private ArrayList<NState> nStateArray;

	private State state;

	public NStateArrayToState(ArrayList<NState> ns, State s)
	{
		nStateArray = ns;
		state = s;
	}

	public ArrayList<NState> getNStateArray()
	{
		return nStateArray;
	}

	public State getState()
	{
		return state;
	}

	public void setNStateArray(ArrayList<NState> ns)
	{
		nStateArray = ns;
	}

	public void setState(State s)
	{
		state = s;
	}
}