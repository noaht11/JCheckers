package com.lightark.checkers;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Game implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final static boolean PLAYER_WHITE = true;
	public final static boolean PLAYER_BLACK = false;
	
	public final static int SELECTION_PIECE = 0;
	public final static int SELECTION_MOVE = 1;
	public final static int SELECTION_MULTIPLE_MOVES_SELECT = 2;
	public final static int SELECTION_MULTIPLE_MOVES_MOVE = 3;
	
	private Player white;
	private Player black;
	
	private Space[][] spaces;
	
	private boolean turn = PLAYER_BLACK;
	
	private int selectionMode = SELECTION_PIECE;
	
	private Space selectedSpace = null;
	
	private boolean winner = PLAYER_BLACK;
	
	private transient GameInfo info;
	private transient JFrame gameWindow;
	
	public Game()
	{		
		initGameBoard();

		white = new Player(this, PLAYER_WHITE);
		black = new Player(this, PLAYER_BLACK);

		initGameWindow();
	}
	
	private void initGameBoard()
	{
		spaces = new Space[8][8];
		
		for(int row = 0;row < 8;row++)
		{
			for(int column = 0;column < 8;column++)
			{
				boolean spaceFilled = false;
				if((row % 2 == 0))
				{
					if(column % 2 == 0)
					{
						spaceFilled = true;
					}
				}
				else
				{
					if(column % 2 != 0)
					{
						spaceFilled = true;
					}
				}

				Point location = new Point(row,column);
				Space spc = new Space(this, spaceFilled, location);
				spaces[row][column] = spc;
			}
		}
	}
	
	private void initGameWindow()
	{		
		gameWindow = new JFrame("JCheckers");
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setResizable(false);
		gameWindow.setIconImages(Checkers.icons);
		gameWindow.setLayout(new BorderLayout());
		
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(8,8));
		
		for(int rowCount = 7;rowCount >= 0;rowCount--)
		{
			for(int columnCount = 0;columnCount <= 7;columnCount++)
			{
				boardPanel.add(spaces[rowCount][columnCount].getPanel());
			}
		}
		
		gameWindow.add(boardPanel, BorderLayout.CENTER);
		
		info = new GameInfo(this);
		gameWindow.add(info.getPanel(), BorderLayout.LINE_END);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveGame = new JMenuItem("Save Game...");
		saveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				SaveFileChooser sfc = new SaveFileChooser(gameWindow, ".csg", "Checkers Save Game (*.csg)", "MyGame.csg")
				{
					@Override
					public void chosen(Object obj)
					{
						saveGame((File)obj);
					}
				};
				sfc.showChooser();
			}
		});
		fileMenu.add(saveGame);
		
		JMenuItem openGame = new JMenuItem("Open Game...");
		openGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		openGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				OpenFileChooser ofc = new OpenFileChooser(gameWindow, ".csg", "Checkers Save Game (*.csg)")
				{
					@Override
					public void chosen(Object obj)
					{
						try
						{
							Game loadedGame = loadGameFromFile((File)obj);
							loadedGame.initGameWindow();
							loadedGame.resetAllSpacesColor();
							loadedGame.getGameWindow().setVisible(true);
							loadedGame.info.updateTurn();
							loadedGame.info.updatePieceCount();
							Game.this.getGameWindow().dispose();
						}
						catch (ClassNotFoundException e)
						{
							e.printStackTrace();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				};
				ofc.showChooser();
			}
		});
		fileMenu.add(openGame);
		
		menuBar.add(fileMenu);
		
		JMenu gameMenu = new JMenu("Game");
		JMenuItem restart = new JMenuItem("Restart");
		restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		restart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				reset();
			}
		});
		gameMenu.add(restart);
		
		menuBar.add(gameMenu);
		
		gameWindow.setJMenuBar(menuBar);
		
		gameWindow.pack();
		gameWindow.setLocationRelativeTo(null);
	}
	
	private void saveGame(File f)
	{
		try
		{
			Game.saveGameToFile(Game.this, f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Player getPlayer(boolean player)
	{
		if(player == PLAYER_WHITE)
		{
			return white;
		}
		else if(player == PLAYER_BLACK)
		{
			return black;
		}
		return null;
	}
	
	public Space[][] getSpaces()
	{
		return spaces;
	}
	
	public Space getSpaceAt(int row, int column)
	{
		if(row < 8 && row > -1 && column < 8 && column > -1)
		{
			return spaces[row][column];
		}
		else
		{
			return null;
		}
	}
	
	public Space getSpaceInBetween(Space space1, Space space2)
	{
		if(space1 != null && space2 != null)
		{
			int row1 = space1.getLocation().x;
			int col1 = space1.getLocation().y;
			
			int row2 = space2.getLocation().x;
			int col2 = space2.getLocation().y;
			
			int inRow = (Math.min(row1, row2) + 1);
			int inCol = (Math.min(col1, col2) + 1);
			
			return getSpaceAt(inRow, inCol);
		}
		else
		{
			return null;
		}
	}
	
	public void resetAllSpacesColor()
	{
		for(Space[] spcs : spaces)
		{
			for(Space spc : spcs)
			{
				spc.setSelected(false);
				spc.displaySelect();
				spc.setValidMove(false);
				spc.displayValidMove();
			}
		}
		selectedSpace = null;
		gameWindow.validate();
		gameWindow.repaint();
	}
	
	public void spaceClicked(Space clickedSpace)
	{
		if(selectionMode == SELECTION_PIECE || selectionMode == SELECTION_MULTIPLE_MOVES_SELECT)
		{
			if(clickedSpace.getPiece() != null && clickedSpace.getPiece().getSide() == turn)
			{
				resetAllSpacesColor();
				clickedSpace.setSelected(true);
				clickedSpace.displaySelect();

				clickedSpace.getPiece().getPlayer().updateCanJump();
				
				if(selectionMode == SELECTION_PIECE)
				{
					selectionMode = SELECTION_MOVE;
				}
				else if(selectionMode == SELECTION_MULTIPLE_MOVES_SELECT)
				{
					selectionMode = SELECTION_MULTIPLE_MOVES_MOVE;
				}
				
				if(!clickedSpace.getPiece().getPlayer().getCanJump())
				{
					((CheckersPiece)clickedSpace.getPiece()).updateMoves(clickedSpace, false);
				}
				clickedSpace.getPiece().getMoveExecuter().markAndDisplayValidMoves(clickedSpace.getPiece());
				
				selectedSpace = clickedSpace;
			}
		}
		else if(selectionMode == SELECTION_MOVE || selectionMode == SELECTION_MULTIPLE_MOVES_MOVE)
		{
			if(clickedSpace.isValidMove())
			{
				CheckersPiece movingPiece = (CheckersPiece)selectedSpace.getPiece();
				int result = MoveExecuter.executeMove(this, new Move(selectedSpace.getPiece(), selectedSpace, clickedSpace));
				resetAllSpacesColor();
				
				movingPiece.updateMoves(clickedSpace, true);
				if(result == MoveExecuter.MOVE_MADE_KING || result == MoveExecuter.MOVE_WAS_NORMAL)
				{
					selectionMode = SELECTION_PIECE;
					switchTurn();
				}
				else if(result == MoveExecuter.MOVE_WAS_JUMP && movingPiece.getMoveExecuter().getValidMovesList(movingPiece).size() > 0)
				{
					selectionMode = SELECTION_MULTIPLE_MOVES_SELECT;
					spaceClicked(clickedSpace);
				}
				else
				{
					selectionMode = SELECTION_PIECE;
					switchTurn();
				}
			}
			else
			{
				if(selectionMode != SELECTION_MULTIPLE_MOVES_MOVE)
				{
					if(clickedSpace.isSelected())
					{
						resetAllSpacesColor();
						selectionMode = SELECTION_PIECE;
					}
					else
					{
						if(clickedSpace.getPiece() != null && clickedSpace.getPiece().getSide() == turn)
						{
							selectionMode = SELECTION_PIECE;
							spaceClicked(clickedSpace);
						}
					}
				}
			}
		}
		white.updatePieceCounts();
		black.updatePieceCounts();
		info.updatePieceCount();
	}
	
	private void checkForWin()
	{
		if(white.getPiecesRemaining() <= 0 || !white.canMove())
		{
			winner = PLAYER_BLACK;
			
			WinDialog winDialog = new WinDialog(this);
			winDialog.setVisible(true);
		}
		else if(black.getPiecesRemaining() <= 0 || !black.canMove())
		{
			winner = PLAYER_WHITE;
			
			WinDialog winDialog = new WinDialog(this);
			winDialog.setVisible(true);			
		}
	}
	
	public boolean getWinner()
	{
		return winner;
	}
	
	public boolean getTurn()
	{
		return turn;
	}
	
	public void switchTurn()
	{
		turn = !turn;
		info.updateTurn();
		white.updatePieceCounts();
		black.updatePieceCounts();
		info.updatePieceCount();
		checkForWin();
	}
	
	public JFrame getGameWindow()
	{
		return gameWindow;
	}
	
	public void reset()
	{
		for(Space[] spcs : spaces)
		{
			for(Space spc : spcs)
			{
				spc.reset();
			}
		}

		white = new Player(this, PLAYER_WHITE);
		black = new Player(this, PLAYER_BLACK);
		
		turn = PLAYER_BLACK;
		
		info.reset();
		
		gameWindow.pack();
		gameWindow.validate();
		gameWindow.repaint();
	}
	
	public static void saveGameToFile(Game game, File destination) throws IOException
	{
        FileOutputStream fileOut = new FileOutputStream(destination);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(game);
        out.close();
        fileOut.close();
	}
	
	public static Game loadGameFromFile(File source) throws IOException, ClassNotFoundException
	{
		Game game = null;
        FileInputStream fileIn = new FileInputStream(source);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        try
        {
        	game = (Game)in.readObject();
        }
        finally
        {
        	in.close();
        	fileIn.close();
        }
        
        return game;
	}
}
