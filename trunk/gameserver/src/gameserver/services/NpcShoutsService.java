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

package gameserver.services;

import commons.utils.Rnd;
import gameserver.ai.state.AIState;
import gameserver.dataholders.DataManager;
import gameserver.model.ShoutEventType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.npcshouts.Shout;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import java.util.ArrayList;
import java.util.List;

public class NpcShoutsService
{
	public NpcShoutsService()
	{
	}
	
	public boolean hasShouts(int npcId, ShoutEventType type)
	{
		List<Shout> shouts = DataManager.NPC_SHOUTS_DATA.getShoutsForNpc(npcId);
		if(shouts == null)
			return false;
		for(Shout s : shouts)
		{
			if(ShoutEventType.valueOf(s.getEvent()) == type)
				return true;
		}
		return false;
	}
	
	public void handleEvent(Npc owner, Creature target, ShoutEventType eventType)
	{
		if(owner == null || target == null)
			return;
	
		if(!hasShouts(owner.getNpcId(), eventType))
		{
			if(eventType == ShoutEventType.WAYPOINT && hasShouts(owner.getNpcId(), ShoutEventType.IDLE))
				eventType = ShoutEventType.IDLE;
			else
			{
				//log.debug("No shouts for " + owner.getNpcId() + " - " + eventType.name());
				return;
			}
		}
				
		boolean canTalk = false;
		switch (eventType)
		{
			case IDLE:
				canTalk = owner.getAi().getAiState() == AIState.ACTIVE || owner.getAi().getAiState() == AIState.NONE || owner.getAi().getAiState() == AIState.THINKING;
				break;
			default:
				if(eventType == ShoutEventType.WIN)
					canTalk = true;
				else
					if(!target.getLifeStats().isAlreadyDead())
						canTalk = true;
				break;
		}
		
		if(!canTalk)
			return;
		
		if(eventType != ShoutEventType.LEAVE && eventType != ShoutEventType.DIE && eventType != ShoutEventType.SEEUSER && eventType != ShoutEventType.WIN)
			if(!owner.mayShout())
			{
				//log.debug("Shout " + owner.getNpcId() + " - " + eventType.name() + " :: not allowed to shout");
				return;
			}
		
		List<Shout> shouts = new ArrayList<Shout>();
		for(Shout s : DataManager.NPC_SHOUTS_DATA.getShoutsForNpc(owner.getNpcId()))
		{
			if(ShoutEventType.valueOf(s.getEvent()) == eventType)
			{
				shouts.add(s);
			}
		}
		
		int randomShout = Rnd.get(shouts.size());
		Shout shout = shouts.get(randomShout);
		
		Object param = shout.getParam();
		
		if(target instanceof Player)
		{
			Player player = (Player)target;
			if ("username".equals(param))
				param = player.getName();
			else if ("userclass".equals(param))
				param = (240000 + player.getCommonData().getPlayerClass().getClassId()) * 2 + 1;
			else if ("usernation".equals(param)) 
			{
				// don't know what is that
				return;
			}
			else if ("usergender".equals(param))
				param = (902012 + player.getCommonData().getGender().getGenderId()) * 2 + 1;
			else if ("mainslotitem".equals(param))
			{
				Item weapon = player.getEquipment().getMainHandWeapon();
				if (weapon == null)
					return;
				param = weapon.getItemTemplate().getNameId();
			}
		}
		
		owner.shout();
		SM_SYSTEM_MESSAGE message = new SM_SYSTEM_MESSAGE(shout.getMessageId(), true, owner.getObjectId(), param);
		
		PacketSendUtility.broadcastPacket(owner, message, 30);
		
		shouts.clear();
		shouts = null;
		
	}
	
	public synchronized boolean hasAnyShouts(int npcId)
	{
		return DataManager.NPC_SHOUTS_DATA.getShoutsForNpc(npcId) != null;
	}

	public static final NpcShoutsService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final NpcShoutsService instance = new NpcShoutsService();
	}	
}
