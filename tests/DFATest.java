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
		DFA DFA1 = getDFA1();
		DFA DFA1Minimized = getDFA1Minimized();

		DFA1.DFAMinimization();
		String result = DFA1.toString();
		String correct = DFA1Minimized.toString();
		return result.equals(correct);
	
	}

	public static boolean testDFAMinimization2()
	{
		DFA DFA2 = getDFA2();
		DFA DFA2Minimized = getDFA2Minimized();

		DFA2.DFAMinimization();
		String result = DFA2.toString();
		String correct = DFA2Minimized.toString();
		return result.equals(correct);
	}

	public static boolean testDFAMinimization3()
	{
		DFA DFA3 = getDFA3();
		DFA DFA3Minimized = getDFA3Minimized();

		DFA3.DFAMinimization();
		String result = DFA3.toString();
		String correct = DFA3Minimized.toString();
		return result.equals(correct);	
	}

	public static boolean testDFAMinimization4()
	{
		DFA DFA4 = getDFA4();
		DFA getDFA4Minimized = getDFA4Minimized();

		DFA4.DFAMinimization();
		String result = DFA4.toString();
		String correct = getDFA4Minimized.toString();

		return result.equals(correct);
	}


	public static boolean testCreateDistinguishTable1()
	{
		DFA DFA1 = getDFA1();
		State[] machine1 = DFA1.getMachine();
		machine1 = DFA.removeUnreachableStates(machine1, DFA1.getState("A"));
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return !result.get(DFA1.getState("C")).get(DFA1.getState("D"));

	}

	public static boolean testCreateDistinguishTable2()
	{
		DFA DFA2 = getDFA2();
		State[] machine1 = DFA2.getMachine();
		machine1 = DFA.removeUnreachableStates(machine1, DFA2.getState("A"));
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return !result.get(DFA2.getState("B")).get(DFA2.getState("H")) && !result.get(DFA2.getState("A")).get(DFA2.getState("E"));

	}

	public static boolean testCreateDistinguishTable3()
	{		
		DFA DFA3 = getDFA3();
		State[] machine1 = DFA3.getMachine();
		machine1 = DFA.removeUnreachableStates(machine1, DFA3.getState("A"));
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return !result.get(DFA3.getState("A")).get(DFA3.getState("B")) && !result.get(DFA3.getState("F")).get(DFA3.getState("G"));

	}

	public static boolean testCreateDistinguishTable4()
	{
		DFA DFA4 = getDFA4();
		State[] machine1 = DFA4.getMachine();
		machine1 = DFA.removeUnreachableStates(machine1, DFA4.getState("A"));
		HashMap<State, HashMap<State, Boolean>> result = DFA.createDistinguishTable(machine1);

		return !result.get(DFA4.getState("B")).get(DFA4.getState("D")) && !result.get(DFA4.getState("C")).get(DFA4.getState("E"));

	}


	public static DFA getDFA1()
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
		return M;
	}

	public static DFA getDFA1Minimized()
	{
		State A = new State("A", null, null, false);
		State B = new State("B", null, null, true);
		State CD = new State("CD", null, null, false);
		
		A.setA(B);
		A.setB(CD);

		B.setA(B);
		B.setB(B);

		CD.setA(CD);
		CD.setB(CD);

		State[] machine2 = {A, B, CD};
		DFA N = new DFA(A, machine2, 'a', 'b');
		return N;
	}

	public static DFA getDFA2()
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
		return M;
	}

	public static DFA getDFA2Minimized()
	{
		State C = new State("C", null, null, true);
		State F = new State("F", null, null, false);
		State G = new State("G", null, null, false);
		State BH = new State("BH", null, null, false);
		State AE = new State("AE", null, null, false);

		C.setA(AE);
		C.setB(C);

		F.setA(C);
		F.setB(G);

		G.setA(G);
		G.setB(AE);

		BH.setA(G);
		BH.setB(C);

		AE.setA(BH);
		AE.setB(F);

		State[] machine2 = {C, F, G, BH, AE};
		DFA N = new DFA(AE, machine2, 'a', 'b');
		return N;
	}

	public static DFA getDFA3()
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
		DFA M = new DFA(A, machine1, 'a', 'b');
		return M;

	}

	public static DFA getDFA3Minimized()
	{
		State C = new State("C", null, null, false);
		State E = new State("E", null, null, false);
		State H = new State("H", null, null, false);
		State AB = new State("AB", null, null, false);
		State FG = new State("FG", null, null, true);

		C.setA(E);
		C.setB(FG);

		E.setA(FG);
		E.setB(FG);

		H.setA(C);
		H.setB(C);

		AB.setA(H);
		AB.setB(AB);

		FG.setA(FG);
		FG.setB(FG);

		State[] machine2 = {C, E, H, AB, FG};
		DFA N = new DFA(AB, machine2, 'a', 'b');
		return N;
	}

	public static DFA getDFA4()
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
		DFA M = new DFA(A, machine1, 'a', 'b');
		return M;
	}

	public static DFA getDFA4Minimized()
	{
		State A = new State("A", null, null, true);
		State BD = new State("BD", null, null, false);
		State CE = new State("CE", null, null, true);

		A.setA(A);
		A.setB(BD);

		BD.setA(CE);
		BD.setB(BD);

		CE.setA(CE);
		CE.setB(CE);

		State[] machine2 = {A, BD, CE};
		DFA N = new DFA(A, machine2, 'a', 'b');
		return N;
	}

	public static boolean testInLanguage1()
	{
		DFA DFA1Minimized = getDFA1Minimized();
		String[] inTheLanguage = {"a", "aa", "ab", "aaa", "aab", "aab", "aba", "abb", "abba", "aaaa"};
		String[] notInLanguage = {"", "b", "ba", "bb", "baa", "bab", "bba", "bbb", "baaa", "baab"};
		boolean result = true;

		for(int i = 0; i < inTheLanguage.length; i++)
		{
			result &= DFA1Minimized.inLanguage(inTheLanguage[i], DFA1Minimized.getStart());
			result &= !(DFA1Minimized.inLanguage(notInLanguage[i], DFA1Minimized.getStart()));
		}
		return result;
	}

	public static boolean testInLanguage2()
	{
		DFA DFA2Minimized = getDFA2Minimized();
		String[] inTheLanguage = {"ab", "ba", "aabab", "abaab", "abb", "baaba", "bbabba", "aaaabab", "abbbb", "babbb"};
		String[] notInLanguage = {"", "b", "a", "aa", "bb", "bba", "bbb", "baa", "babba", "bbbb"};
		boolean result = true;

		for(int i = 0; i < inTheLanguage.length; i++)
		{
			result &= DFA2Minimized.inLanguage(inTheLanguage[i], DFA2Minimized.getStart());
			result &= !(DFA2Minimized.inLanguage(notInLanguage[i], DFA2Minimized.getStart()));
		}
		return result;
	}

	public static void main(String[] args)
	{
		System.out.println("Running DFATest....");
		System.out.println("	DFATest.testInLanguage1: " + testInLanguage1());
		System.out.println("	DFATest.testInLanguage2: " + testInLanguage2());
		System.out.println("	DFATest.testCreateDistinguishTable1: " + testCreateDistinguishTable1());
		System.out.println("	DFATest.testCreateDistinguishTable2: " + testCreateDistinguishTable2());
		System.out.println("	DFATest.testCreateDistinguishTable3: " + testCreateDistinguishTable3());		
		System.out.println("	DFATest.testCreateDistinguishTable4: " + testCreateDistinguishTable4());		
		System.out.println("	DFATest.testDFAMinimization1: " + testDFAMinimization1());
		System.out.println("	DFATest.testDFAMinimization2: " + testDFAMinimization2());
		System.out.println("	DFATest.testDFAMinimization3: " + testDFAMinimization3());
		System.out.println("	DFATest.testDFAMinimization4: " + testDFAMinimization4());
	}
}