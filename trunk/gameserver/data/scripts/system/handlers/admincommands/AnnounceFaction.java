/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class AnnounceFaction extends AdminCommand
{
	public AnnounceFaction()
	{
		super("announcefaction");
	}

	@Override
	public void executeCommand(Player admin, final String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ANNOUNCE_FACTION)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //announcefaction <ely | asmo> <message>");
		}
		else
		{
			String message = "";

			if (params[0].equals("ely"))
				message += "Elyos : ";
			else
				message += "Asmodians : ";

			// Add with space
			for (int i=1; i<params.length-1; i++)
				message += params[i] + " ";

			// Add the last without the end space
			message += params[params.length-1];

			final String _message = message;
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if (player.getCommonData().getRace() == Race.ELYOS && params[0].equals("ely"))
						PacketSendUtility.sendSysMessage(player, _message);
					else if (player.getCommonData().getRace() == Race.ASMODIANS && params[0].equals("asmo"))
						PacketSendUtility.sendSysMessage(player, _message);
					return true;
				}
			});
		}
	}
}
