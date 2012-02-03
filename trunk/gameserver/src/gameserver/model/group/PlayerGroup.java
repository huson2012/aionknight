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

package gameserver.model.group;

import commons.objects.filter.ObjectFilter;
import gameserver.configs.main.GroupConfig;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.*;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import javolution.util.FastMap;
import java.util.Collection;

public class PlayerGroup extends AionObject
{
	private LootGroupRules lootGroupRules = new LootGroupRules();
	private Player groupLeader;
	private FastMap<Integer, Player> groupMembers = new FastMap<Integer, Player>().shared();
	private int RoundRobinNr = 0;
	private int instancePoints = 0;
	private int instanceKills = 0;
	private long instanceStartTime = 0;
	private boolean instanceDisplaycounter = true;

	public PlayerGroup(int groupId, Player groupleader)
	{
		super(groupId);
		this.groupMembers.put(groupleader.getObjectId(), groupleader);
        this.groupLeader = groupleader;
		groupleader.setPlayerGroup(this);
		PacketSendUtility.sendPacket(groupLeader, new SM_GROUP_INFO(this));
	}

	public int getGroupId()
	{
		return this.getObjectId();
	}

	public Player getGroupLeader()
	{
		return groupLeader;
	}

	public void setGroupLeader(Player groupLeader)
	{
		this.groupLeader = groupLeader;
	}

	public void addPlayerToGroup(Player newComer)
	{
		groupMembers.put(newComer.getObjectId(), newComer);
		newComer.setPlayerGroup(this);
		PacketSendUtility.sendPacket(newComer, new SM_GROUP_INFO(this));
		updateGroupUIToEvent(newComer, GroupEvent.ENTER);
	}

	public int getRoundRobinMember(Npc npc)
	{
		if(size() == 0)
			return 0;

		RoundRobinNr = ++RoundRobinNr % size();
		int i = 0;
		for(Player player : getMembers())
		{
			if(i == RoundRobinNr)
			{
				if(MathUtil.isIn3dRange(player, npc, GroupConfig.GROUP_MAX_DISTANCE))
				{
					return player.getObjectId();
				}
				else
				{
					return 0;
				}
			}
			i++;
		}
		return 0;
	}

	public void removePlayerFromGroup(Player player)
	{
		this.groupMembers.remove(player.getObjectId());
		player.setPlayerGroup(null);
		updateGroupUIToEvent(player, GroupEvent.LEAVE);
		PacketSendUtility.broadcastPacket(player, new SM_LEAVE_GROUP_MEMBER(), true, new ObjectFilter<Player>(){
			@Override
			public boolean acceptObject(Player object)
			{
				return object.getPlayerGroup() == null;
			}
		});
	}

	public void disband() {
		this.groupMembers.clear();
	}

	public void onGroupMemberLogIn(Player player)
	{
		groupMembers.remove(player.getObjectId());
		groupMembers.put(player.getObjectId(), player);
	}

	public boolean isFull()
	{
		return groupMembers.size() == 6;
	}

	public Collection<Player> getMembers()
	{
		return groupMembers.values();
	}

	public Collection<Integer> getMemberObjIds()
	{
		return groupMembers.keySet();
	}

	public int size()
	{
		return groupMembers.size();
	}

	public LootGroupRules getLootGroupRules()
	{
		return lootGroupRules;
	}

	public void setLootGroupRules(LootGroupRules lgr)
	{
		this.lootGroupRules = lgr;
		for(Player member : groupMembers.values())
			PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
	}

	public void updateGroupUIToEvent(Player subjective, GroupEvent groupEvent)
	{
		switch(groupEvent)
		{
			case CHANGELEADER:
			{
				for(Player member : this.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
					if(subjective.equals(member))
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.CHANGE_GROUP_LEADER());
					PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
				}
			}
				break;
			case LEAVE:
			{
				boolean changeleader = false;
				if(subjective == this.groupLeader)
				{
					this.setGroupLeader(this.getMembers().iterator().next());
					changeleader = true;
				}
				for(Player member : this.getMembers())
				{
					if(changeleader)
					{
						PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.CHANGE_GROUP_LEADER());
					}
					if(!subjective.equals(member))
						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
					if(this.size() > 1)
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.MEMBER_LEFT_GROUP(subjective.getName()));
				}
				eventToSubjective(subjective, GroupEvent.LEAVE);
			}
				break;
			case ENTER:
			{
				eventToSubjective(subjective, GroupEvent.ENTER);
				for(Player member : this.getMembers())
				{
					if(!subjective.equals(member))
					{
						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
						PacketSendUtility.sendPacket(member, new SM_INSTANCE_COOLDOWN(subjective));
					}
					PacketSendUtility.sendPacket(subjective, new SM_INSTANCE_COOLDOWN(member));
				}
			}
				break;
			default:
			{
				for(Player member : this.getMembers())
				{
					if(!subjective.equals(member))
						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
				}
			}
				break;
		}
	}

	private void eventToSubjective(Player subjective, GroupEvent groupEvent)
	{
		for(Player member : getMembers())
		{
			if(!subjective.equals(member))
				PacketSendUtility.sendPacket(subjective, new SM_GROUP_MEMBER_INFO(this, member, groupEvent));
		}
		if(groupEvent == GroupEvent.LEAVE)
			PacketSendUtility.sendPacket(subjective, SM_SYSTEM_MESSAGE.YOU_LEFT_GROUP());
	}
	
	public void setGroupInstancePoints(int points)
	{
		instancePoints = points;
	}
	
	public int getGroupInstancePoints()
	{
		return instancePoints;
	}
	
	public void setInstanceStartTimeNow()
	{
		instanceStartTime = System.currentTimeMillis();
	}
	
	public long getInstanceStartTime()
	{
		return instanceStartTime;
	}

	public void setInstanceKills(int kills)
	{
		instanceKills = kills;
	}

	public int getInstanceKills()
	{
		return instanceKills;
	}

	public void setInstanceDisplaycounter(boolean display)
	{
		instanceDisplaycounter = display;
	}

	public boolean getInstanceDisplaycounter()
	{
		return instanceDisplaycounter;
	}
	
	@Override
	public String getName()
	{
		return "Player Group";
	}
}
