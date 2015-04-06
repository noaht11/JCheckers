package com.lightark.checkers;

import java.io.Serializable;

public interface MoveRestriction extends Serializable
{
	public boolean isPermitted(Move move, Piece piece);
}
