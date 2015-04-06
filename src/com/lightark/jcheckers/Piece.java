package com.lightark.jcheckers;

import java.io.Serializable;

import javax.swing.JComponent;

public interface Piece extends Serializable
{
	public boolean getSide();
	public int getMovementDirection();
	public boolean isAlive();
	public void setAlive(boolean b);
	public Player getPlayer();
	public MoveExecuter getMoveExecuter();
	public void updateMoves(Space currentSpace);
	public JComponent getComponent();
}
