import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Used for test all primary functions in DFA.java
 **/
public class DFATest
{

	public static boolean testDFAMinimization1()
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
		M.DFAMinimization();

		String result = M.toString();
		State CD = new State("CD", null, null, false);
		A = new State("A", null, null, false);
		B = new State("B", null, null, true);
		CD.setA(CD);
		CD.setB(CD);
		A.setA(B);
		A.setB(CD);
		B.setA(B);
		B.setB(B);

		State[] machine2 = {CD, A, B};
		DFA N = new DFA(A, machine2, 'a', 'b');
		String correct = N.toString();

		return result.equals(correct);
	
	}

	public static boolean testDFAMinimization2()
	{
		State A = new State("A", null, null, false);
		State B = new State("B", null, null, false);
		State C = new State("C", null, null, true);
		State D = new State("D", null, null, false);
		State E = new State("E", null, null, false);
		State F = new State("F", null, null, false);
		State G = new State("G", null, null, false);
		State H = new State("H", null, null, false);

		A.setA(B);
		A.setB(F);

		B.setA(G);
		B.setB(C);

		C.setA(A);
		C.setB(C);

		D.setA(C);
		D.setB(G);

		E.setA(H);
		E.setB(F);

		F.setA(C);
		F.setB(G);

		G.setA(G);
		G.setB(E);

		H.setA(G);
		H.setB(C);

		State[] machine1 = {A, B, C, D, E, F, G, H};

		DFA M = new DFA(A, machine1, 'a', 'b');
		M.DFAMinimization();
		// System.out.println(M.toString());
		return true;
	}

	public static boolean testDFAMinimization3()
	{
		return true;	
	}

	public static boolean testDFAMinimization4()
	{
		return true;
	}


	public static boolean testCreateDistinguishTable1()
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
		machine1 = DFA.removeUnreachableStates(machine1, A);
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return true;

	}

	public static boolean testCreateDistinguishTable2()
	{
		State A = new State("A", null, null, false);
		State B = new State("B", null, null, false);
		State C = new State("C", null, null, true);
		State D = new State("D", null, null, false);
		State E = new State("E", null, null, false);
		State F = new State("F", null, null, false);
		State G = new State("G", null, null, false);
		State H = new State("H", null, null, false);

		A.setA(B);
		A.setB(F);

		B.setA(G);
		B.setB(C);

		C.setA(A);
		C.setB(C);

		D.setA(C);
		D.setB(G);

		E.setA(H);
		E.setB(F);

		F.setA(C);
		F.setB(G);

		G.setA(G);
		G.setB(E);

		H.setA(G);
		H.setB(C);

		State[] machine1 = {A, B, C, D, E, F, G, H};
		machine1 = DFA.removeUnreachableStates(machine1, A);
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return true;

	}

	public static boolean testCreateDistinguishTable3()
	{
		State A = new State("A", null, null, false);
		State B = new State("B", null, null, false);
		State C = new State("C", null, null, false);
		State D = new State("D", null, null, false);
		State E = new State("E", null, null, false);
		State F = new State("F", null, null, true);
		State G = new State("G", null, null, true);
		State H = new State("H", null, null, false);

		A.setA(H);
		A.setB(B);

		B.setA(H);
		B.setB(A);

		C.setA(E);
		C.setB(F);

		D.setA(E);
		D.setB(F);

		E.setA(F);
		E.setB(G);

		F.setA(F);
		F.setB(F);

		G.setA(G);
		G.setB(F);

		H.setA(C);
		H.setB(C);

		State[] machine1 = {A, B, C, D, E, F, G, H};
		machine1 = DFA.removeUnreachableStates(machine1, A);
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return true;

	}


	public static boolean testCreateDistinguishTable4()
	{
		State A = new State("A", null, null, true);
		State B = new State("B", null, null, false);
		State C = new State("C", null, null, true);
		State D = new State("D", null, null, false);
		State E = new State("E", null, null, true);

		A.setA(A);
		A.setB(B);

		B.setA(C);
		B.setB(D);

		C.setA(C);
		C.setB(E);

		D.setA(C);
		D.setB(D);

		E.setA(E);
		E.setB(E);

		State[] machine1 = {A, B, C, D, E};
		machine1 = DFA.removeUnreachableStates(machine1, A);
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return true;

	}


	public static void main(String[] args)
	{
		System.out.println("testDFAMinimization1: " + testDFAMinimization1());
		System.out.println("testDFAMinimization2: " + testDFAMinimization2());
		System.out.println("testDFAMinimization3: " + testDFAMinimization3());
		System.out.println("testDFAMinimization4: " + testDFAMinimization4());
		// System.out.println("testCreateDistinguishTable1: " + testCreateDistinguishTable1());
		// System.out.println("testCreateDistinguishTable2: " + testCreateDistinguishTable2());
		// System.out.println("testCreateDistinguishTable3: " + testCreateDistinguishTable3());		
		System.out.println("testCreateDistinguishTable4: " + testCreateDistinguishTable4());		

	}

}