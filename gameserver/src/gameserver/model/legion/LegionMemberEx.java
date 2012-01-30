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

package gameserver.model.legion;

import gameserver.dataholders.DataManager;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;
import java.sql.Timestamp;

public class LegionMemberEx extends LegionMember
{
	private static Logger log = Logger.getLogger(LegionMemberEx.class);

	private String name;
	private PlayerClass	playerClass;
	private int level;
	private Timestamp lastOnline;
	private int worldId;
	private boolean online = false;

	public LegionMemberEx(Player player, LegionMember legionMember, boolean online)
	{
		super(player.getObjectId(), legionMember.getLegion(), legionMember.getRank());
		this.nickname = legionMember.getNickname();
		this.selfIntro = legionMember.getSelfIntro();
		this.name = player.getName();
		this.playerClass = player.getPlayerClass();
		this.level = player.getLevel();
		this.lastOnline = player.getCommonData().getLastOnline();
		this.worldId = player.getPosition().getMapId();
		this.online = online;
	}

	public LegionMemberEx(int playerObjId)
	{
		super(playerObjId);
	}

	public LegionMemberEx(String name)
	{
		super();
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass)
	{
		this.playerClass = playerClass;
	}

	public int getLastOnline()
	{
		if (lastOnline == null || online)
			return 0;
		return (int) (lastOnline.getTime() / 1000);
	}

	public void setLastOnline(Timestamp timestamp)
	{
		lastOnline = timestamp;
	}

	public int getLevel()
	{
		return level;
	}

	public void setExp(long exp)
	{
		int maxLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel();

		if(playerClass != null && playerClass.isStartingClass())
			maxLevel = 10;

		long maxExp = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(maxLevel);
		int level = 1;

		if(exp > maxExp)
		{
			exp = maxExp;
		}

		while((level + 1) != maxLevel && exp >= DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level + 1))
		{
			level++;
		}

		this.level = level;
	}

	public int getWorldId()
	{
		return worldId;
	}

	public void setWorldId(int worldId)
	{
		this.worldId = worldId;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public boolean isOnline()
	{
		return online;
	}

	public boolean sameObjectId(int objectId)
	{
		return getObjectId() == objectId;
	}

	public boolean isValidLegionMemberEx()
	{
		if(getObjectId() < 1)
		{
			log.error("[LegionMemberEx] Player Object ID is empty.");			
		}
		else if (name == null)
		{
			log.error("[LegionMemberEx] Player Name is empty." + getObjectId());
		}
		else if (playerClass == null)
		{
			log.error("[LegionMemberEx] Player Class is empty." + getObjectId());
		}
		else if (level < 1)
		{
			log.error("[LegionMemberEx] Player Level is empty." + getObjectId());
		}
		else if (getLastOnline() == 0)
		{
			log.error("[LegionMemberEx] Last Online is empty." + getObjectId());
		}
		else if (worldId < 1)
		{
			log.error("[LegionMemberEx] World Id is empty." + getObjectId());
		}
		else if (getLegion() == null)
		{
			log.error("[LegionMemberEx] Legion is empty." + getObjectId());
		}
		else if (getRank() == null)
		{
			log.error("[LegionMemberEx] Rank is empty." + getObjectId());
		}
		else if (getNickname() == null)
		{
			log.error("[LegionMemberEx] Nickname is empty." + getObjectId());
		}
		else if (getSelfIntro() == null)
		{
			log.error("[LegionMemberEx] Self Intro is empty." + getObjectId());
		}
		else
		{
			return true;
		}
		return false;
	}
}
