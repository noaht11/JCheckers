package com.lightark.jcheckers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Game game;
	
	private JPanel panel;
	
	private JLabel turnLabel;
	private JLabel blackNormal;
	private JLabel blackKings;
	private JLabel whiteNormal;
	private JLabel whiteKings;
	
	public GameInfo(Game game)
	{
		this.game = game;
		initPanel();
	}
	
	private void initPanel()
	{		
		panel = new JPanel();
		panel.setLayout(new GridLayout(3,0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.setBackground(Color.blue);
		
		Font labelFont = new Font("Tahoma", Font.PLAIN, 40);

		JPanel blackInfo = new JPanel();
		blackInfo.setLayout(new GridLayout(2,0));
		blackInfo.setOpaque(false);
		blackInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.white));
		
		blackNormal = new JLabel("x12", CheckersPiece.PIECE_BLACK, JLabel.LEFT);
		blackNormal.setHorizontalTextPosition(JLabel.RIGHT);
		blackNormal.setVerticalTextPosition(JLabel.CENTER);
		blackNormal.setIconTextGap(10);
		blackNormal.setFont(labelFont);
		blackNormal.setForeground(Color.white);

		blackKings = new JLabel("x0", CheckersPiece.PIECE_BLACK_KING, JLabel.LEFT);
		blackKings.setHorizontalTextPosition(JLabel.RIGHT);
		blackKings.setVerticalTextPosition(JLabel.CENTER);
		blackKings.setIconTextGap(10);
		blackKings.setFont(labelFont);
		blackKings.setForeground(Color.white);
		
		blackInfo.add(blackNormal);
		blackInfo.add(blackKings);
		
		panel.add(blackInfo);
		
		
		JPanel turn = new JPanel();
		turn.setOpaque(false);
		turn.setLayout(new BorderLayout());
		
		turnLabel = new JLabel("Turn:", CheckersPiece.PIECE_WHITE, JLabel.CENTER);
		turnLabel.setHorizontalTextPosition(JLabel.CENTER);
		turnLabel.setVerticalTextPosition(JLabel.TOP);
		turnLabel.setIconTextGap(20);
		turnLabel.setFont(labelFont);
		turnLabel.setForeground(Color.white);
		
		updateTurn();
		
		turn.add(turnLabel, BorderLayout.CENTER);
		
		panel.add(turn);

		
		JPanel whiteInfo = new JPanel();
		whiteInfo.setLayout(new GridLayout(2,0));
		whiteInfo.setOpaque(false);
		whiteInfo.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.white));
		
		whiteNormal = new JLabel("x12", CheckersPiece.PIECE_WHITE, JLabel.LEFT);
		whiteNormal.setHorizontalTextPosition(JLabel.RIGHT);
		whiteNormal.setVerticalTextPosition(JLabel.CENTER);
		whiteNormal.setIconTextGap(10);
		whiteNormal.setFont(labelFont);
		whiteNormal.setForeground(Color.white);

		whiteKings = new JLabel("x0", CheckersPiece.PIECE_WHITE_KING, JLabel.LEFT);
		whiteKings.setHorizontalTextPosition(JLabel.RIGHT);
		whiteKings.setVerticalTextPosition(JLabel.CENTER);
		whiteKings.setIconTextGap(10);
		whiteKings.setFont(labelFont);
		whiteKings.setForeground(Color.white);
		
		whiteInfo.add(whiteNormal);
		whiteInfo.add(whiteKings);
		
		panel.add(whiteInfo);
	}
	
	public void updateTurn()
	{
		if(game.getTurn() == Game.PLAYER_WHITE)
		{
			turnLabel.setIcon(CheckersPiece.PIECE_WHITE);
		}
		else if(game.getTurn() == Game.PLAYER_BLACK)
		{
			turnLabel.setIcon(CheckersPiece.PIECE_BLACK);
		}
		
		game.getGameWindow().pack();
	}
	
	public void updatePieceCount()
	{
		int whiteNormalCount = game.getPlayer(Game.PLAYER_WHITE).getNormalPieceCount();
		int whiteKingCount = game.getPlayer(Game.PLAYER_WHITE).getKingCount();
		whiteNormal.setText("x" + Integer.toString(whiteNormalCount));
		whiteKings.setText("x" + Integer.toString(whiteKingCount));

		int blackNormalCount = game.getPlayer(Game.PLAYER_BLACK).getNormalPieceCount();
		int blackKingCount = game.getPlayer(Game.PLAYER_BLACK).getKingCount();
		blackNormal.setText("x" + Integer.toString(blackNormalCount));
		blackKings.setText("x" + Integer.toString(blackKingCount));
		
		game.getGameWindow().pack();
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public void reset()
	{
		updatePieceCount();
		updateTurn();
	}
}
