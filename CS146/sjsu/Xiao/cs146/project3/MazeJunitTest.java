package sjsu.Xiao.cs146.project3;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

public class MazeJunitTest {

	@Test
	public void test() {
		int size;

		Scanner reader = new Scanner(System.in);  
		System.out.print("Enter Size of Graph: ");
		size = reader.nextInt(); 
		
		Graph maze = new Graph(size);
		maze.mazeGenerator();
		maze.DFS();
		maze.BFS();
		
		System.out.println("Graph Size: " + maze.size);
		System.out.println(maze.printMaze());
		System.out.println(maze.printDFS());
		System.out.println(maze.printBFS());
		
		System.out.println("============================");
		System.out.println("     Program Completed!");
		System.out.println("============================");
	}

}
