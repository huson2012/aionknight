/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

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