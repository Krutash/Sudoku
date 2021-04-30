/***************************
 * @author UTKARSH KUMAR
 * ID: 2017B2A71008P
 ***************************/

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sudoku {
	
	public static void main(String[] args) throws FileNotFoundException
	{
		int[][] GRID =  { { 0,1,4,0,0,0,0,0,5 }, { 0,0,0,0,0,4,0,0,8}, {9,0,0,5,2,0,0,1,4},
				{ 0,0,2,6,9,0,5,0,0}, {0,0,0,0,0,0,0,0,0 }, {0,0,6,0,8,5,2,0,0 },
				{ 8,9,0,0,7,3,0,0,2 }, {2,0,0,9,0,0,0,0,0}, { 5,0,0,0,0,0,8,7,0 } };
		
		Scanner in = new Scanner(System.in);
		System.out.println("Choose of the following options:\n1. Solve default GRID\n2. Provide a text file");
		System.out.print("Your Choice (1/2): ");
		int choice = in.nextInt();
		if(choice == 2)
		{
			System.out.print("\nFile Name (for example: sudoku.txt): ");
			String filename = in.next();
			
			FileReader fr = new FileReader(filename); 
			Scanner inFile = new Scanner(fr);
			
			for(int i = 0; i<9; i++)
			{
				for(int j = 0; j<9; j++)
				{
					GRID[i][j] = inFile.nextInt();
				}
			}
			inFile.close();
		}
		
		in.close();
		new Solve(GRID);
	}
	
}

class Pos
{
	int ln;
	int col;
	Pos(int ln, int col)
	{
		this.ln=ln;
		this.col = col;
	}
}
class Solve {
	private int[][] GRID = new int[9][9];
	int[][][] Domain = new int[9][9][10];
	int assignmentNumber = 0;
	long BacktrackCount = 0;
	public void InitializeDomain()
	{
		for(int i = 0; i< 9; i++)
			for(int j = 0; j< 9; j++)
				Domain[i][j][0] = 27;
		for(int i = 0; i< 9; i++)
		{
			for(int j = 0; j<9; j++)
			{
				if(GRID[i][j] != 0)
				{
					Pos p = new Pos(i, j);
					int val = GRID[i][j];
					markPlace(p, val, -1);
				}
			}
		}

		
	}
	
	Solve(int[][] GRID)
	{
		this.GRID = GRID;
		InitializeDomain();
		System.out.println("Given GRID:");
		for(int i = 0; i< 9; i++)
 		{
 			for(int j = 0; j<9; j++)
 			{
 				System.out.print(GRID[i][j]+" ");
 			}
 			System.out.println();
 		}
		long startTime = System.currentTimeMillis();
		boolean done = backtrack();
		long totalTime = System.currentTimeMillis() - startTime;
		
		
		if(done)
			{System.out.println("FINAL GRID:");
			for(int i = 0; i< 9; i++)
	 		{
	 			for(int j = 0; j<9; j++)
	 			{
	 				System.out.print(GRID[i][j]+" ");
	 			}
	 			System.out.println();
	 		}}
		else
			System.out.println("No Solution");
    	 
    	 System.out.println("Performance Stats:\nTime Taken: "+ totalTime+ " ms\nBacktracks: "+BacktrackCount);
    	 
    	 System.exit(1);
		
	}
	
	
	private void markPlace(Pos p, int val, int mark)
	{
		//mark row peers
		int regR = p.ln-p.ln%3;
		int regC = p.col-p.col%3;
		for(int i=0; i<9; i++)
		{
			if(p.col - i != 0 )
			{
				Domain[p.ln][i][val] += mark;
				Domain[p.ln][i][0] += mark;
			}
			if(p.ln - i != 0 )
			{
				Domain[i][p.col][val] += mark;
				Domain[i][p.col][0] += mark;
			}
			if((regR+i/3)-p.ln != 0 && (regC+i%3)-p.col != 0 )
			{
				Domain[regR+i/3][regC+i%3][val] += mark;
				Domain[regR+i/3][regC+i%3][0] += mark;
			}
		}
		
	}
	private boolean ForwardChecking(Pos p, int val)
	{
		
		if (Domain[p.ln][p.col][val] == 0) 
        {    
			//GRID[p.ln][p.col] = val;
        	markPlace(p, val, -1);
        	
            if (isSolutionPossible(p, val)) 
            {
            	GRID[p.ln][p.col] = val;
            	// System.out.println("assigned "+val);
            	return true;
            }
        }
		return false;
	}
	
