package com.lightark.checkers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WinDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel rootPanel;
	
	private Image imageOrig;
	
	public WinDialog(final Game game)
	{
		super(game.getGameWindow(), "Victory", true);
		int winW = 500;
		int winH = 325;
		this.setSize(winW, winH);
		this.setResizable(false);
		this.setLocationRelativeTo(game.getGameWindow());
		this.setUndecorated(true);
		this.setIconImages(Checkers.icons);
		
		this.setLayout(new BorderLayout());
		
		imageOrig = new ImageIcon(ResourceLoader.loadResource("Resources/bg/Win_BG.png")).getImage();
		
		rootPanel = new JPanel()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g)
			{
			    super.paintComponent(g);
			    Graphics2D g2 = (Graphics2D)g;
			    g2.drawImage(imageOrig, 0, 0, null);
			}
		};
		rootPanel.setLayout(new BorderLayout());
		rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 25, 20));
		
		JLabel winner = new JLabel("Error");
		if(game.getWinner() == Game.PLAYER_WHITE)
		{
			winner = new JLabel((CheckersPiece.PIECE_WHITE_NAME + " WINS"), CheckersPiece.PIECE_WHITE_KING_LARGE, JLabel.CENTER);
		}
		else if(game.getWinner() == Game.PLAYER_BLACK)
		{
			winner = new JLabel((CheckersPiece.PIECE_BLACK_NAME + " WINS"), CheckersPiece.PIECE_BLACK_KING_LARGE, JLabel.CENTER);
		}
		winner.setHorizontalTextPosition(JLabel.CENTER);
		winner.setVerticalTextPosition(JLabel.TOP);
		winner.setIconTextGap(20);
		winner.setFont(new Font("Tahoma", Font.BOLD, 40));
		winner.setForeground(Color.white);
		rootPanel.add(winner, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0,3));
		
		Font buttonFont = new Font("Tahoma", Font.BOLD, 20);
		
		JButton playAgain = new JButton("Play Again");
		playAgain.setFont(buttonFont);
		playAgain.setContentAreaFilled(false);
		playAgain.setFocusable(false);
		playAgain.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				game.reset();
				dispose();
			}
		});

		JButton viewBoard = new JButton("View Board");
		viewBoard.setFont(buttonFont);
		viewBoard.setContentAreaFilled(false);
		viewBoard.setFocusable(false);
		viewBoard.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				dispose();
			}
		});

		JButton exit = new JButton("Exit");
		exit.setFont(buttonFont);
		exit.setContentAreaFilled(false);
		exit.setFocusable(false);
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				System.exit(0);
			}
		});
		
		buttonPanel.add(playAgain);
		buttonPanel.add(viewBoard);
		buttonPanel.add(exit);
		rootPanel.add(buttonPanel, BorderLayout.PAGE_END);
		
		this.add(rootPanel, BorderLayout.CENTER);
	}
}
