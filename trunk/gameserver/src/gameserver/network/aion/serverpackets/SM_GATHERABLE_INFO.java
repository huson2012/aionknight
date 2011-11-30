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

import gameserver.model.gameobjects.VisibleObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**

 *
 */
public class SM_GATHERABLE_INFO  extends AionServerPacket
{
	private VisibleObject visibleObject;

	public SM_GATHERABLE_INFO(VisibleObject visibleObject)
	{
		super();
		this.visibleObject = visibleObject;
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeF(buf, visibleObject.getX());
		writeF(buf, visibleObject.getY());
		writeF(buf, visibleObject.getZ());
		writeD(buf, visibleObject.getObjectId());
		writeD(buf, visibleObject.getSpawn().getStaticid()); //unk
		writeD(buf, visibleObject.getObjectTemplate().getTemplateId());
		writeH(buf, 1); //unk
		writeC(buf, 0);
		writeD(buf, visibleObject.getObjectTemplate().getNameId());
		writeH(buf, 0);
		writeH(buf, 0);
		writeH(buf, 0);
		writeC(buf, 100); //unk
	}
}
