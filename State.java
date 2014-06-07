public class State
{
	private String name;

	private State a;

	private State b;

	boolean isAccept;

	public State(String name, State a, State b, boolean isAccept)
	{
		this.name = name;
		this.a = a;
		this.b = b;
		this.isAccept = isAccept;
	}

	public String getName()
	{
		return name;
	}

	public State getA()
	{
		return a;
	}

	public State getB()
	{
		return b;
	}

	public boolean isAccepting()
	{
		return isAccept;
	}

	public void setA(State s)
	{
		a = s;
	}

	public void setB(State s)
	{
		b = s;
	}
}