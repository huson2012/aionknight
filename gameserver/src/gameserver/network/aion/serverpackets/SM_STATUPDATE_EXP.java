/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * This packet is used to update current exp / recoverable exp / max exp values.
 */
public class SM_STATUPDATE_EXP extends AionServerPacket
{
	private long	currentExp;
	private long	recoverableExp;
	private long	maxExp;

	/**
	 * 
	 * @param currentExp
	 * @param recoverableExp
	 * @param maxExp
	 */
	public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp)
	{
		this.currentExp = currentExp;
		this.recoverableExp = recoverableExp;
		this.maxExp = maxExp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeQ(buf, currentExp);
		writeQ(buf, recoverableExp);
		writeQ(buf, maxExp);
		if(con.getActivePlayer() != null && con.getActivePlayer().getCommonData().getRepletionState() > 0)
		{
			writeQ(buf, con.getActivePlayer().getCommonData().getRepletionState());
			writeQ(buf, (((con.getActivePlayer().getLevel() * 1000) * 2) * con.getActivePlayer().getLevel()));
		}
		else
			writeQ(buf, 0);
			writeQ(buf, 0);
	}
}