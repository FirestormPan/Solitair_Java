# Solitaire with DFS Solver

Implemented in Java for a university assignment.

This project recreates the logic of the classic Solitaire card game in a Command Line Interface (CLI) environment. The focus is entirely on the game mechanics and internal logic, 
without a graphical user interface.

Each execution generates a random initial game state. The user interacts with the game by entering CLI commands to move cards between stacks, while the program validates and enforces legal Solitaire moves.

The card stacks (tableau, foundation, stock, etc.) are modeled using appropriate data structures from the Java Collections Framework, selected according to the required behavior of each structure.

Additionally, the project implements an uninformed search solver using Depth-First Search (DFS) with recursive backtracking. The algorithm constructs a search tree where each node represents 
a possible game state produced by a card move. It explores these states until a solution (winning configuration) is found. However, the solver is memory-inefficient, as it stores complete game states
while exploring the search tree. As a result, solving arbitrary random games can exceed the memory limits of a typical PC.

Concepts Demonstrated
- Object-Oriented Design
- Game State Modeling
- Tree Search Algorithms (DFS with Backtracking)
- Java Collections Framework
