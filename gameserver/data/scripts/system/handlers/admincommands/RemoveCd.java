/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
 *
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова)
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */
 
package admincommands;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
