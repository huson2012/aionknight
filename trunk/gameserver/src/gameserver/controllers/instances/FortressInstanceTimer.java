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

package gameserver.controllers.instances;

import gameserver.controllers.NpcController;
import gameserver.model.NpcType;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.services.InstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

public class FortressInstanceTimer extends NpcController
{
	public static void schedule(final Player player, int timeInSeconds)
	{
		if(!player.getQuestTimerOn() && player.isInInstance())
		{
			// Usual delay is 15 minutes (inf' strat) 10 minutes (strat sup')
			final WorldMapInstance instance = InstanceService.getRegisteredInstance(player.getWorldId(), player.getPlayerGroup().getGroupId());
			instance.setTimerEnd(timeInSeconds);
			
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					instance.doOnAllObjects(new Executor<AionObject>(){
						@Override
						public boolean run(AionObject obj)
						{
							if(obj instanceof Player)
								((Player)obj).setQuestTimerOn(false);
							else if(obj instanceof Npc && ((Npc)obj).getObjectTemplate().getNpcType() == NpcType.CHEST)
								((Npc)obj).getController().delete();
							return true;
						}
					}, true);
				}
			}, timeInSeconds * 1000);
			
			for(Player member : player.getPlayerGroup().getMembers())
			{
				if(!member.getQuestTimerOn() && member.getWorldId() == instance.getMapId() && member.getInstanceId() == instance.getInstanceId())
				{
					member.setQuestTimerOn(true);
					
					// Member.getController().addTask(TaskId.QUEST_TIMER, task);
					PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(4, 0, timeInSeconds));
				}
			}
		}
	}
}