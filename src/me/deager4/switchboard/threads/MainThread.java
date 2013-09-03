package me.deager4.switchboard.threads;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import me.deager4.switchboard.SwitchBoard;

public class MainThread extends Thread
{
	private ListenerThread listenerThread;
	
	private static ArrayList<DatagramPacket> packetPool = new ArrayList<DatagramPacket>();
	
	private boolean isRunning;

	public MainThread()
	{
		super("main thread");
		isRunning = true;
	}
	
	public void run()
	{
		isRunning = true;
		listenerThread = new ListenerThread(SwitchBoard.getListeningPort());
		listenerThread.start();
		while(isRunning == true)
		{
			if(packetPool.size()!= 0)
			{
				DatagramPacket packet = packetPool.get(0);
				InetAddress packetAddress = packet.getAddress();
				if(SwitchBoard.getDatabase().isApprovedClient(packetAddress) == false)
				{
					byte[] buf = packet.getData();
					String data = buf.toString();
					StringTokenizer a = new StringTokenizer(data, "#%#");
					if(a.countTokens() == 0)
					{
						System.out.println("Server received unauthorized empty packet from " + packetAddress.getHostAddress());
					}
					else
					{
						String packetType = a.nextToken();
						if(packetType.equals("auth"))
						{
							//the format of the auth packet will be password, listening port, and name
							//if the password is correct, and everything checks out, there will be 
						}
						else
						{
							System.out.println("Server received unauthorized packet from " + packetAddress.getHostAddress());
						}
						
					}
				}
				else
				{
					byte[] buf = packet.getData();
					String data = buf.toString();
					StringTokenizer a = new StringTokenizer(data, "#%#");
					if(a.countTokens() == 0)
					{
						System.out.println("Server received empty packet from " + packetAddress.getHostAddress());
					}
					else
					{
						String packetType = a.nextToken();
						if(packetType.equals("msg"))
						{
							String target = a.nextToken();
							
							try
							{
								InetAddress targetAddress = InetAddress.getByName(target);
								if(SwitchBoard.getDatabase().isClientOnline(targetAddress) == null)
								{
									String dataToSend = "err#%#There is no client online with the address " + target;
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().isClientOnline(packetAddress), dataToSend);
									senderThread.start();
								}
								else
								{
									String dataToSend = "msg#%#" + a.nextToken();
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().isClientOnline(targetAddress), dataToSend);
									senderThread.start();
								}
							}
							catch (UnknownHostException e)
							{
								System.out.println("Server could not find address " + target);
							}
							
						}
						else if(packetType.equals("login"))
						{
							if(SwitchBoard.getDatabase().isClientOnline(packetAddress) != null)
							{
								
							}
							else
							{
								SwitchBoard.getDatabase().setClientOnline(packetAddress);
							}
						}
						else if(packetType.equals("logout"))
						{
							if(SwitchBoard.getDatabase().isClientOnline(packetAddress) == null)
							{
								
							}
							else
							{
								SwitchBoard.getDatabase().setClientOffline(packetAddress);
							}
						}
						else if(packetType.equals("chk"))
						{
							
						}
						else if(packetType.equals("chk-resp"))
						{
							
						}
					}
				}
				
				packetPool.remove(0);
			}
			else
			{
				continue;
			}
		}
	}
	
	public void terminate()
	{
		isRunning = false;
		listenerThread.terminate();
	}
	
	public static void addPacketToPool(DatagramPacket packet)
	{
		packetPool.add(packet);
	}
}
