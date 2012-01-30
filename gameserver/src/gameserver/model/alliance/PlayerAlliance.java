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

package gameserver.model.alliance;

import gameserver.configs.main.GroupConfig;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.LootGroupRules;
import gameserver.network.aion.serverpackets.SM_ALLIANCE_INFO;
import gameserver.network.aion.serverpackets.SM_ALLIANCE_MEMBER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.AllianceService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerAlliance extends AionObject
{
	private int captainObjectId;
	private List<Integer> viceCaptainObjectIds = new ArrayList<Integer>();
	private FastMap<Integer, PlayerAllianceMember> allianceMembers = new FastMap<Integer, PlayerAllianceMember>().shared();
	private FastMap<Integer, PlayerAllianceGroup> allianceGroupForMember = new FastMap<Integer, PlayerAllianceGroup>().shared();
	private FastMap<Integer, PlayerAllianceGroup> allianceGroups = new FastMap<Integer, PlayerAllianceGroup>().shared();
	private LootGroupRules lootGroupRules = new LootGroupRules();
	private int RoundRobinNr = 0;
	public PlayerAlliance(int objectId, int leaderObjectId)
	{
		super(objectId);
		setLeader(leaderObjectId);
	}
	
	public void addMember(Player member)
	{
		PlayerAllianceGroup group = getOpenAllianceGroup();
		PlayerAllianceMember allianceMember = new PlayerAllianceMember(member);
		group.addMember(allianceMember);
		
		allianceMembers.put(member.getObjectId(), allianceMember);
		allianceGroupForMember.put(member.getObjectId(), group);
		
		member.setPlayerAlliance(this);
	}

	private PlayerAllianceGroup getOpenAllianceGroup()
	{
		for (int i = 1000; i <= 1004; i++)
		{
			PlayerAllianceGroup group = allianceGroups.get(i);
			
			if (group == null)
			{
				group = new PlayerAllianceGroup(this);
				group.setAllianceId(i);
				allianceGroups.put(i, group);
				return group;
			}
			
			if (group.getMembers().size() < 6)
				return group;
		}
		throw new RuntimeException("All Alliance Groups Full.");
	}

	public void removeMember(int memberObjectId)
	{
		allianceGroupForMember.get(memberObjectId).removeMember(memberObjectId);
		allianceGroupForMember.remove(memberObjectId);
		allianceMembers.remove(memberObjectId);
		
		if (viceCaptainObjectIds.contains(memberObjectId))
		{
			viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(memberObjectId));
		}
		
		if (memberObjectId == this.captainObjectId)
		{
			if (!viceCaptainObjectIds.isEmpty())
			{
				int newCaptain = viceCaptainObjectIds.get(0);
				viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(newCaptain));
				this.captainObjectId = newCaptain;
			}
			else if (!allianceMembers.isEmpty())
			{
				PlayerAllianceMember newCaptain = allianceMembers.values().iterator().next();
				this.captainObjectId = newCaptain.getObjectId();
			}
		}
		
		AllianceService.getInstance().broadcastAllianceInfo(this, PlayerAllianceEvent.UPDATE);
	}

	public void setLeader(int newLeaderObjectId)
	{
		if (viceCaptainObjectIds.contains(newLeaderObjectId))
		{
			viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(newLeaderObjectId));
			viceCaptainObjectIds.add(this.captainObjectId);
		}
		this.captainObjectId = newLeaderObjectId;
	}

	public void promoteViceLeader(int viceLeaderObjectId)
	{
		viceCaptainObjectIds.add(viceLeaderObjectId);
	}

	public void demoteViceLeader(int viceLeaderObjectId)
	{
		viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(viceLeaderObjectId));
	}

	public PlayerAllianceMember getCaptain()
	{
		return getPlayer(captainObjectId);
	}

	public int getCaptainObjectId()
	{
		return this.captainObjectId;
	}

	public List<Integer> getViceCaptainObjectIds()
	{
		return this.viceCaptainObjectIds;
	}

	public int getAllianceIdFor(int playerObjectId)
	{
		if (!allianceGroupForMember.containsKey(playerObjectId))
			return 0;
		else
			return allianceGroupForMember.get(playerObjectId).getAllianceId();
	}

	public PlayerAllianceMember getPlayer(int playerObjectId)
	{
		return allianceMembers.get(playerObjectId);
	}

	public int size()
	{
		return getMembers().size();
	}

	public boolean isFull()
	{
		return (size() >= 24);
	}

	public Collection<PlayerAllianceMember> getMembers()
	{
		return allianceMembers.values();
	}

	public boolean hasAuthority(int playerObjectId)
	{
		return (playerObjectId == captainObjectId || viceCaptainObjectIds.contains(playerObjectId));
	}

	@Override
	public String getName()
	{
		return "Player Alliance";
	}

	public void swapPlayers(int playerObjectId1, int playerObjectId2)
	{
		PlayerAllianceGroup group1 = allianceGroupForMember.get(playerObjectId1);
		PlayerAllianceGroup group2 = allianceGroupForMember.get(playerObjectId2);
		PlayerAllianceMember player1 = group1.removeMember(playerObjectId1);
		PlayerAllianceMember player2 = group2.removeMember(playerObjectId2);
		group1.addMember(player2);
		group2.addMember(player1);
		allianceGroupForMember.put(playerObjectId1, group2);
		allianceGroupForMember.put(playerObjectId2, group1);
	}

	public void setAllianceGroupFor(int memberObjectId, int allianceGroupId)
	{
		PlayerAllianceGroup leavingGroup = allianceGroupForMember.get(memberObjectId);
		if(leavingGroup == null)
			return;
		PlayerAllianceMember member = leavingGroup.getMemberById(memberObjectId);
		leavingGroup.removeMember(memberObjectId);
		
		PlayerAllianceGroup group = allianceGroups.get(allianceGroupId);
		
		if (group == null)
		{
			group = new PlayerAllianceGroup(this);
			group.setAllianceId(allianceGroupId);
			allianceGroups.put(allianceGroupId, group);
		}
		
		group.addMember(member);
		allianceGroupForMember.put(memberObjectId, group);
	}

	public PlayerAllianceGroup getPlayerAllianceGroupForMember(int objectId)
	{
		return allianceGroupForMember.get(objectId);
	}

	public void onPlayerLogin(Player player)
	{
		allianceMembers.get(player.getObjectId()).onLogin(player);
	}

	public void onPlayerDisconnect(Player player)
	{
		PlayerAllianceMember allianceMember = allianceMembers.get(player.getObjectId());
		allianceMember.onDisconnect();
		
		for(PlayerAllianceMember member : allianceMembers.values())
		{
			if (member.isOnline())
			{
				PacketSendUtility.sendPacket(member.getPlayer(), SM_SYSTEM_MESSAGE.STR_FORCE_HE_BECOME_OFFLINE(player.getName()));
				PacketSendUtility.sendPacket(member.getPlayer(), new SM_ALLIANCE_MEMBER_INFO(allianceMember, PlayerAllianceEvent.DISCONNECTED));
			}
		}
	}

	public Collection<PlayerAllianceMember> getMembersForGroup(int playerObjectId)
	{
		PlayerAllianceGroup group = allianceGroupForMember.get(playerObjectId);
		if (group == null) return (new FastMap<Integer, PlayerAllianceMember>()).values();
		return group.getMembers();
	}

	public LootGroupRules getLootAllianceRules()
	{
		return lootGroupRules;
	}

	public void setLootAllianceRules(LootGroupRules lgr)
	{
		this.lootGroupRules = lgr;
		for(PlayerAllianceMember member : allianceMembers.values())
		{
			Player pl = member.getPlayer();
			PacketSendUtility.sendPacket(pl, new SM_ALLIANCE_INFO(this));
		}
	}

	public int getRoundRobinMember(Npc npc)
	{
		if(size() == 0)
			return 0;

		RoundRobinNr = ++RoundRobinNr % size();
		int i = 0;
		for(PlayerAllianceMember member : allianceMembers.values())
		{
			if(i == RoundRobinNr)
			{
				Player player = member.getPlayer();
				
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
}
