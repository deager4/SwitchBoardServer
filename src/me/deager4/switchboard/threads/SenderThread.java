package me.deager4.switchboard.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import me.deager4.switchboard.SwitchBoard;
import me.deager4.switchboard.database.Client;

/**
 * Sends a single packet to a single recipient. Note that this thread will self-terminate, meaning that you can just make it and run it, and forget about it.
 * @author deager4
 *
 */
public class SenderThread extends Thread
{
	/**
	 * A {@link DatagramPacket} 
	 */
	private DatagramPacket packet;
	
	/**
	 * The {@link DatagramSocket that the thread is going to be using
	 */
	private DatagramSocket socket;
	
	/**
	 * Creates a sender thread, taking in a {@link Client} and a String as input. This is used when a packet is sent to a known client
	 * @param client {@link Client}
	 * @param data String
	 */
	public SenderThread(Client client, String data)
	{
		super("sender Thread");
		packet = new DatagramPacket(data.getBytes(), data.getBytes().length, client.getAddress(), client.getListeningPort());
	}
	
	/**
	 * Creates a sender thread, used when the client is not known
	 * @param address {@link InetAddress}
	 * @param port int
	 * @param data String
	 */
	public SenderThread(InetAddress address, int port, String data)
	{
		super("sender Thread");
		packet = new DatagramPacket(data.getBytes(), data.getBytes().length, address, port);
	}
	
	/**
	 * runs the thread
	 * 
	 * @note This thread self-terminates. It is only meant to iterate once
	 */
	public void run()
	{
		try 
		{
			socket = new DatagramSocket();
			socket.send(packet);
		} catch (SocketException e)
{
			try {
				SwitchBoard.getLogger().logLine("[sender thread]:unable to create socket");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			try {
				SwitchBoard.getLogger().logLine("[sender thread]:unable to send packet");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		this.stop();
		socket.close();
		
	}
}
