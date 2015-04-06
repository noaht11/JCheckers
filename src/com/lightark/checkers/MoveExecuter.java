package com.lightark.checkers;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class MoveExecuter implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final static int MOVE_WAS_NORMAL = 0;
	public final static int MOVE_WAS_JUMP = 1;
	public final static int MOVE_MADE_KING = 2;
	
	private ArrayList<MoveRestriction> restrictions = new ArrayList<MoveRestriction>();
	private ArrayList<Move> moves = new ArrayList<Move>();
	
	public MoveExecuter()
	{
		
	}

	public void addRestriction(MoveRestriction mr)
	{
		restrictions.add(mr);
	}
	public void removeRestriction(MoveRestriction mr)
	{
		restrictions.remove(mr);
	}
	
	public void addMove(Move m)
	{
		moves.add(m);
	}
	public void removeMove(Move m)
	{
		moves.remove(m);
	}
	public void clearMoves()
	{
		moves.clear();
	}
	public ArrayList<Move> getMoveList()
	{
		return moves;
	}
	
	public void markAndDisplayValidMoves(Piece piece)
	{
		for(Move m : moves)
		{
			if(m.getDestinationSpace() != null)
			{
				for(MoveRestriction mr : restrictions)
				{
					if(mr.isPermitted(m, piece))
					{
						m.getDestinationSpace().setValidMove(true);
						m.getDestinationSpace().getPanel().setBackground(Color.green);
					}
				}
			}
		}
	}
	
	public ArrayList<Move> getValidMovesList(Piece piece)
	{
		ArrayList<Move> validMoves = new ArrayList<Move>();
		for(Move m : moves)
		{
			if(m.getDestinationSpace() != null)
			{
				for(MoveRestriction mr : restrictions)
				{
					if(mr.isPermitted(m, piece))
					{
						validMoves.add(m);
					}
				}
			}
		}
		return validMoves;
	}
	
	public static int executeMove(Game game, Move move)
	{
		int returnVal = MOVE_WAS_NORMAL;
		
		int rowDiff = Math.abs(move.getOriginalSpace().getLocation().x - move.getDestinationSpace().getLocation().x);
		int colDiff = Math.abs(move.getOriginalSpace().getLocation().y - move.getDestinationSpace().getLocation().y);
		if(rowDiff == 2 || colDiff == 2)
		{
			Space inbetween = game.getSpaceInBetween(move.getOriginalSpace(), move.getDestinationSpace());
			if(inbetween != null && inbetween.getPiece() != null && inbetween.getPiece().getSide() != move.getPiece().getSide())
			{
				inbetween.getPiece().setAlive(false);
				inbetween.setPiece(null);
				returnVal = MOVE_WAS_JUMP;
			}
		}

		//The move only actually made a king if it wasn't already a king
		//Without this check there's the possibility that an existing king could jump to the back row
		//and still have a jump available, but returning MOVE_MADE_KING would prevent that second
		//jump from happening
		if(move.getPiece() instanceof CheckersPiece && !((CheckersPiece)move.getPiece()).isKing())
		{
			//Piece is not a king, is moving up the board and reached the last row so it is now a king
			if(move.getPiece().getMovementDirection() == 1 && move.getDestinationSpace().getLocation().x == 7)
			{
				((CheckersPiece)move.getPiece()).setIsKing();
				returnVal = MOVE_MADE_KING;
			}
			//Piece is not a king, is moving down the board and reached the first row so it is now a king
			else if(move.getPiece().getMovementDirection() == -1 && move.getDestinationSpace().getLocation().x == 0)
			{
				((CheckersPiece)move.getPiece()).setIsKing();
				returnVal = MOVE_MADE_KING;
			}
		}

		move.getDestinationSpace().setPiece(move.getPiece());
		move.getOriginalSpace().setPiece(null);
		
		return returnVal;
	}
}
