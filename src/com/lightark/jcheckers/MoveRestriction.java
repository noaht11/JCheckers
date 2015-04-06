package com.lightark.jcheckers;

import java.io.Serializable;

public interface MoveRestriction extends Serializable
{
	public boolean isPermitted(Move move, Piece piece);
}
