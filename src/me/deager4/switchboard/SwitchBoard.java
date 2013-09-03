package me.deager4.switchboard;

import javax.swing.JFrame;

import me.deager4.switchboard.database.Database;
import me.deager4.switchboard.threads.MainThread;

public class SwitchBoard 
{
	private static JFrame frame;
	
	private static Database database;
	
	private static MainThread mainThread;
	
	private static int listeningPort;
	
	private static int sendingPort;
	
	public static void main(String args[])
	{
		mainThread = new MainThread();
		mainThread.start();
	}
	
	public static int getListeningPort()
	{
		return listeningPort;
	}
	
	public static int getSendingPort()
	{
		return sendingPort;
	}
	

	public static Database getDatabase()
	{
		return database;
	}

	
	
}
