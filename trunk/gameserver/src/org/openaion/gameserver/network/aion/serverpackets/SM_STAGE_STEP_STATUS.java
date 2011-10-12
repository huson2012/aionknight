
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

public class SM_STAGE_STEP_STATUS extends AionServerPacket
{
	private int unk1;
	private int unk2;
	private int mess;

	public SM_STAGE_STEP_STATUS(int unk1, int mess, int unk2)
	{
		this.unk1 = unk1;
		this.mess = mess;
		this.unk2 = unk2;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, unk1); // unk 2 or 3 
		writeD(buf, 0); 
		writeH(buf, mess); // id Stage/Round
		writeH(buf, unk2); // unk(1,3,4,6......)
	}
}