	 private boolean isSolutionPossible(Pos p, int val)
	 {
		 
		int regR = p.ln-p.ln%3;
		int regC = p.col-p.col%3;
		//boolean possible = true;
		for(int i=0; i<9; i++)
		{
			if(Domain[p.ln][i][0] == 0 || Domain[i][p.col][0] == 0 || Domain[regR+i/3][regC+i%3][0] == 0)
			{
				markPlace(p, val, 1);
				return false;
			}
			
			if((p.col - i != 0 && Domain[p.ln][i][0] == 1 && Domain[p.ln][i][val] == 0) || 
					(p.ln - i != 0 && Domain[i][p.col][i] == 1 && Domain[i][p.col][val] == 0) ||
					(((regR+i/3)-p.ln != 0 && (regC+i%3)-p.col != 0) && Domain[regR+i/3][regC+i%3][0] == 1 && 
					Domain[regR+i/3][regC+i%3][val] == 0 ))
			{
				markPlace(p, val, 1);
				return false;
			}
			
		}
		return true;
	 }
	 

	private boolean backtrack()
	 {
	     Pos p = SelectUnassignedVariable();
	     
	     if(p == null)
	     {
	    	 return true;
	     }
	     
	     int[] domainRetain = new int[10];
	     
	     for(int i = 0; i< 10; i++)
	     {
	    	 domainRetain[i] = Domain[p.ln][p.col][i];
	     }

	     while (domainRetain[0] > 0)
	     {
	         int val = SelectDomainValue(p, domainRetain);
	         if(val == 0)
	        	 return false;
	         boolean assigned = ForwardChecking(p, val);
	         if(assigned)
	         {
	        	boolean done = backtrack();
	        	if(done) return done;
	        	else {
	        		GRID[p.ln][p.col] = 0;
	        		markPlace(p, val, 1);
	        	}
	         }
	         
	         BacktrackCount++;
	         domainRetain[val] += -1;
	         domainRetain[0] += -1;
	     }
	     return false;
	 }
	 
	 private Pos SelectUnassignedVariable()
	 {
		 List<Pos> VariableList = MRVList();
		 Pos p = MaxDegreeV(VariableList);
		 
		 return p;
	 }

	 List<Pos> MRVList()
	 {
	     int min = 27 + 1;
	     ArrayList<Pos> list = new ArrayList<Pos>(); 
	     for (int i = 0; i < 9; i++)
	     {
	         for (int j = 0; j < 9; j++)
	         {
	             if (((GRID[i][j] == 0)) && (Domain[i][j][0] == min))
	             {
	                 list.add(new Pos(i, j));
	                 continue;
	             }
	             if (((GRID[i][j] == 0)) && (Domain[i][j][0] < min))
	             {
	                 list.clear();
	                 min = Domain[i][j][0];
	                 list.add(new Pos(i, j)); 
	             }

	         }
	     }
	     return list;
	 }
	 
	 Pos MaxDegreeV(List<Pos> MRVS)
	 {
	     int deg = -1;
	     Pos p = null;
	     for (int i = 0; i < MRVS.size(); i++)
	     {
	        Pos t = MRVS.get(i);
	    	int count = 0; 
	        int regR = t.ln-t.ln%3;
	 		int regC = t.col-t.col%3;
	 		
	 		for(int j=0; j<9; j++)
	 		{
	 			if(t.col - j != 0 && GRID[t.ln][j] == 0)
	 			{
	 				count++;
	 			}
	 			if(t.ln - j != 0 && GRID[j][t.col] == 0)
	 			{
	 				count++;
	 			}
	 			if(((regR+j/3)-t.ln != 0 && (regC+j%3)-t.col != 0)&& GRID[regR+j/3][regC+j%3] == 0)
	 			{
	 				count++;
	 			}
	 		}
	         if (count > deg)
	         {
	             deg = count;
	             p = MRVS.get(i); 
	         }
	     }
	     return p;
	 }
	 
	 private int SelectDomainValue(Pos t, int[] domainRetain)
	 {
		 int val = 0;
		 int comp = 28;
		 for(int i = 1; i< 10; i++)
		 {
			 int count = 0; 
		     int regR = t.ln-t.ln%3;
		     int regC = t.col-t.col%3;
			 if(domainRetain[i] == 0)
			 {
				
				for(int j=0; j<9; j++)
		 		{
		 			if(t.col - j != 0 && Domain[t.ln][j][i] == 0)
		 			{
		 				count++;
		 			}
		 			if(t.ln - j != 0 && Domain[j][t.col][i] == 0)
		 			{
		 				count++;
		 			}
		 			if(((regR+j/3)-t.ln != 0 && (regC+j%3)-t.col != 0)&& Domain[regR+j/3][regC+j%3][i] == 0)
		 			{
		 				count++;
		 			}
		 		}
				if(count<comp)
				{
					 val = i;
					 comp = count;
				}
			 }
		 }
		 return val;
	 }
	
}
