package me.deager4.switchboard.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import me.deager4.switchboard.SwitchBoard;
import me.deager4.switchboard.database.Client;


/**
 * The main thread for the server. handles all packet processing
 * @author deager4
 *
 */
public class MainThread extends Thread
{
	/**
	 * THe {@link ListenerThread} of the server
	 */
	private ListenerThread listenerThread;
	
	/**
	 * An {@link ArrayList}<{@link DatagramPacket}> that contains all packets received by the server. This allows the server to keep receiving packets while processing previous packets.
	 */
	private static ArrayList<DatagramPacket> packetPool = new ArrayList<DatagramPacket>();
	
	/**
	 *The run controller for the thread. If true, this thread will run. If false, no running for you.
	 */
	private boolean isRunning;

	/**
	 * The main processing thread for the server. Processes all packets passed to it by {@link MainThread#listenerThread}
	 */
	public MainThread()
	{
		super("main thread");
		isRunning = true;
	}
	
	
	/**
	 * Runs the thread
	 */
	public void run()
	{
		isRunning = true;
		listenerThread = new ListenerThread(SwitchBoard.getListeningPort());
		listenerThread.start();
		while(isRunning == true)
		{
			if(packetPool.size()!= 0) //if there are packets in the pool
			{
				DatagramPacket packet = packetPool.get(0); //gets the first packet in the pool. This way the packets get processed in the order that they are received
				InetAddress packetAddress = packet.getAddress();
				if(SwitchBoard.getDatabase().isApprovedClient(packetAddress) == false) //checks to see if the connecting client is already part of the network
				{
					byte[] buf = packet.getData();
					String data = buf.toString();
					StringTokenizer a = new StringTokenizer(data, "#%#");
					if(a.countTokens() == 0) //if there are no other tokens, the packet is empty, I wanted to get rid of these first, to get them out of the pool
					{
						try {
							SwitchBoard.getLogger().logLine("[main thread]:Server received unauthorized empty packet from " + packetAddress.getHostAddress());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else //if there is something in the packet
					{
						String packetType = a.nextToken();
						if(packetType.equals("auth"))//if the packet is an auth request packet
						{
							if(a.countTokens() == 0) //if the packet is empty other than a header, get rid of it. I really don't know how this would happen.
							{
								try {
									SwitchBoard.getLogger().logLine("[main thread]:Server received empty auth packet from " + packetAddress.getHostAddress());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else //there is something other than the header
							{
								String password = a.nextToken();
								if(!password.equals(SwitchBoard.getPassword())) // checks to see if the password is correct
								{
									String dataToSend = "auth-deny#%#Incorrect Password Knave!!"; //foolish knave...
									SenderThread senderThread = new SenderThread(packet.getAddress(), packet.getPort(), dataToSend);
									senderThread.start();
								}
								else //otherwise add him to the clientlist
								{
									int port = Integer.parseInt(a.nextToken());
									String name = a.nextToken();
									Client client = new Client(packet.getAddress(), port, name);
									try {
										SwitchBoard.getLogger().logLine("[main thread]:Server received propper authentication code from " + packet.getAddress() + ", Adding to client list...");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									SwitchBoard.getDatabase().addClient(client);
									//sends an empty packet, other than the hader, to the client, letting them know that they are in fact allowed to communicate with the server
									String dataToSend = "auth-success#%#";
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().getOnlineClient(packet.getAddress()), dataToSend);
									senderThread.start();
								}
							}
						}
						else // the header is not "auth"
						{
							try {
								SwitchBoard.getLogger().logLine("[main thread]:Server received unauthorized packet from " + packetAddress.getHostAddress());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
				}
				else //this person is already a client
				{
					byte[] buf = packet.getData();
					String data = buf.toString();
					StringTokenizer a = new StringTokenizer(data, "#%#");
					if(a.countTokens() == 0) //empty packet checking
					{
						try {
							SwitchBoard.getLogger().logLine("[main thread]:Server received empty packet from " + packetAddress.getHostAddress());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
									try {
										SwitchBoard.getLogger().logLine("[main thread]:Server sending message packet to " + targetAddress.getHostAddress());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									String dataToSend = "msg#%#" + a.nextToken();
									SenderThread senderThread = new SenderThread(SwitchBoard.getDatabase().getOnlineClient(targetAddress), dataToSend);
									senderThread.start();
								}
							}
							catch (UnknownHostException e)
							{
								try {
									SwitchBoard.getLogger().logLine("[main thread]:Server could not find address " + target);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							
						}
						else if(packetType.equals("login"))
						{
							if(SwitchBoard.getDatabase().getOnlineClient(packetAddress) != null)
							{
								try {
									SwitchBoard.getLogger().logLine("[main thread]:Server received redundant login request from " + packet.getAddress().toString());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
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
								try {
									SwitchBoard.getLogger().logLine("[main thread]:Server received a What the FUck packet, something is horribly wrong!!!");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else
							{
								SwitchBoard.getDatabase().setClientOffline(packetAddress);
							}
						}
						else if(packetType.equals("chk"))
						{
							//TODO: Do This
							//deprecated right now
						}
						else if(packetType.equals("chk-resp"))
						{
							//TODO: DO This
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
	
	
	/**
	 * Stops the thread by setting {@link MainThread#isRunning} to False. Also terminates {@link MainThread#listenerThread}
	 */
	public void terminate()
	{
		isRunning = false;
		listenerThread.terminate();
	}
	
	
	/**
	 * Adds a {@link DatagramPacket} to {@link MainThread#packetPool}.
	 * @param packet
	 */
	public static void addPacketToPool(DatagramPacket packet)
	{
		packetPool.add(packet);
	}
}
