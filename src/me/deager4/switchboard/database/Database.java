package me.deager4.switchboard.database;

import java.net.InetAddress;
import java.util.HashMap;

public class Database 
{
	private HashMap<InetAddress, Client> onlineClientMap;
	
	private HashMap<InetAddress, Client> approvedClientMap;
	
	
	public Database()
	{
		onlineClientMap = new HashMap<InetAddress, Client>();
		approvedClientMap = new HashMap<InetAddress, Client>();
	}
	
	public void addClient(Client client)
	{
		approvedClientMap.put(client.getAddress(), client);
	}
	
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
	
	public void setClientOnline(InetAddress address)
	{
		onlineClientMap.put(address, approvedClientMap.get(address));
	}
	
	public void setClientOffline(InetAddress address)
	{
		onlineClientMap.remove(address);
	}
	
	public Client isClientOnline(InetAddress address)
	{
		return onlineClientMap.get(address);
	}
}
