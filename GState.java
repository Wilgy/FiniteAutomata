import java.util.ArrayList;
import java.util.HashMap;

public class GState
{
	private String name;

	//edges that lead to other states from this state
	private HashMap<GState, String> goingTo;

	//edges that are coming from other states to this state
	private HashMap<GState, String> comingFrom;

	//whether this state is accepting or not
	private boolean isAccept;

	public GState(String n, HashMap<GState, String> gt, HashMap<GState, String> cf, boolean i)
	{
		name = n;
		if(gt == null)
		{
			goingTo = new HashMap<GState, String>();
		}
		else
		{
			goingTo = gt;
		}

		if(cf == null)
		{
			comingFrom = new HashMap<GState, String>();
		}

		else
		{
			comingFrom = cf;
		}
		
		isAccept = i;
	}

	public String getName()
	{
		return name;
	}

	public HashMap<GState, String> getGoingTo()
	{
		return goingTo;
	}

	public HashMap<GState, String> getComingFrom()
	{
		return comingFrom;
	}

	public boolean isAccepting()
	{
		return isAccept;
	}

	public void addGoingTo(GState g, String s)
	{
		goingTo.put(g, s);
	}

	public void addComingFrom(GState g, String s)
	{
		comingFrom.put(g, s);
	}

	public void setGoingTo(HashMap<GState, String> gt)
	{
		goingTo = gt;
	}

	public void setComingFrom(HashMap<GState, String> cf)
	{
		comingFrom = cf;
	}

	public void removeGoingTo(GState g)
	{
		goingTo.remove(g);
	}

	public void removeComingFrom(GState g)
	{
		comingFrom.remove(g);
	}

}