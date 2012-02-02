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

import gameserver.model.Race;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.spawn.RiftSpawnManager;
import java.nio.ByteBuffer;

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

	public SM_RIFT_ANNOUNCE(Race race) 
	{
		this.race = race;
	}
  
	public SM_RIFT_ANNOUNCE(int action, int annouce, int count, Race race)  
	{
		this.action = action;
		this.annouce = annouce;
		this.count = count;
		this.race = race;
	}

	public SM_RIFT_ANNOUNCE(int action, int targetObjectId, RiftSpawnManager.RiftEnum paramRiftEnum, SpawnTemplate paramSpawnTemplate, int time)
	{
		this.action = action;
		this.targetObjectId = targetObjectId;
		this.rift = paramRiftEnum;
		this.spawnTemplate = paramSpawnTemplate;
		this.time = time;
	}

	public SM_RIFT_ANNOUNCE(int action, int targetObjectId, int usedEntries, int time)
	{
		this.action = action;
		this.targetObjectId = targetObjectId;
		this.usedEntries = usedEntries;
		this.time = time;
	}

	protected void writeImpl(AionConnection com, ByteBuffer buf)
	{
		writeH(buf, action);
		switch (action)
    {
		case 9:
			writeC(buf, annouce);
			writeD(buf, count);
			switch (race) // Destination
			{
            
			// Master rift announcements
            case ASMODIANS:
                writeD(buf, 1);
                writeD(buf, 0);
                break;
            case ELYOS:
                writeD(buf, 1);
                writeD(buf, 0);
                break;
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
