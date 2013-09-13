package me.deager4.switchboard.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.deager4.switchboard.SwitchBoard;

/**
 * Writes Strings to a log file. 
 * Keeps track of server activity;
 * @author deager4
 *
 */
public class Logger
{
	/**
	 * The {@link File} that the server activity will be logged to
	 */
	private File logFile;
	
	/**
	 * An object that logs all server activity to a text file
	 * @param file {@link File}
	 */
	public Logger(File file)
	{
		this.logFile = file;
	}
	
	/**
	 * Writes a String to {@link Logger#logFile} with the date and time preceding it
	 * @param line String
	 * @throws IOException
	 */
	public void logLine(String line) throws IOException
	{
		FileWriter saver = new FileWriter(logFile.getAbsoluteFile(), true);
		PrintWriter writer = new PrintWriter(saver);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		SwitchBoard.getMenu().writeToLogArea(format.format(date) + ": " + line);
		writer.println(format.format(date) + ": " + line);
		writer.close();
		saver.close();
	}
	
}
