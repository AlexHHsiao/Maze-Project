package sjsu.Xiao.cs146.project3;

import java.util.Scanner;

public class MazeTest {

	public static void main(String[] args) {
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
