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
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Recall extends AdminCommand
{
	public Recall()
	{
		super("recall");
	}

	@Override
	public void executeCommand(final Player admin, final String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_RECALL)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length == 0 || params.length > 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //recall <ELYOS | ASMODIANS | ALL>");
			return;
		}

		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player player)
			{
				if (params[0].equals("ALL"))
				{
					if (!player.equals(admin))
					{
						TeleportService.teleportTo(player, admin.getWorldId(),
						admin.getInstanceId(), admin.getX(), admin.getY(),
						admin.getZ(), admin.getHeading(), 5);
						PacketSendUtility.sendMessage(player, "Teleported by Admin " + admin.getName() + ".");
					}
				}

				if (params[0].equals("ELYOS"))
				{
					if (!player.equals(admin))
					{
						if (player.getCommonData().getRace() == Race.ELYOS)
						{
							TeleportService.teleportTo(player, admin.getWorldId(),
								admin.getInstanceId(), admin.getX(), admin.getY(),
								admin.getZ(), admin.getHeading(), 5);
							PacketSendUtility.sendMessage(player, "Teleported by Admin " + admin.getName() + ".");
						}
					}
				}

				if (params[0].equals("ASMODIANS"))
				{
					if (!player.equals(admin))
					{
						if (player.getCommonData().getRace() == Race.ASMODIANS)
						{
							TeleportService.teleportTo(player, admin.getWorldId(),
							admin.getInstanceId(), admin.getX(), admin.getY(),
							admin.getZ(), admin.getHeading(), 5);
							PacketSendUtility.sendMessage(player, "Teleported by Admin " + admin.getName() + ".");
						}
					}
				}
				return true;
			}
		}, true);
		
		PacketSendUtility.sendMessage(admin, "Player(s) teleported.");
	}
}