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
 
package admincommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.items.ItemCooldown;
import gameserver.network.aion.serverpackets.SM_ITEM_COOLDOWN;
import gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class RemoveCd extends AdminCommand
{

	public RemoveCd()
	{
		super("removecd");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_REMOVECD)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		VisibleObject target = admin.getTarget();
		if(target == null)
			target = admin;
		
		if (target instanceof Player)
		{
			Player player = (Player)target;
			List<Integer> delayIds = new ArrayList<Integer>();
			if (player.getSkillCoolDowns() != null)
			{
				for(Entry<Integer, Long> en : player.getSkillCoolDowns().entrySet())
				{
					delayIds.add(en.getKey());
				}
				for (Integer delayId : delayIds)
				{
					player.setSkillCoolDown(delayId, 0);
				}
				delayIds.clear();
				PacketSendUtility.sendPacket(player, new SM_SKILL_COOLDOWN(player.getSkillCoolDowns()));
			}
			if (player.getItemCoolDowns() != null)
			{
				for(Entry<Integer, ItemCooldown> en : player.getItemCoolDowns().entrySet())
				{
					delayIds.add(en.getKey());
				}
				for (Integer delayId : delayIds)
				{
					player.addItemCoolDown(delayId, 0, 0);
				}
				delayIds.clear();
				PacketSendUtility.sendPacket(player, new SM_ITEM_COOLDOWN(player.getItemCoolDowns()));
			}
			if(params.length > 0)
			{
				if(params.length == 1 && params[0].equals("instance")
					&& player.getInstanceCDs() != null && player.getInstanceCDs().size() > 0)
				{
					player.getInstanceCDs().clear();
				}
				else if(params.length == 2 && params[0].equals("instance") && params[1].equals("all"))
				{
					World.getInstance().doOnAllPlayers(new Executor<Player>(){
						@Override
						public boolean run(Player rPlayer)
						{
							if(rPlayer != null && rPlayer.getInstanceCDs() != null && rPlayer.getInstanceCDs().size() > 0)
							{
								rPlayer.getInstanceCDs().clear();
								PacketSendUtility.sendMessage(rPlayer, "Intance CDs were removed by an admin");
							}
							return true;
						}
					});
				}
				else
					PacketSendUtility.sendMessage(player, "Syntax: //removecd instance [all]");
			}
			
			if(player.equals(admin))
				PacketSendUtility.sendMessage(admin, "Your cooldowns were removed");
			else
			{
				PacketSendUtility.sendMessage(admin, "You have removed cooldowns of player: "+player.getName());
				PacketSendUtility.sendMessage(player, "Your cooldowns were removed by admin");
			}
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Only players are allowed as target");
		}
	}
}
