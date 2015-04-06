package com.lightark.jcheckers;

import java.io.Serializable;

public class Player implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Game game;
	
	private boolean side;
	private int normalPieceCount = 12;
	private int kingCount = 0;
	
	private boolean canJump = false;
	
	public CheckersPiece[] pieces;
	
	public Player(Game game, boolean side)
	{
		this.game = game;
		this.side = side;
		
		int startRow = 0;
		int endRow = 0;
		if(side == Game.PLAYER_WHITE)
		{
			startRow = 0;
			endRow = 3;
		}
		else if(side == Game.PLAYER_BLACK)
		{
			startRow = 5;
			endRow = 8;
		}
		
		pieces = new CheckersPiece[12];
		int indexCount = 0;
		
		for(int row = startRow; row < endRow;row++)
		{
			for(int column = 0;column < 8;column++)
			{				
				if(row % 2 == 0)
				{
					if(column % 2 == 0)
					{
						CheckersPiece thePiece = new CheckersPiece(game, this);
						game.getSpaceAt(row, column).setPiece(thePiece);
						pieces[indexCount] = thePiece;
						indexCount++;
					}
				}
				else
				{
					if(column % 2 != 0)
					{
						CheckersPiece thePiece = new CheckersPiece(game, this);
						game.getSpaceAt(row, column).setPiece(thePiece);
						pieces[indexCount] = thePiece;
						indexCount++;
					}
				}
			}
		}
	}
		
	public void updateCanJump()
	{
		canJump = false;
		for(Space[] spcs : game.getSpaces())
		{
			for(Space spc : spcs)
			{
				if(spc.getPiece() != null && spc.getPiece().getSide() == side)
				{
					((CheckersPiece)spc.getPiece()).updateMoves(spc, true);
					if(spc.getPiece().getMoveExecuter().getValidMovesList(spc.getPiece()).size() > 0)
					{
						canJump = true;
					}
				}
			}
		}
	}
	
	public boolean getCanJump()
	{
		return canJump;
	}
	
	public boolean canMove()
	{
		for(Space[] spcs : game.getSpaces())
		{
			for(Space spc : spcs)
			{
				if(spc.getPiece() != null && spc.getPiece().getSide() == side)
				{
					((CheckersPiece)spc.getPiece()).updateAllMoves(spc);
					if(spc.getPiece().getMoveExecuter().getValidMovesList(spc.getPiece()).size() > 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int getPiecesRemaining()
	{
		return (normalPieceCount + kingCount);
	}
	
	public void updatePieceCounts()
	{
		int normals = 0;
		int kings = 0;
		for(CheckersPiece piece : pieces)
		{
			if(piece.isAlive())
			{
				if(piece.isKing())
				{
					kings++;
				}
				else
				{
					normals++;
				}
			}
		}
		normalPieceCount = normals;
		kingCount = kings;
	}
	
	public int getNormalPieceCount()
	{
		return normalPieceCount;
	}
	
	public int getKingCount()
	{
		return kingCount;
	}
	
	public boolean getSide()
	{
		return side;
	}
}
