package me.deager4.switchboard.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import me.deager4.switchboard.database.Client;

public class SenderThread extends Thread
{
	
	private DatagramPacket packet;
	
	private DatagramSocket socket;
	public SenderThread(Client client, String data)
	{
		super("sender Thread");
		packet = new DatagramPacket(data.getBytes(), data.getBytes().length, client.getAddress(), client.getListeningPort());
	}
	
	public SenderThread(InetAddress address, int port, String data)
	{
		super("sender Thread");
		packet = new DatagramPacket(data.getBytes(), data.getBytes().length, address, port);
	}
	
	public void run()
	{
		try 
		{
			socket = new DatagramSocket();
			socket.send(packet);
		} catch (SocketException e)
{
			System.out.println("unable to create socket");
		} catch (IOException e) {
			System.out.println("unable to send packet");
		}
		
		this.stop();
		socket.close();
		
	}
}
