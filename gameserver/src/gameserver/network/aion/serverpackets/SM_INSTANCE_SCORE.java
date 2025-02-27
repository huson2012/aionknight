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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.DredgionInstanceService;
import java.nio.ByteBuffer;

public class SM_INSTANCE_SCORE extends AionServerPacket
{
	private int		mapId;
	private int		instanceTime;
	private int		stopTime;
	private int		totalPoints;
	private int		points;
	private int		kills;
	private int		rank;
	private int		signs;
	private PlayerGroup	elyosGroup;
	private PlayerGroup	asmosGroup;
	private PlayerGroup	registeredGroup;
	private boolean		showRank;

	public SM_INSTANCE_SCORE(int mapId, int instanceTime, int stopTime, int totalPoints, int points, int kills, int rank)
	{
		this.mapId = mapId; // 300040000
		this.instanceTime = instanceTime; // 3h30
		this.stopTime = stopTime; // 2097152 for running time, 
		this.totalPoints = totalPoints; // Total score value
		this.points = points; // Hunted value
		this.kills = kills; // Collection value
		this.rank = rank; // 7 for none, 8 for F, 5 for D, 4 C, 3 B, 2 A, 1 S
	}

	public SM_INSTANCE_SCORE(int mapId, int instanceTime, PlayerGroup elyosGroup, PlayerGroup asmosGroup, boolean showRank)
	{
		this.mapId = mapId;
		this.instanceTime = instanceTime;
		this.elyosGroup = elyosGroup;
		this.asmosGroup = asmosGroup;
		this.showRank = showRank;
	}
	
	public SM_INSTANCE_SCORE(int mapId, int instanceTime, PlayerGroup registeredGroup, int points, int signs, boolean showRank)
	{
		this.mapId = mapId;
		this.instanceTime = instanceTime;
		this.registeredGroup = registeredGroup;
		this.points = points; // Hunted value
		this.signs = signs;
		this.showRank = showRank;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(DredgionInstanceService.isDredgion(mapId))
		{
			writeD(buf, mapId);
			writeD(buf, instanceTime);

			if(showRank)
				writeD(buf, 3145728);
			else
				writeD(buf, 2097152);

			int count = 0;

			for(Player member : elyosGroup.getMembers())
			{
				writeD(buf, member.getObjectId());//playerObjectId
				writeD(buf, member.getAbyssRank().getRank().getId());//playerRank
				writeD(buf, member.getInstancePVPKills());//pvpKills
				writeD(buf, member.getInstanceBalaurKills());//balaurKills
				writeD(buf, member.getInstanceCaptured());//captured
				writeD(buf, member.getInstancePlayerScore());//playerScore

				if(showRank)
				{
					writeD(buf, member.getInstancePlayerAP() + member.getInstancePlayerScore());//apBonus1
					writeD(buf, member.getInstancePlayerAP());//apBonus2
				}else{
					writeD(buf, 0);//apBonus1
					writeD(buf, 0);//apBonus2
				}

				writeC(buf, member.getPlayerClass().getClassId());//playerClass
				writeC(buf, 0);//unk
				writeS(buf, member.getName());//playerName

				int spaces = (member.getName().length() * 2) + 2;

				if(spaces < 52)
					writeB(buf, new byte[(52 - spaces)]);

				count++;
			}

			if(count < 6)
				writeB(buf, new byte[80 * (6 - count)]);//spaces

			count = 0;

			for(Player member : asmosGroup.getMembers())
			{
				writeD(buf, member.getObjectId());//playerObjectId
				writeD(buf, member.getAbyssRank().getRank().getId());//playerRank
				writeD(buf, member.getInstancePVPKills());//pvpKills
				writeD(buf, member.getInstanceBalaurKills());//balaurKills
				writeD(buf, member.getInstanceCaptured());//captured
				writeD(buf, member.getInstancePlayerScore());//playerScore

				if(showRank)
				{
					writeD(buf, member.getInstancePlayerAP() + member.getInstancePlayerScore());//apBonus1
					writeD(buf, member.getInstancePlayerAP());//apBonus2
				}else{
					writeD(buf, 0);//apBonus1
					writeD(buf, 0);//apBonus2
				}

				writeC(buf, member.getPlayerClass().getClassId());//playerClass
				writeC(buf, 0);//unk
				writeS(buf, member.getName());//playerName

				int spaces = (member.getName().length() * 2) + 2;

				if(spaces < 52)
					writeB(buf, new byte[(52 - spaces)]);

				count++;
			}

			if(count < 6)
				writeB(buf, new byte[80 * (6 - count)]);//spaces

			int elyosScore = DredgionInstanceService.getInstance().getGroupScore(elyosGroup);
			int asmosScore = DredgionInstanceService.getInstance().getGroupScore(asmosGroup);

			if(showRank)
			{
				if(asmosScore > elyosScore)
					writeD(buf, 1);
				else
					writeD(buf, 0);
			}else{
				writeD(buf, 255);
			}

			writeD(buf, elyosScore);//elyos score
			writeD(buf, asmosScore);//asmos score

			for(int x = 1; x <= 12;x++)
				writeC(buf, 0xFF);

			writeH(buf, 0);//unk
			
		}else{
			writeD(buf, mapId);
			writeD(buf, instanceTime); // unknown
			writeD(buf, stopTime); // unknown
			writeD(buf, totalPoints); // 0, 1, 2
			writeD(buf, points);
			writeD(buf, kills);
			writeD(buf, rank);
		}
	}
}