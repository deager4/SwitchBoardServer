package me.deager4.switchboard.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Opens a socket that listens for Packets. Passes all packets, regardless of content
 * @author deager4
 *
 */
public class ListenerThread extends Thread
{
	/**
	 * the port that the thread is listening on
	 */
	private int port;
	
	/**
	 * the {@link DatagramSocket} that the server is using to listen for packets
	 */
	private DatagramSocket socket;
	
	/**
	 * The run controller for the thread
	 */
	private boolean isRunning;
	
	
	/**
	 * Creates a Listening Thread
	 * @param port int
	 */
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
	
	/**
	 * runs the thread
	 */
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
	
	/**
	 * Stops the thread. Also closes {@link ListenerThread#socket}
	 */
	public void terminate()
	{
		isRunning = false; //stops while loop
			socket.close();
		
	}
	
	
	/**
	 * Passes a packet to the packet pool in the Main Thread
	 * @param packet {@link DatagramPacket}
	 */
	private void passPacketToMain(DatagramPacket packet)
	{
		MainThread.addPacketToPool(packet);
	}
}
