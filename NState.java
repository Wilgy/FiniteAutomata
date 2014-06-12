import java.util.Comparator;

public class NState implements Comparable<NState>
{
	private String name;

	private NState[] a;

	private NState[] b;

	private NState[] epsilon;

	boolean isAccept;

	public NState(String name, NState[] a, NState[] b, NState[] epsilon, boolean isAccept)
	{
		this.name = name;
		this.a = a;
		this.b = b; 
		this.epsilon = epsilon;
		this.isAccept = isAccept;
	}

	public String getName()
	{
		return name;
	}

	public NState[] getA()
	{
		return a;
	}

	public NState[] getB()
	{
		return b;
	}

	public NState[] getEpsilon()
	{
		return epsilon;
	}

	public boolean isAccepting()
	{
		return isAccept;
	}

	public void setA(NState[] a)
	{
		this.a = a;
	}

	public void setB(NState[] b)
	{
		this.b = b;
	}

	public void setEpsilon(NState[] e)
	{
		epsilon = e;
	}

	public int compareTo(NState compareNState)
	{
		return this.name.compareTo(compareNState.getName());
	}

	public static Comparator<NState> NStateComparator = new Comparator<NState>()
	{
		public int compare(NState n1, NState n2)
		{
			return n1.compareTo(n2);
		}
	};
}