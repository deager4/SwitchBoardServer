package me.deager4.switchboard.database;

import java.net.InetAddress;

public class Client
{
	
	private InetAddress address;
	
	private int listeningPort;
	
	private String name;
	
	public Client(InetAddress address, int listeningPort, String name)
	{
		this.address = address;
		this.listeningPort = listeningPort;
		this.name = name;
	}
	
	public InetAddress getAddress()
	{
		return address;
	}
	
	public int getListeningPort()
	{
		return listeningPort;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		String output = this.name + "####" + this.listeningPort + "####" + address.toString();
		return output;
	}
}
