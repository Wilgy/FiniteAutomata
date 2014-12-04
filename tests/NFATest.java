import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Used for test all primary functions in NFA.java
 **/
public class NFATest
{
	public static boolean testInLanguage1()
	{
		return true;
	}

	public static boolean testInLanguage2()
	{
		return true;
	}

	public static boolean testInLanguage3()
	{
		return true;
	}

	public static boolean testInLanguage4()
	{
		return true;
	}

	/**
	* Language described by this NFA: (a|b)*(aa)(a|b)*
	*
	**/
	public static NFA getNFA1()
	{
		NState A = new NState("A", null, null, null, false);
		NState B = new NState("B", null, null, null, false);
		NState C = new NState("C", null, null, null, true);

		NState[] AA = {B};
		NState[] BA = {B};
		NState[] BB = {B, C};
		A.setA(AA);
		B.setA(BA);
		B.setB(BB);

		NState[] machine = {A, B, C};

		NFA N = new NFA(A, machine, 'a', 'b');

		return N;
	}


	/**
	* Language described by this NFA: (ab)*(aab)*
	*
	**/
	public static NFA getNFA2()
	{
		NState A = new NState("A", null, null, null, false);
		NState B = new NState("B", null, null, null, false);
		NState C = new NState("C", null, null, null, true);
		NState D = new NState("D", null, null, null, false);
		NState E = new NState("E", null, null, null, false);

		NState[] AA = {B};
		NState[] AE = {C};
		NState[] BB = {A};
		NState[] CA = {D};
		NState[] DA = {E};
		NState[] EB = {C};

		A.setA(AA);
		A.setEpsilon(AE);
		B.setB(BB);
		C.setA(CA);
		D.setA(DA);
		E.setB(EB);

		NState[] machine = {A, B, C, D, E};

		NFA N = new NFA(A, machine, 'a', 'b');

		return N;
	}

	/**
	* Language described by this NFA: ((ab)*|(aba)*)
	*
	**/
	public static NFA getNFA3()
	{
		NState A = new NState("A", null, null, null, false);
		NState B = new NState("B", null, null, null, false);
		NState C = new NState("C", null, null, null, false);
		NState D = new NState("D", null, null, null, false);
		NState E = new NState("E", null, null, null, false);
		NState F = new NState("F", null, null, null, false);

		return null;
	}

	public static NFA getNFA4()
	{
		return null;
	}


	public static void main(String[] args)
	{
		System.out.println("Running NFATest...");
		System.out.println("	NFATest.testInLanguage1(): " + testInLanguage1());
		System.out.println("	NFATest.testInLanguage2(): " + testInLanguage2());
		System.out.println("	NFATest.testInLanguage3(): " + testInLanguage3());
		System.out.println("	NFATest.testInLanguage4(): " + testInLanguage4());
	}
	

}