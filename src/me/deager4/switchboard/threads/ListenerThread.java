package me.deager4.switchboard.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ListenerThread extends Thread
{
	private int port;
	
	private DatagramSocket socket;
	
	private boolean isRunning;
	
	public ListenerThread(int port)
	{
		super("Listener Thread");
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		isRunning = true;
		while(isRunning)
		{
			byte[] buf = null;
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				passPacketToMain(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void terminate()
	{
		isRunning = false;
			socket.close();
		
	}
	
	
	
	private void passPacketToMain(DatagramPacket packet)
	{
		MainThread.addPacketToPool(packet);
	}
}
