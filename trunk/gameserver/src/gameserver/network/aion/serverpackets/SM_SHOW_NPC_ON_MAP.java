/**
 * This file is part of Aion-Knight <Aion-Knight.smfnew.com>.
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

/**
 * @author Lyahim
 *
 */
public class SM_SHOW_NPC_ON_MAP extends AionServerPacket
{
	private	int npcid, worldid;
	private	float x,y,z;
	
	public SM_SHOW_NPC_ON_MAP(int npcid, int worldid, float x, float y, float z)
	{
		this.npcid = npcid;
		this.worldid = worldid;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, this.npcid);
		writeD(buf, this.worldid);
		writeD(buf, this.worldid);
		writeF(buf, this.x);
		writeF(buf, this.y);
		writeF(buf, this.z);
	}
}
