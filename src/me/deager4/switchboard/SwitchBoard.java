package me.deager4.switchboard;

import javax.swing.JFrame;

import me.deager4.switchboard.database.Database;
import me.deager4.switchboard.gui.MainMenu;
import me.deager4.switchboard.threads.MainThread;

/**
 * The Main class for the server, basically this is where everything lives. There are lots of calls made to the private variables located here
 * @since 09/06/13
 * @author deager
 */
public class SwitchBoard 
{
	/**
	 * The {@link JFrame} for the Switchboard gui
	 */
	private static JFrame frame;
	
	/**
	 * The {@link Database} for the Server
	 */
	private static Database database;
	
	/**
	 * The {@link MainThread} for the Server
	 */
	private static MainThread mainThread;
	
	/**
	 * The port on which the Server listens for packets.
	 */
	private static int listeningPort;
	
	/**
	 * the {@link MainMenu} for the Server
	 * THis is where all of the GUI madness happens
	 */
	private static MainMenu mainMenu;
	
	/**
	 * The Password for the server, Stored as a String
	 */
	private static String password;
	
	
	/**
	 * Does nothing right now
	 * @param args
	 */
	public static void main(String args[])
	{
		
	}
	
	/**
	 * Starts the actual server, it is removed from the main to allow the user to actually set up the server without the server exploding
	 */
	public static void startServer()
	{
		mainThread = new MainThread();
		mainThread.start();
	}
	
	/**
	 * Do I really have to explain what this will do
	 */
	public static void stopServer()
	{
		//TODO:Actuall do this
	}
	
	/**
	 * The public getter for {@link SwitchBoard#frame}, the {@link JFrame} for the entire server}
	 * @return {@link SwitchBoard#frame}
	 */
	public static JFrame getFrame()
	{
		return frame;
	}
	
	/**
	 * The public getter for {@link SwitchBoard#listeningPort} for the Server
	 * @return {@link SwitchBoard#listeningPort}
	 */
	public static int getListeningPort()
	{
		return listeningPort;
	}
	
	/**
	 * Returns {@link SwitchBoard#database} for the Server
	 * @return {@link SwitchBoard#database}
	 */
	public static Database getDatabase()
	{
		return database;
	}
	
	/**
	 * The public getter for {@link SwitchBoard#password} for the server
	 * @return {@link SwitchBoard#password}
	 */
	public static String getPassword()
	{
		return password;
	}
	
	/**
	 * the public setter for {@link SwitchBoard#listeningPort}
	 * @param port an int value greater than 2000
	 */
	public static void setListeningPort(int port)
	{
		listeningPort = port;
	}
	
	/**
	 * The public setter for {@link SwitchBoard#password}
	 * @param newPassword a String
	 */
	public static void setPassword(String newPassword)
	{
		password = newPassword;
	}

	
	
}
