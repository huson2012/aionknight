package org.openaion.gameserver.network.aion.serverpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.AcademyBootcampService;
import org.openaion.gameserver.services.DredgionInstanceService;

import java.nio.ByteBuffer;

import javolution.util.FastMap;


/**
 * 
 * @author Dns, ginho1,xtreme007, kosyachok
 * 
 */
public class SM_INSTANCE_SCORE extends AionServerPacket
{
	private int		mapId;
	private int		instanceTime;
	private int		stopTime;
	private int		totalPoints;
	private int		points;
	private int		kills;
	private int		rank;
	private PlayerGroup	elyosGroup;
	private PlayerGroup	asmosGroup;
	private boolean		showRank;
	private int[] playersInsigmas;
	private FastMap<Integer, Integer> arenaPoints;

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
	
	/**
	 * Academy bootcamp counter
	 */
	public SM_INSTANCE_SCORE(int mapId, FastMap<Integer, Integer> points, PlayerGroup group, int[] playersInsigmas, boolean showRank)
	{
		this.mapId = mapId;
		this.elyosGroup = group;
		this.showRank = showRank;
		this.playersInsigmas = playersInsigmas;
		this.instanceTime = 0;
		this.arenaPoints = points;
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

		}
		else if(AcademyBootcampService.isAcademyBootcamp(mapId))
		{
			writeD(buf, mapId);
			writeD(buf, instanceTime);

			if(showRank)
				writeD(buf, 3145728);
			else
				writeD(buf, 2097152);
			int i = 0;
			for(Player member : elyosGroup.getMembers())
			{
				writeD(buf, member.getObjectId());//playerObjectId
				writeD(buf, arenaPoints.get(member.getObjectId()) == null ? 0 : arenaPoints.get(member.getObjectId()));
				writeD(buf, 0);
				writeD(buf, playersInsigmas[i]);
				i++;
			}
			
			if(elyosGroup.size() < 6)
				writeB(buf, new byte[16 * (6 - elyosGroup.size())]);//spaces
		}
		else
		{
			writeD(buf, mapId);
			writeD(buf, instanceTime); // instanceTime
			writeD(buf, stopTime); // stopTime
			writeD(buf, totalPoints); // 0, 1, 2
			writeD(buf, points);
			writeD(buf, kills);
			writeD(buf, rank);
		}
	}
}
