package sjsu.Xiao.cs146.project3;

/*
 * CellStack (FILO) will be used for maze generator and DFS solution.
 * Cell location with row and column will be store into it, and location
 * will be return after pop.
 */

public class CellStack {

	private int popRow, popCol;
	
	public class Cell
	{
		int row;
		int col;
		Cell prev;
	}
	
	Cell stackPtr;
	
	public CellStack()
	{
		stackPtr = null;
	}
	
	public void push(int row, int col)
	{
		Cell cell = new Cell();
		
		cell.row= row;
		cell.col = col;
		
		if (stackPtr == null)
		{
			stackPtr = cell;
			stackPtr.prev = null;
		}
		else
		{
			cell.prev = stackPtr;
			stackPtr = cell;
		}
	}
	
	public void pop()
	{	
		if (stackPtr == null)
			System.out.println("There is nothing in the stack");
		else
		{
			Cell cell = stackPtr;
			
			popRow = cell.row;
			popCol = cell.col;
			stackPtr = stackPtr.prev;
			cell.prev = null;
		}
	}
	
	public int getRow()
	{
		return popRow;
	}
	
	public int getCol()
	{
		return popCol;
	}
	
	public boolean isEmpty()
	{
		if (stackPtr == null)
			return true;
		else
			return false;
	}
}
