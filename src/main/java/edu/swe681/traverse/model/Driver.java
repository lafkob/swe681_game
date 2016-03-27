package edu.swe681.traverse.model;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary driver for debugging
 */
public class Driver {

	public static void main(String[] args)
	{
		BufferedReader br;
		GameBoard board, newBoard;
		String input;
		String[] tokens;
		Point start, dest;
		List<Point> dests;
		int numDests;
		
		board = new GameBoard(false);
		
		try
		{
			br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println(board);
			System.out.println("What move, Player " + board.getCurrentState().getPlayer() + "?");
			while(!((input=br.readLine()).equals("q")))
			{
				tokens = input.split("[ ]+");
				if (tokens.length % 2 == 0 && tokens.length >= 4)
				{
					start = new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
					
					numDests = (tokens.length - 2) / 2;
					dests = new ArrayList<Point>();
					for (int i = 0; i < numDests; i++)
					{
						dest = new Point(Integer.parseInt(tokens[(i*2)+2]), 
								Integer.parseInt(tokens[(i*2)+3]));
						dests.add(dest);
					}
					
					newBoard = null;
					try
					{
						newBoard = board.movePiece(start, dests);
					}
					catch (IllegalMoveException ime)
					{
						System.out.println(ime.getMessage());
					}
					catch (InvalidMoveException ime)
					{
						System.out.println(ime.getMessage());
					}
					
					if (newBoard != null)
					{
						board = newBoard;
						System.out.println("\n" + board);
					}
				}
				else
					System.out.println("Invalid number of tokens.\n");
				
				System.out.println("What move, Player " + board.getCurrentState().getPlayer() + "?");
			}
			
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}

}
