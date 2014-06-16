FiniteAutomata
==============

Describes some functionality of DFA's and NFA's with an alphabet of size 2 (an 'a' and a 'b' transition)

DFA can do the following:
  1. Test if a string is in language described by this machine
  2. Determine if one of the states in the machine is reachable by the start state (or any other state)
  3. Can be minimized so as to become the smallest DFA neccassary to represent to language described
     by the current DFA (Note: will update the current DFA)
  -There is also a main function that I was using for testing
  -I plan on implementing a function to create a regex that represents this DFA
  
NFA can do the following:
  1. Test is a string is in the language described by this machine
  2. Create a DFA that represents the current NFA
  -Again, there is also another main function that I was using for testing
