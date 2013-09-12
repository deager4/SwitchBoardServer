package me.deager4.switchboard.database;

import java.net.InetAddress;
import java.util.HashMap;

/**
 * The Database
 * @author deager4
 *
 */
public class Database 
{
	/**
	 * A {@link HashMap}<{@link InetAddress}, {@link Client}> that stores all of the {@link Client}s that are currently online
	 */
	private HashMap<InetAddress, Client> onlineClientMap;
	
	/**
	 * A {@link HashMap}<{@link InetAddress}, {@link Client}> that stores all of the approved {@link Client}s on the server
	 */
	private HashMap<InetAddress, Client> approvedClientMap;
	
	
	public Database()
	{
		onlineClientMap = new HashMap<InetAddress, Client>();
		approvedClientMap = new HashMap<InetAddress, Client>();
	}
	
	/**
	 * Adds a {@link Client} to {@link Database#onlineClientMap}
	 * @param client {@link Client}
	 */
	public void addClient(Client client)
	{
		approvedClientMap.put(client.getAddress(), client);
	}
	
	/**
	 * Checks to see if the supplied {@link InetAddress} is an authorized {@link Client} stored in {@link Database#approvedClientMap}
	 * @param address {@link InetAddress}
	 * @return {@link boolean}
	 */
	public boolean isApprovedClient(InetAddress address)
	{
		if(approvedClientMap.get(address) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	/**
	 * Shows the {@link Client} as Online
	 * @param address {@link InetAddress}
	 */
	public void setClientOnline(InetAddress address)
	{
		onlineClientMap.put(address, approvedClientMap.get(address));
	}

	
	/**
	 * Shows the {@link Client} as Offline
	 * @param address {@link InetAddress}
	 */
	public void setClientOffline(InetAddress address)
	{
		onlineClientMap.remove(address);
	}
	
	/**
	 * Gets a the {@link Client} associated with the provided {@link InetAddress} in {@link Database#onlineClientMap}. If no such element exists, returns null.
	 * @param address {@link InetAddress}
	 * @return {@link Client}, null
	 */
	public Client getOnlineClient(InetAddress address)
	{
		return onlineClientMap.get(address);
	}
}
