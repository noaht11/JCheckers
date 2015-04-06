package com.lightark.jcheckers;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class JCheckers
{
	public static List<Image> icons = new ArrayList<Image>();
	
	public static void main(String[] args)
	{
		icons.add(new ImageIcon(ResourceLoader.loadResource("Resources/icons/Icon_256.png")).getImage());
		icons.add(new ImageIcon(ResourceLoader.loadResource("Resources/icons/Icon_128.png")).getImage());
		icons.add(new ImageIcon(ResourceLoader.loadResource("Resources/icons/Icon_64.png")).getImage());
		icons.add(new ImageIcon(ResourceLoader.loadResource("Resources/icons/Icon_48.png")).getImage());
		icons.add(new ImageIcon(ResourceLoader.loadResource("Resources/icons/Icon_32.png")).getImage());
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) 
		{
	    	e.printStackTrace();
	    }
	    catch (ClassNotFoundException e)
		{
	    	e.printStackTrace();
	    }
	    catch (InstantiationException e)
		{
	    	e.printStackTrace();
	    }
	    catch (IllegalAccessException e)
		{
	    	e.printStackTrace();
	    }
		
		Game theGame = new Game();
		theGame.getGameWindow().setVisible(true);
	}
}