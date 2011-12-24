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

import gameserver.model.Race;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.spawn.RiftSpawnManager;

import java.nio.ByteBuffer;

/**
 * 
 * @author Sweetkr
 *
 */
public class SM_RIFT_ANNOUNCE extends AionServerPacket
{
	private Race race;
	private SpawnTemplate spawnTemplate;
	private RiftSpawnManager.RiftEnum rift;
	private int action;
	private int targetObjectId;
	private int annouce;
	private int usedEntries;
	private int count;
	private int time;

	public SM_RIFT_ANNOUNCE(int action, int annouce, int count, Race race)
	{
		this.action = action;
		this.annouce = annouce;
		this.count = count;
		this.race = race;
	}

	public SM_RIFT_ANNOUNCE(int action, int targetObjectId, RiftSpawnManager.RiftEnum rift, SpawnTemplate spawnTemplate, int time)
	{
		this.action = action;
		this.targetObjectId = targetObjectId;
		this.rift = rift;
		this.spawnTemplate = spawnTemplate;
		this.time = time;
	}

	public SM_RIFT_ANNOUNCE(int action, int targetObjectId, int usedEntries, int time)
	{
		this.action = action;
		this.targetObjectId = targetObjectId;
		this.usedEntries = usedEntries;
		this.time = time;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, action);
		switch(action)
		{
			case 9:
				writeC(buf, annouce);
				writeD(buf, count);
				switch(race.getRaceId())
				{
					case 1:
						writeD(buf, 0);
						break;
					case 2:
						writeD(buf, 1);
				}
				break;
			case 13:
				writeC(buf, 3);
				writeD(buf, targetObjectId);
				writeD(buf, usedEntries);
				writeD(buf, time);
				break;
			case 33:
				writeC(buf, 2);
				writeD(buf, targetObjectId);
				writeD(buf, rift.getEntries());
				writeD(buf, time);
				writeD(buf, 25);
				writeD(buf, rift.getMaxLevel());
				writeF(buf, spawnTemplate.getX());
				writeF(buf, spawnTemplate.getY());
				writeF(buf, spawnTemplate.getZ());
		}
	}
}
