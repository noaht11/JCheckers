package com.lightark.checkers;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class CheckersPiece implements Piece
{
	private static final long serialVersionUID = 1L;
	
	public static ImageIcon PIECE_WHITE = new ImageIcon(ResourceLoader.loadResource("Resources/pieces/Piece_White_48.png"));
	public static ImageIcon PIECE_BLACK = new ImageIcon(ResourceLoader.loadResource("Resources/pieces/Piece_Red_48.png"));
	public static ImageIcon PIECE_WHITE_KING = new ImageIcon(ResourceLoader.loadResource("Resources/pieces/Piece_White_King_48.png"));
	public static ImageIcon PIECE_BLACK_KING = new ImageIcon(ResourceLoader.loadResource("Resources/pieces/Piece_Red_King_48.png"));

	public static ImageIcon PIECE_WHITE_KING_LARGE = new ImageIcon(ResourceLoader.loadResource("Resources/pieces/Piece_White_King_128.png"));
	public static ImageIcon PIECE_BLACK_KING_LARGE = new ImageIcon(ResourceLoader.loadResource("Resources/pieces/Piece_Red_King_128.png"));
	
	public static String PIECE_WHITE_NAME = "WHITE";
	public static String PIECE_BLACK_NAME = "RED";
	
	private Game game;
	
	private Player player;
	
	private JLabel pieceImage;
	
	private boolean alive = true;
	
	private boolean side;
	private int movementDirection;
	
	private boolean king;
	
	private MoveExecuter moveExecuter;
	
	public CheckersPiece(Game game, Player player)
	{
		this.game = game;
		this.player = player;
		this.side = player.getSide();
		
		if(side == Game.PLAYER_WHITE)
		{
			pieceImage = new JLabel(PIECE_WHITE);
			movementDirection = 1;
		}
		else if(side == Game.PLAYER_BLACK)
		{
			pieceImage = new JLabel(PIECE_BLACK);
			movementDirection = -1;
		}
		
		moveExecuter = new MoveExecuter();
		MoveRestriction mr = new MoveRestriction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isPermitted(Move move, Piece piece)
			{
				int rowDiff = Math.abs(move.getOriginalSpace().getLocation().x - move.getDestinationSpace().getLocation().x);
				int colDiff = Math.abs(move.getOriginalSpace().getLocation().y - move.getDestinationSpace().getLocation().y);
				//Check the piece is only moving one or two spaces
				if((rowDiff <= 2) && (colDiff <= 2))
				{
					//Check for no piece on destination
					if(move.getDestinationSpace().getPiece() == null)
					{
						//Check that both squares are the same color (only moving diagonally)
						if(move.getOriginalSpace().isFilled() == move.getDestinationSpace().isFilled())
						{
							//If piece isn't king...
							if(!((CheckersPiece)piece).isKing())
							{
								//... if the piece is going up the board...
								if(piece.getMovementDirection() == 1)
								{
									///... check that destination is going the right direction
									if(move.getDestinationSpace().getLocation().x > move.getOriginalSpace().getLocation().x)
									{
										//If move is a jump
										if(rowDiff == 2 || colDiff == 2)
										{
											return checkForValidJump(move, piece);
										}
										//If move is not a jump
										else
										{
											return true;
										}
									}
								}
								//... if the piece is going down the board...
								else if(piece.getMovementDirection() == -1)
								{
									///... check that destination is going the right direction
									if(move.getDestinationSpace().getLocation().x < move.getOriginalSpace().getLocation().x)
									{
										//If move is a jump
										if(rowDiff == 2 || colDiff == 2)
										{
											return checkForValidJump(move, piece);
										}
										//If move is not a jump
										else
										{
											return true;
										}
									}
								}
							}
							//If the piece is a king than all remaining spaces are valid so just check for a jump
							else
							{
								//If move is a jump
								if(rowDiff == 2 || colDiff == 2)
								{
									return checkForValidJump(move, piece);
								}
								//If move is not a jump
								else
								{
									return true;
								}
							}
						}
					}
				}
				return false;
			}
		};
		
		moveExecuter.addRestriction(mr);
	}
	
	private boolean checkForValidJump(Move move, Piece piece)
	{
		Space inbetween = game.getSpaceInBetween(move.getOriginalSpace(), move.getDestinationSpace());
		if(inbetween != null && inbetween.getPiece() != null && inbetween.getPiece().getSide() != piece.getSide())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean getSide()
	{
		return side;
	}
	
	@Override
	public int getMovementDirection()
	{
		return movementDirection;
	}
	
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	
	@Override
	public void setAlive(boolean b)
	{
		alive = b;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}

	public void setIsKing()
	{
		king = true;

		if(side == Game.PLAYER_WHITE)
		{
			pieceImage = new JLabel(PIECE_WHITE_KING);
			movementDirection = 1;
		}
		else if(side == Game.PLAYER_BLACK)
		{
			pieceImage = new JLabel(PIECE_BLACK_KING);
			movementDirection = -1;
		}
		game.getGameWindow().validate();
		game.getGameWindow().repaint();
	}
	
	public boolean isKing()
	{
		return king;
	}
	
	@Override
	public void updateMoves(Space currentSpace)
	{
		updateMoves(currentSpace, 1, 1);
	}

	public void updateMoves(Space currentSpace, boolean jumps)
	{
		if(jumps)
		{
			updateMoves(currentSpace, 2, 2);
		}
		else
		{
			updateMoves(currentSpace, 1, 1);
		}
	}
	
	public void updateAllMoves(Space currentSpace)
	{
		updateMoves(currentSpace, 1, 2);
	}
	
	public void updateMoves(Space currentSpace, int minDistance, int maxDistance)
	{
		moveExecuter.clearMoves();
		
		int currentRow = currentSpace.getLocation().x;
		int currentCol = currentSpace.getLocation().y;
				
		for(int i = minDistance;i <= maxDistance;i++)
		{
			Move move1 = new Move(this, currentSpace, game.getSpaceAt(currentRow + i, currentCol + i));
			Move move2 = new Move(this, currentSpace, game.getSpaceAt(currentRow - i, currentCol + i));
			Move move3 = new Move(this, currentSpace, game.getSpaceAt(currentRow + i, currentCol - i));
			Move move4 = new Move(this, currentSpace, game.getSpaceAt(currentRow - i, currentCol - i));
			
			moveExecuter.addMove(move1);
			moveExecuter.addMove(move2);
			moveExecuter.addMove(move3);
			moveExecuter.addMove(move4);
		}
	}
	
	@Override
	public MoveExecuter getMoveExecuter()
	{
		return moveExecuter;
	}

	@Override
	public JComponent getComponent()
	{
		return pieceImage;
	}
}
