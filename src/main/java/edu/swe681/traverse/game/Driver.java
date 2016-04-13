package edu.swe681.traverse.game;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.swe681.traverse.game.exception.*;

/**
 * Temporary driver for debugging
 */
public class Driver {

	public static void main(String[] args)
	{
		BufferedReader br;
		GameBoard board, newBoard;
		String input;
		
		board = new GameBoard(10, true);
		board.registerPlayer(20);
		board.registerPlayer(30);
		
		try
		{
			br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println(board);
			System.out.println("What move, Player " + board.getGameState().getCurrentPlayerID() + "?");
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
				catch (TraverseException te)
				{
					System.out.println(te.getMessage());
				}
				
				if (newBoard != null)
				{
					board = newBoard;
					System.out.println("\n" + board);
				}
				
				System.out.println("What move, Player " + board.getGameState().getCurrentPlayerID() + "?");
			}
			
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
	
	private static GameBoard parseAndMakeMove(GameBoard board, String moveStr)
			throws TraverseException
		{
			String[] tokens, subTokens;
			int pieceID;
			List<Point> dests;
			
			tokens = moveStr.split(",");
			
			pieceID = Integer.parseInt(tokens[0]);
			
			dests = new ArrayList<Point>();
			if (tokens.length > 1)
			{
				subTokens = tokens[1].split(" ");
				for (int i = 0; i < subTokens.length; i=i+2)
				{
					dests.add(new Point(Integer.parseInt(subTokens[i]), Integer.parseInt(subTokens[i+1])));
				}
			}
			
			return board.movePiece(pieceID, dests);		
		}
}
