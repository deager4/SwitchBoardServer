package me.deager4.switchboard.database;

import java.net.InetAddress;
/**
 * A Client
 * @author deager4
 *
 */
public class Client
{
	/**
	 * The {@link InetAddress} for the {@link Client}
	 * @type {@link InetAddress}
	 */
	private InetAddress address;

	/**
	 * The port that the {@link Client} is listening on
	 * @type {@link int}
	 */
	private int listeningPort;
	
	/**
	 * The Name of the {@link Client}
	 * @type {@link String}
	 */
	private String name;
	
	/**
	 * A client for the server
	 * @param address an {@link InetAddress}
	 * @param listeningPort an int value greater than 2000
	 * @param name a String
	 */
	public Client(InetAddress address, int listeningPort, String name)
	{
		this.address = address;
		this.listeningPort = listeningPort;
		this.name = name;
	}
	
	/**
	 * Returns the {@link InetAddress} of the {@link Client},  {@link Client#address}
	 * @return {@link Client#address}
	 */
	public InetAddress getAddress()
	{
		return address;
	}
	
	/**
	 * Returns the port value of the {@link Client}, {@link Client#listeningPort}
	 * @return {@link Client#listeningPort}
	 */
	public int getListeningPort()
	{
		return listeningPort;
	}
	
	/**
	 * Returns the Name of the {@link Client}, {@link Client#name}
	 * @return {@link Client#name}
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns a {@link String} printout of the [@link Client}. 
	 * The format of this printout is '{@link Client#name}#%#{@link Client#listeningPort}#%#{@link Client#address}'
	 */
	public String toString()
	{
		String output = this.name + "#%#" + this.listeningPort + "#%#" + address.toString();
		return output;
	}
}
