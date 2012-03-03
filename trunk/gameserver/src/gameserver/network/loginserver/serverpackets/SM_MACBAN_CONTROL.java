package gameserver.network.loginserver.serverpackets;

import java.nio.ByteBuffer;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;

/**
 * 
 * @author jenose
 * @author drsaluml
 * @author Rossdale
 *
 */
public class SM_MACBAN_CONTROL extends LsServerPacket
{
	private byte type;
	private int playerId;
	private String address;
	private String details;
	private long time;
	
	public SM_MACBAN_CONTROL(byte type,int playerId , String address, long time, String details)
	{
		super(0x11);
		this.type = type;
		this.address = address;
		this.playerId = playerId;
		this.time = time;
		this.details = details;
	}

	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, type);
		writeD(buf, playerId);
		writeS(buf, address);
		writeS(buf, details);
		writeQ(buf, time);
	}
}
