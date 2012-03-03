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
public class SM_MAC extends LsServerPacket {

	private int accountId;
	private String address;
	
	public SM_MAC(int accountId, String address)
	{
		super(0x10);
		this.accountId = accountId;
		this.address = address;
	}

	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
	    writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeS(buf, address);
	}
}
