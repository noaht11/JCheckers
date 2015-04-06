package com.lightark.checkers;

import java.io.Serializable;

public class Move implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Piece piece;
	private Space originalSpace;
	private Space destinationSpace;
	
	public Move(Piece piece, Space originalSpace, Space destinationSpace)
	{
		this.piece = piece;
		this.originalSpace = originalSpace;
		this.destinationSpace = destinationSpace;
	}
	
	public Piece getPiece()
	{
		return piece;
	}
	
	public Space getOriginalSpace()
	{
		return originalSpace;
	}
	
	public Space getDestinationSpace()
	{
		return destinationSpace;
	}
}
