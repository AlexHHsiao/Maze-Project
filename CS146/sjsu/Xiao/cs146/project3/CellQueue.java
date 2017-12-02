package sjsu.Xiao.cs146.project3;

/*
 * CellQueue (FIFO) will be used for maze BFS solution.
 * Cell location with row and column will be store into it, and location
 * will be return after dequeue.
 */

public class CellQueue {

	private int dequeueRow, dequeueCol;
	
	public class Cell
	{
		int row;
		int col;
		
		Cell next;
	}
	
	Cell front, rear;
	int numCells;
	
	public CellQueue()
	{
		front = null;
		rear = null;
		numCells = 0;
	}
	
	public void enqueue(int row, int col)
	{
		Cell cell = new Cell();
		
		cell.row = row;
		cell.col = col;
		cell.next = null;
		
		if (isEmpty())
		{
			front = cell;
			rear = cell;
		}
		else
		{
			rear.next = cell;
			rear = cell;
		}
		
		numCells++;
	}
	
	public void dequeue()
	{
		Cell cell = new Cell();
		
		if (isEmpty())
			System.out.println("The queue is empty.");
		else
		{
			dequeueRow = front.row;
			dequeueCol = front.col;
			
			cell = front;
			front = front.next;
			
			numCells--;
		}
	}
	
	public boolean isEmpty()
	{
		if (numCells > 0)
			return false;
		else
			return true;
	}
	
	public int getRow()
	{
		return dequeueRow;
	}
	
	public int getCol()
	{
		return dequeueCol;
	}
}
