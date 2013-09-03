package me.deager4.switchboard.packets;

public enum PacketType 
{
	MESSAGE("msg"),
	CHECK_IN("chk"),
	AUTH("auth"),
	AUTH_RESPONSE("auth-resp");
	
	
	private String type;
	
	PacketType(String type)
	{
		this.type = type;
	}
	
	public String toString()
	{
		return this.type;
	}
}
