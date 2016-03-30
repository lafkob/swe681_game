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
		
		board = new GameBoard(false);
		
		try
		{
			br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println(board);
			System.out.println("What move, Player " + board.getCurrentState().getPlayer() + "?");
			while(!((input=br.readLine()).equals("q")))
			{
				newBoard = null;
				try
				{
					newBoard = parseAndMakeMove(board, input);
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
				
				System.out.println("What move, Player " + board.getCurrentState().getPlayer() + "?");
			}
			
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
	
	private static GameBoard parseAndMakeMove(GameBoard board, String moveStr)
			throws IllegalMoveException, InvalidMoveException
		{
			String[] tokens, subTokens;
			int pieceID;
			Point start;
			List<Point> dests;
			
			tokens = moveStr.split(",");
			
			pieceID = Integer.parseInt(tokens[0]);
			
			subTokens = tokens[1].split(" ");
			start = new Point(Integer.parseInt(subTokens[0]), Integer.parseInt(subTokens[1]));
			
			dests = new ArrayList<Point>();
			if (tokens.length > 2)
			{
				subTokens = tokens[2].split(" ");
				for (int i = 0; i < subTokens.length; i=i+2)
				{
					dests.add(new Point(Integer.parseInt(subTokens[i]), Integer.parseInt(subTokens[i+1])));
				}
			}
			
			return board.movePiece(pieceID, start, dests);		
		}
}
