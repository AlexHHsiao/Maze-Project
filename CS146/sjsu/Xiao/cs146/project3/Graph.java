package sjsu.Xiao.cs146.project3;

import java.util.ArrayList;
import java.util.Random;

/*
 * The goal of this project is to write a program that will 
 * automatically generate and solve mazes. Each time you run the program, 
 * it will generate and print a new random maze and the solution. 
 * You will use depth-first search (DFS) and breadth-first search (BFS).
 */

public class Graph {
	
	// open and close are used to indicate the wall between each cell whether close or open
	private static final boolean open = false;
	private static final boolean close = true;
	
	// queue and stack are created for BFS and DFS solution. 
	CellQueue BFSqueue = new CellQueue(); 
	CellStack DFSstack = new CellStack(); 
	
	public int size; // maze size (n * n)
	private Cell[][] cell; // 2-D array for storing Cell object in order to represent the maze
	
	/*
	 * Cell class is created to represent each cell. Each cell will be 
	 * connected with all its neighbor cells and has its wall open in order 
	 * to create the way of maze.
	 */
	
	public class Cell
	{
		Cell topCell, botCell, leftCell, rightCell; // all neighbors of a cell
		Cell parent; //show the parent of each cell for BFS solution
		boolean topWall, botWall, leftWall, rightWall; // all walls of a cell
		boolean topPass, botPass, leftPass, rightPass; // indicate whether the passage of each cell has been passed or not for DFS solution
		int DFSnum, BFSnum; // show the walk way of DFS and BFS solution
		String DFSpath, BFSpath; // show the final path of DFS and BFS solution
		boolean visited; // indicate whether the cell has been visited or not for BFS solution  
		
		/*
		 * Cell class constructor for setting the status of each created cell. In the beginning, 
		 * cell has no neighbor, no wall open, no passage passed, no parent, no solution path, 
		 * no solution walk way, and has been visited 
		 */
		
		public Cell()
		{
			topWall = close;
			botWall = close;
			rightWall = close;
			leftWall = close;
			
			topCell = null;
			botCell = null;
			leftCell = null;
			rightCell = null;
			
			parent = null;
			
			topPass = false;
			botPass	= false;
			leftPass = false;
			rightPass =false;
			
			DFSnum = -1;
			BFSnum = -1;
			visited = false;
			
			DFSpath = " ";
			BFSpath = " ";
		}
	}
	
	/*
	 * Graph class constructor for creating the maze. After getting the size of maze, 
	 * all cells will be created with their unique location (2-D array). Then each cell 
	 * will be connected with its neighbor 
	 */
	
