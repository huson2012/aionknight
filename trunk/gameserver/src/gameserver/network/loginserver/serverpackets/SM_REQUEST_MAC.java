package gameserver.network.loginserver.serverpackets;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Administrator
 * @author Rossdale
 *
 */
public class SM_REQUEST_MAC extends LsServerPacket {
	
	private int	playerId;
	private int	type;

	public SM_REQUEST_MAC(int type ,int playerId)
	{
		super(0x12);
		this.type = type;
		this.playerId = playerId;
	}
	
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
	    writeC(buf, getOpcode());
	    writeD(buf, type);
		writeD(buf, playerId);
	}
}
