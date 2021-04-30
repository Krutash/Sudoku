# SUDOKU AS CSP
Algo used: Backtracking, optimized with Forward Checking, MRV, Max-Degree and LCV heuristics

# MODEL
To define Sudoku as CSP, we define layout first. It is a 9x9 grid sudoku. Rows are labelled as A, B, C....... I (9 rows) and columns are labelled as numbers 1, 2, 3......9. Hence variables are defined as X = {A1, A2…...Xi..... I8, I9}. A1 represents cell in Ath row and 1st column. Each variable can take up value from 1 to 9 hence Domain for each variable is D = {1, 2, 3, 4, 5, 6, 7, 8, 9}.

# JAVA IMPLEMENTATION
Here as well we make use of constraint propagation using forward checking and backtracking. Unlike N-Queen problem where we wanted to get all the solutions, in solving Sudoku, idea is to optimize our approach to solution to save computation. We assume that there is only solution to a particular sudoku grid provided.


# DATA STRUCTURES
We maintain our sudoku grid in a 2D 9x9 array called “GRID”. We refer to each cell using an object called Pos. This records row and column number for referring to a cell. We maintain domain members for each of 81 variables in a 3D 9x9x10 array called “Domain”. The third dimension of this array maintains the record of possible domain values for a particular cell. The first value in the third dimension of this array records the domain size. It is initialized to 27. For each cell domain size must be at least 1 for successful assignment of a value. The rest of the 9 cells in third dimension record availability of ith number (1 to 9) for assignment. This value must be zero if assignment is to be made. When we assign a value to a cell, we update domain for the rest of constraint cells in “Domain” array. To count time taken, and total backtracks performed, we define variables “totalTime” and “BacktrackCount”.

# METHODS DEFINED AND OPTIMIZATIONS
After initialization of Data structures and variables, we first update our domain array according to initial configuration of provided grid. We then call “backtrack” method. In every backtrack call we perform following operations and optimizations:

a.	We first select an unassigned cell from the “GRID”. To select a cell we call “SelectUnassignedVariable” method. This method uses minimum-remaining-values heuristics to select unassigned cell by calling “MRVList” method. “MRVList” returns a list of all those unassigned cells having least domain size. Then this list is passed to another method called “MaxDegreeV” to break tie. “MaxDegreeV” uses maximum-degree heuristics gives the cell which has the greatest number of unassigned cells in its rows, column and square region combined.

b.	We then select a domain value for assignment. We call “SelectDomainValue” method. This method uses Least-Constraint-Value heuristics to select value which least appears in cell’s row, column, and square region. 

c.	We now perform forward check by calling “ForwardChecking” to see if it is safe to assign the obtained value to unassigned cell.

1.	We update Domain for each of constraint cells in row, column, and square region of cell. We do this by calling “markPlace” method and passing -1 as an argument.
2.	We check whether any of the update cells have zero domain size or domain size is 1 but for the same value as the current cell’s assigning value. We do this by calling “isSolutionPossible” method and on failure we revert the updates done on domain by calling “markPlace” method again and passing 1 as an argument.

d.	On successful assignment we backtrack again. Backtracking stops when either there are no more unassigned variables or there are no more possibilities of assignment of domain values.

# FINAL RESULTS AND INPUTS
Result is displayed on the screen. On success the code outputs the initial GRID and the FINAL GRID of solution. It also displays various performance statistics as well. 
There are two ways of providing input to the code. Either changing the default grid in the code which is defined as GRID at line 16 or by providing a text file containing the sudoku grid at run time. 