	public Graph(int inputSize)
	{
		size = inputSize;
		
		cell = new Cell[size][size];
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				cell[i][j] = new Cell();
			}
		}
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (j != (size - 1))
				{
					cell[i][j].rightCell = cell[i][j + 1];
				}
				
				if (j != 0)
				{
					cell[i][j].leftCell = cell[i][j - 1];
				}
				
				if (j != (size - 1))
				{
					cell[j][i].botCell = cell[j + 1][i];
				}
				
				if (j != 0)
				{
					cell[j][i].topCell = cell[j - 1][i];
				}
			}
		}
	}
	
	/*
	 * return true if this cell has all neighbors' wall intact, return false
	 * otherwise. For maze generator
	 */
	
	private boolean neighborIsIntact(Cell cell)
	{
		if (cell.rightCell != null)
		{
			if (cell.rightCell.rightWall == close 
					&& cell.rightCell.leftWall == close
					&& cell.rightCell.topWall == close
					&& cell.rightCell.botWall == close)
				return true;
		}
		
		if (cell.leftCell != null)
		{
			if (cell.leftCell.rightWall == close 
					&& cell.leftCell.leftWall == close
					&& cell.leftCell.topWall == close
					&& cell.leftCell.botWall == close)
				return true;
		}
		
		if (cell.topCell != null)
		{
			if (cell.topCell.rightWall == close 
					&& cell.topCell.leftWall == close
					&& cell.topCell.topWall == close
					&& cell.topCell.botWall == close)
				return true;
		}
		
		if (cell.botCell != null)
		{
			if (cell.botCell.rightWall == close 
					&& cell.botCell.leftWall == close
					&& cell.botCell.topWall == close
					&& cell.botCell.botWall == close)
				return true;
		}

		return false;
	}
	
	/*
	 * This function uses four if statements to check each neighbor of this 
	 * cell. If any neighbor has all wall intact, the direction of this neighbor 
	 * will be stored into array (string). In the end, randomly return a neighbor for 
	 * maze generator.
	 */
	
	private String randomNeighborCell(Cell cell)
	{
		String[] randomNeighborCell = new String[4];
		int counter = 0;
		
		if (cell.botCell != null)
		{
			if (cell.botCell.rightWall == close 
					&& cell.botCell.leftWall == close
					&& cell.botCell.topWall == close
					&& cell.botCell.botWall == close)
			{
				randomNeighborCell[counter] = "bot";
				counter++;
			}
		}
		
		if (cell.rightCell != null)
		{
			if (cell.rightCell.rightWall == close 
					&& cell.rightCell.leftWall == close
					&& cell.rightCell.topWall == close
					&& cell.rightCell.botWall == close)
			{
				randomNeighborCell[counter] = "right";
				counter++;
			}
		}
		
		if (cell.topCell != null)
		{
			if (cell.topCell.rightWall == close 
					&& cell.topCell.leftWall == close
					&& cell.topCell.topWall == close
					&& cell.topCell.botWall == close)
			{
				randomNeighborCell[counter] = "top";
				counter++;
			}
		}
		
		if (cell.leftCell != null)
		{
			if (cell.leftCell.rightWall == close 
					&& cell.leftCell.leftWall == close
					&& cell.leftCell.topWall == close
					&& cell.leftCell.botWall == close)
			{
				randomNeighborCell[counter] = "left";
				counter++;
			}		
		}
		
		return randomNeighborCell[randomNum(counter)];
	}
	
	/*
	 * return true if this cell has a way out that hasn't been passed, 
	 * return false otherwise. for DFS solution
	 */
	
	private boolean anyWayOut(Cell cell)
	{
		if (cell.leftWall == open && cell.leftPass == false)
			return true;
		
		if (cell.rightWall == open && cell.rightPass == false)
			return true;
		
		if (cell.topWall == open && cell.topPass == false)
			return true;
		
		if (cell.botWall == open && cell.botPass == false)
			return true;
			
		return false;
	}
	
	/*
	 * The function uses four if statements to check if this cell has any way 
	 * out that hasn't been passed. The direction will be stored into array (String). 
	 * In the end, randomly return a direction from the array for DFS solution.
	 */
	
	private String randomNeighborDFS(Cell cell)
	{
		String[] randomNeighborDFS = new String[4];
		int counter = 0;
		
		if (cell.leftWall == open && cell.leftPass == false)
		{
			randomNeighborDFS[counter] = "left";
			counter++;
		}
		
		if (cell.rightWall == open && cell.rightPass == false)
		{
			randomNeighborDFS[counter] = "right";
			counter++;
		}
		
		if (cell.topWall == open && cell.topPass == false)
		{
			randomNeighborDFS[counter] = "top";
			counter++;
		}
		
		if (cell.botWall == open && cell.botPass == false)
		{
			randomNeighborDFS[counter] = "bot";
			counter++;
		}
			
		return randomNeighborDFS[randomNum(counter)];
	}
	
	/*
	 * The function uses four if statements to check all neighbors that this 
	 * cell can reach. Store any neighbor that hasn't been visited into array (String) 
	 * and return this array for BFS solution.
	 */
	
	private ArrayList<String> neighborBFS(Cell cell)
	{
		ArrayList<String> neighborBFS = new ArrayList<String>();
		
		if (cell.rightWall == open)
			if (cell.rightCell.visited == false)
				neighborBFS.add("right");
		
		if (cell.leftWall == open)
			if (cell.leftCell.visited == false)
				neighborBFS.add("left");
		
		if (cell.topWall == open)
			if (cell.topCell.visited == false)
				neighborBFS.add("top");
		
		if (cell.botWall == open) 
			if (cell.botCell.visited == false)
				neighborBFS.add("bot");
		
		return neighborBFS;
	}
	
	// store the maze into a string
	
	public String printMaze()
	{
		String maze = "\n" + "MAZE" + "\n";
		maze += "+ ";
		
		for (int i = 1; i < size ; i++)
			maze += "+-";
		
		maze += "+" + "\n|";
		
		for (int i = 0; i < size - 1; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].rightWall)
					maze += " |";
				else
					maze += "  ";
			}
			
			maze += "\n+";
			
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].botWall)
					maze += "-+";
				else
					maze += " +";
			}
			
			maze += "\n|";
		}
		
		for (int i = 0; i < size; i++)
		{
			if (cell[size - 1][i].rightWall)
				maze += " |";
			else
				maze += "  ";
		}
		
		maze += "\n+";
		
		for (int i = 0; i < size - 1; i++)
			maze += "-+";
		
		maze += " +";
			
		return maze + "\n";
	}
	
	// store the DFS walk way and solution path into a string
	
	public String printDFS()
	{
		String maze = "\n" + "DFS" + "\n";
		maze += "+ ";
		
		for (int i = 1; i < size ; i++)
			maze += "+-";
		
		maze += "+" + "\n|";
		
		for (int i = 0; i < size - 1; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].DFSnum != -1)
					maze += Integer.toString(cell[i][j].DFSnum);
				else 
					maze += " ";
				
				if (cell[i][j].rightWall)
					maze += "|";
				else
					maze += " ";
			}
			
			maze += "\n+";
			
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].botWall)
					maze += "-+";
				else
					maze += " +";
			}
			
			maze += "\n|";
		}
		
		for (int i = 0; i < size; i++)
		{
			if (cell[size - 1][i].DFSnum != -1)
				maze += Integer.toString(cell[size - 1][i].DFSnum);
			else 
				maze += " ";
			
			if (cell[size - 1][i].rightWall)
				maze += "|";
			else
				maze += " ";
		}
		
		maze += "\n+";
		
		for (int i = 0; i < size - 1; i++)
			maze += "-+";
		
		maze += " +" + "\n";
		
		maze += "\n" + "+ ";
		
		for (int i = 1; i < size ; i++)
			maze += "+-";
		
		maze += "+" + "\n|";
		
		for (int i = 0; i < size - 1; i++)
		{
			for (int j = 0; j < size; j++)
			{
				maze += cell[i][j].DFSpath;
				
				if (cell[i][j].rightWall)
					maze += "|";
				else
					maze += " ";
			}
			
			maze += "\n+";
			
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].botWall)
					maze += "-+";
				else
					maze += " +";
			}
			
			maze += "\n|";
		}
		
		for (int i = 0; i < size; i++)
		{
			maze += cell[size - 1][i].DFSpath;
			
			if (cell[size - 1][i].rightWall)
				maze += "|";
			else
				maze += " ";
		}
		
		maze += "\n+";
		
		for (int i = 0; i < size - 1; i++)
			maze += "-+";
		
		maze += " +";
			
		return maze + "\n";
	}
	
	// store the BFS walk way and solution path into a string
	
	public String printBFS()
	{
		String maze = "\n" + "BFS" + "\n";
		maze += "+ ";
		
		for (int i = 1; i < size ; i++)
			maze += "+-";
		
		maze += "+" + "\n|";
		
		for (int i = 0; i < size - 1; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].BFSnum != -1)
					maze += Integer.toString(cell[i][j].BFSnum);
				else 
					maze += " ";
				
				if (cell[i][j].rightWall)
					maze += "|";
				else
					maze += " ";
			}
			
			maze += "\n+";
			
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].botWall)
					maze += "-+";
				else
					maze += " +";
			}
			
			maze += "\n|";
		}
		
		for (int i = 0; i < size; i++)
		{
			if (cell[size - 1][i].BFSnum != -1)
				maze += Integer.toString(cell[size - 1][i].BFSnum);
			else 
				maze += " ";
			
			if (cell[size - 1][i].rightWall)
				maze += "|";
			else
				maze += " ";
		}
		
		maze += "\n+";
		
		for (int i = 0; i < size - 1; i++)
			maze += "-+";
		
		maze += " +" + "\n";
		
		maze += "\n" + "+ ";
		
		for (int i = 1; i < size ; i++)
			maze += "+-";
		
		maze += "+" + "\n|";
		
		for (int i = 0; i < size - 1; i++)
		{
			for (int j = 0; j < size; j++)
			{
				maze += cell[i][j].BFSpath;
				
				if (cell[i][j].rightWall)
					maze += "|";
				else
					maze += " ";
			}
			
			maze += "\n+";
			
			for (int j = 0; j < size; j++)
			{
				if (cell[i][j].botWall)
					maze += "-+";
				else
					maze += " +";
			}
			
			maze += "\n|";
		}
		
		for (int i = 0; i < size; i++)
		{
			maze += cell[size - 1][i].BFSpath;
			
			if (cell[size - 1][i].rightWall)
				maze += "|";
			else
				maze += " ";
		}
		
		maze += "\n+";
		
		for (int i = 0; i < size - 1; i++)
			maze += "-+";
		
		maze += " +";
			
		return maze + "\n";
	}
	
	// for generate random number within a range
	
	private int randomNum(int range)
	{
		Random random = new Random();
		int randNum = random.nextInt() % range;
		
		if (randNum < 0)
			randNum = 0 - randNum;
		
		return randNum;
	}
	
	/* maze generator function uses a while loop to generate the maze. As long as all cells have 
	 * been visited, the while loop will stop. If while loop, the location of current cell will 
	 * be push into stack, and all neighbors of current cell will be checked. If any neighbor's walls 
	 * are all intact, randomly set a neighbor that has all wall intact to current cell, and the number 
	 * of visited cell plus one. Otherwise pop a cell from stack, and set it to be the current wall. 
	 */

	public void mazeGenerator()
	{
		CellStack stack = new CellStack(); 
		
		int totalCell = size * size; // total size = size * size
		int visitedCell = 1; // visited cell number
		int currentCellRow = 0; // current cell row index
		int currentCellCol = 0; // current cell column index
		String randomCell; // use to catch the next neighbor current cell will go
		
		while (visitedCell < totalCell)
		{
			if (neighborIsIntact(cell[currentCellRow][currentCellCol]))
			{
				stack.push(currentCellRow, currentCellCol);
				randomCell = randomNeighborCell(
						cell[currentCellRow][currentCellCol]);
				
				if (randomCell == "right")
				{
					cell[currentCellRow][currentCellCol].rightWall = open;
					cell[currentCellRow][currentCellCol + 1].leftWall = open;
					currentCellCol++;
				}
				else if (randomCell == "left")
				{
					cell[currentCellRow][currentCellCol].leftWall = open;
					cell[currentCellRow][currentCellCol - 1].rightWall = open;
					currentCellCol--;
				}
				else if (randomCell == "top")
				{
					cell[currentCellRow][currentCellCol].topWall = open;
					cell[currentCellRow - 1][currentCellCol].botWall = open;
					currentCellRow--;
				}
				else if (randomCell == "bot")
				{
					cell[currentCellRow][currentCellCol].botWall = open;
					cell[currentCellRow + 1][currentCellCol].topWall = open;
					currentCellRow++;
				}
				
				visitedCell++;
			}
			else
			{
				stack.pop();
				currentCellRow = stack.getRow();
				currentCellCol = stack.getCol();
			}
		}
	}
	
	/*
	 * DFS function will look for maze solution. First set the left top cell to be the 
	 * current cell, and check all cells. Once the right bottom cell is reached, which means 
	 * the ending of maze is reached, while loop stop. In first, current will be check if it has 
	 * any way out that hasn't been visited. If yes, push current cell into stack, randomly pick a way 
	 * to go, and set the neighbor that been chosen as the current cell, if not, pop a cell from stack
	 * and set it to be the current cell.  
	 */
	
	public void DFS()
	{
		int counter = 0; // display the DFS solution walk way
		int currentCellRow = 0; // current cell row index
		int currentCellCol = 0; // current cell column index
		String randomCell; // use to catch the next neighbor current cell will go
		
		while ((currentCellRow != (size - 1)) || (currentCellCol != (size - 1)))
		{
			if (anyWayOut(cell[currentCellRow][currentCellCol]))
			{
				DFSstack.push(currentCellRow, currentCellCol);
				randomCell = randomNeighborDFS(
						cell[currentCellRow][currentCellCol]);
				
				if (cell[currentCellRow][currentCellCol].DFSnum == -1)
					cell[currentCellRow][currentCellCol].DFSnum = counter;
				
				if (randomCell == "right")
				{
					cell[currentCellRow][currentCellCol].rightPass = true;
					cell[currentCellRow][currentCellCol + 1].leftPass = true;
					currentCellCol++;
				}
				else if (randomCell == "left")
				{
					cell[currentCellRow][currentCellCol].leftPass = true;
					cell[currentCellRow][currentCellCol - 1].rightPass= true;
					currentCellCol--;
				}
				else if (randomCell == "top")
				{
					cell[currentCellRow][currentCellCol].topPass = true;
					cell[currentCellRow - 1][currentCellCol].botPass = true;
					currentCellRow--;
				}
				else if (randomCell == "bot")
				{
					cell[currentCellRow][currentCellCol].botPass = true;
					cell[currentCellRow + 1][currentCellCol].topPass = true;
					currentCellRow++;
				}
				
				counter++;
			}
			else
			{
				if (cell[currentCellRow][currentCellCol].DFSnum == -1)
					cell[currentCellRow][currentCellCol].DFSnum = counter;
				
				DFSstack.pop();
				currentCellRow = DFSstack.getRow();
				currentCellCol = DFSstack.getCol();
			}
			
			if (counter == 10)
				counter = 0;
		}
		
		DFSstack.push(currentCellRow, currentCellCol);
		cell[currentCellRow][currentCellCol].DFSnum = counter;
		
		DFSpath();
	}
	
	/*
	 * After find the walk way of DFS solution, whatever left in the stack 
	 * is the final path of DFS solution. Pop every cell from stack, and set 
	 * them to be the final path of DFS solution.
	 */
	
	private void DFSpath()
	{
		int col, row;
		
		while (!DFSstack.isEmpty())
		{
			DFSstack.pop();
			row = DFSstack.getRow();
			col = DFSstack.getCol();
			
			cell[row][col].DFSpath = "#";
		}
	}
	
	/*
	 * BSF function will look for maze solution. First set the left top cell to be the 
	 * current cell, and check all cells. Once the right bottom cell is reached, which means 
	 * the ending of maze is reached, while loop stop. Within the while loop, every neighbor 
	 * this cell that hasn't been visited will be enqueue into queue, and set them all to be 
	 * visited. Then dequeue a cell from queue, and set it to be the current cell. 
	 */
	
	public void BFS()
	{	
		int counter = 0;
		int currentCellRow = 0; // current cell row index
		int currentCellCol = 0; // current cell column index
		String whichCell; // use to catch all neighbors that this cell can reach which haven't been visited
		
		cell[0][0].visited = true;
		ArrayList<String> neighborCell = new ArrayList<String>();
		
		while ((currentCellRow != (size - 1)) || (currentCellCol != (size - 1)))
		{
			neighborCell = neighborBFS(cell[currentCellRow][currentCellCol]);
			
			if (cell[currentCellRow][currentCellCol].BFSnum == -1)
				cell[currentCellRow][currentCellCol].BFSnum = counter;
			
			
			for (int i = 0; i < neighborCell.size(); i++)
			{
				whichCell = neighborCell.get(i);
				
				if (whichCell == "right")
				{
					BFSqueue.enqueue(currentCellRow, currentCellCol + 1);
					cell[currentCellRow][currentCellCol + 1].visited = true;
					cell[currentCellRow][currentCellCol + 1].parent = cell[currentCellRow][currentCellCol];
				}
				else if (whichCell == "left")
				{
					BFSqueue.enqueue(currentCellRow, currentCellCol - 1);
					cell[currentCellRow][currentCellCol - 1].visited = true;
					cell[currentCellRow][currentCellCol - 1].parent = cell[currentCellRow][currentCellCol];
				}
				else if (whichCell == "top")
				{
					BFSqueue.enqueue(currentCellRow - 1, currentCellCol);
					cell[currentCellRow - 1][currentCellCol].visited = true;
					cell[currentCellRow - 1][currentCellCol].parent = cell[currentCellRow][currentCellCol];
				}
				else if (whichCell == "bot")
				{
					BFSqueue.enqueue(currentCellRow + 1, currentCellCol);
					cell[currentCellRow + 1][currentCellCol].visited = true;
					cell[currentCellRow + 1][currentCellCol ].parent = cell[currentCellRow][currentCellCol];
				}
			}
			
			BFSqueue.dequeue();
			currentCellRow = BFSqueue.getRow();
			currentCellCol = BFSqueue.getCol();
			
			counter++;
			
			if (counter == 10)
				counter = 0;
		}
		
		cell[currentCellRow][currentCellCol].BFSnum = counter;
		
		BFSpath();
	}
	
	/*
	 * Backtrack the parent from the right bottom cell, which is the ending of maze all 
	 * the way until the left top maze, which is the starting of maze. The final path of 
	 * BFS solution will be consisted by all cells that have been visited.
	 */
	
	private void BFSpath()
	{	
		Cell currentCell = new Cell();
		currentCell = cell[size - 1][size - 1];
		
		while (currentCell != cell[0][0])
		{
			currentCell.BFSpath = "#";
			currentCell = currentCell.parent;
		}
		
		cell[0][0].BFSpath = "#";
	}
}