package me.deager4.switchboard.database;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;

public class Importer 
{
	private File fileLocation;
	private HashMap<InetAddress, Client> fileContents;
	
	public Importer(File file)
	{
		fileLocation = file;
	}
	
	public void exportFile()
	{
		
	}
	
	public HashMap<InetAddress, Client> importFile()
	{
		return fileContents;
	}
}
