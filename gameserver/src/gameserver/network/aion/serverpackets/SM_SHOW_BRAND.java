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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**
 * @author Sweetkr
 * @author Kamui
 */
public class SM_SHOW_BRAND extends AionServerPacket
{
	private int modeId;
	private int brandId;
	private int targetObjectId;
	
	public SM_SHOW_BRAND(int modeId, int brandId, int targetObjectId)
	{
		this.modeId = modeId;
		this.brandId = brandId;
		this.targetObjectId = targetObjectId;
	}


	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{

		writeH(buf, 0x01); //unk
		writeD(buf, modeId);
		writeD(buf, brandId);
		writeD(buf, targetObjectId);
		
	}
}
