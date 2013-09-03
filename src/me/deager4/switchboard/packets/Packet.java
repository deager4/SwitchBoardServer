package me.deager4.switchboard.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Packet 
{
	private String packetType;
	
	private String data;
	
	private int port;
	
	private InetAddress address;
	
	
	public Packet(PacketType packetType, String data, int port, InetAddress address)
	{
		this.packetType = packetType.toString();
		this.data = data;
		this.port = port;
		this.address = address;
	}
	
	public DatagramPacket toDatagram()
	{
		String newData = packetType + " " + data;
		DatagramPacket packet = new DatagramPacket(newData.getBytes(), newData.getBytes().length, this.address, this.port);
		return packet;
	}
}
