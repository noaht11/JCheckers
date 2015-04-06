package com.lightark.jcheckers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Space implements MouseListener, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final static Color COLOR_FILLED = new Color(69, 20, 9);
	public final static Color COLOR_EMPTY = new Color(255,228,196);
	
	public final static Color COLOR_HIGHLIGHT = new Color(50,150,255);
	
	public final static Color COLOR_VALID = new Color(0,255,0);

	public final static Border BORDER_NORMAL = null;
	public final static Border BORDER_HIGHLIGHT = BorderFactory.createLineBorder(COLOR_HIGHLIGHT, 5);
	
	public final static int WIDTH = 75;
	public final static int HEIGHT = 75;
	
	private Game game;
	
	private JPanel panel;
	private Color bgColor;
	
	private boolean selected = false;
	
	private boolean validMove = false;
	
	private Point location;
	private boolean filled;
	
	private Piece piece = null;
	
	public Space(Game game, boolean filled, Point location)
	{
		this.game = game;
		this.filled = filled;
		this.location = location;
		
		if(filled)
		{
			bgColor = COLOR_FILLED;
		}
		else
		{
			bgColor = COLOR_EMPTY;
		}
		
		initPanel();
	}
	
	private void initPanel()
	{
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setBackground(bgColor);
		panel.setLayout(new BorderLayout());
		panel.addMouseListener(this);
	}
	
	public void setPiece(Piece newPiece)
	{
		piece = newPiece;
		if(piece != null)
		{
			panel.add(piece.getComponent(), BorderLayout.CENTER);
		}
		else
		{
			panel.removeAll();
		}
	}
	
	public Piece getPiece()
	{
		return piece;
	}
	
	public void setSelected(boolean s)
	{
		this.selected = s;
	}
	
	public void displaySelect()
	{
		if(selected)
		{
			panel.setBackground(COLOR_HIGHLIGHT);
			panel.setBorder(BORDER_NORMAL);
		}
		else
		{
			panel.setBackground(bgColor);
		}
	}
	
	public void displayValidMove()
	{
		if(validMove)
		{
			panel.setBackground(COLOR_VALID);
			panel.setBorder(BORDER_NORMAL);
		}
		else
		{
			panel.setBackground(bgColor);
		}
	}
	
	public boolean isSelected()
	{
		return selected;
	}

	public void setValidMove(boolean valid)
	{
		validMove = valid;
	}
	
	public boolean isValidMove()
	{
		return validMove;
	}
	
	public boolean isFilled()
	{
		return filled;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public void reset()
	{
		validMove = false;
		selected = false;
		panel.setBackground(bgColor);
		panel.setBorder(BORDER_NORMAL);
		setPiece(null);
	}
	
	@Override
	public void mouseClicked(MouseEvent evt)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent evt)
	{
		game.spaceClicked(this);
	}

	@Override
	public void mouseEntered(MouseEvent evt)
	{
		panel.setBorder(BORDER_HIGHLIGHT);
	}

	@Override
	public void mouseExited(MouseEvent evt)
	{
		panel.setBorder(BORDER_NORMAL);
	}
}
