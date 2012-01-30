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

public class SM_LEGION_UPDATE_EMBLEM extends AionServerPacket
{
	private int	legionId;
	private int emblemVer;
	private int	color_r;
	private int	color_g;
	private int	color_b;
	private boolean isCustom;


	public SM_LEGION_UPDATE_EMBLEM(int legionId, int emblemVer, int color_r, int color_g, int color_b, boolean isCustom)
	{
		this.legionId = legionId;
		this.emblemVer = emblemVer;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		this.isCustom = isCustom;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, legionId);
		writeC(buf, emblemVer);
		writeC(buf, isCustom ? 0x80 : 0x00);
		writeC(buf, 0xFF);
		writeC(buf, color_r);
		writeC(buf, color_g);
		writeC(buf, color_b);
	}
}