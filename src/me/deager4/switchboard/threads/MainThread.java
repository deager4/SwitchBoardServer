package me.deager4.switchboard.threads;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import me.deager4.switchboard.SwitchBoard;
import me.deager4.switchboard.database.Client;

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
						System.out.println("[main thread]:Server received unauthorized empty packet from " + packetAddress.getHostAddress());
					}
					else
					{
						String packetType = a.nextToken();
						if(packetType.equals("auth"))
						{
							if(a.countTokens() == 0)
							{
								System.out.println("[main thread]:Server received empty auth packet from " + packetAddress.getHostAddress());
							}
							else
							{
								String password = a.nextToken();
								if(!password.equals(SwitchBoard.getPassword()))
								{
									String dataToSend = "auth-deny#%#Incorrect Password Knave!!";
									SenderThread senderThread = new SenderThread(packet.getAddress(), packet.getPort(), dataToSend);
									senderThread.start();
								}
								else
								{
									int port = Integer.parseInt(a.nextToken());
									String name = a.nextToken();
									Client client = new Client(packet.getAddress(), port, name);
									System.out.println("[main thread]:Server received propper authentication code from " + packet.getAddress() + ", Adding to client list...");
									SwitchBoard.getDatabase().addClient(client);
									String dataToSend = "auth-success#%#";
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().getOnlineClient(packet.getAddress()), dataToSend);
									senderThread.start();
								}
							}
						}
						else
						{
							System.out.println("[main thread]:Server received unauthorized packet from " + packetAddress.getHostAddress());
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
						System.out.println("[main thread]:Server received empty packet from " + packetAddress.getHostAddress());
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
								if(SwitchBoard.getDatabase().getOnlineClient(targetAddress) == null)
								{
									String dataToSend = "err#%#There is no client online with the address " + target;
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().getOnlineClient(packetAddress), dataToSend);
									senderThread.start();
								}
								else
								{
									String dataToSend = "msg#%#" + a.nextToken();
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().getOnlineClient(targetAddress), dataToSend);
									senderThread.start();
								}
							}
							catch (UnknownHostException e)
							{
								System.out.println("[main thread]:Server could not find address " + target);
							}
							
						}
						else if(packetType.equals("login"))
						{
							if(SwitchBoard.getDatabase().getOnlineClient(packetAddress) != null)
							{
								System.out.println("[main thread]:Server received redundant login request from " + packet.getAddress().toString());
								
							}
							else
							{
								SwitchBoard.getDatabase().setClientOnline(packetAddress);
							}
						}
						else if(packetType.equals("logout"))
						{
							if(SwitchBoard.getDatabase().getOnlineClient(packetAddress) == null)
							{
								System.out.println("[main thread]:Server received a What the FUck packet, something is horribly wrong!!!");
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
