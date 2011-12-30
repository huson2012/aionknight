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
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

import java.util.Collection;

public class Who extends AdminCommand
{
	public Who()
	{
		super("who");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_WHO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		Collection<Player> players = World.getInstance().getPlayers();

		PacketSendUtility.sendMessage(admin, "List players :");

		for(Player player : players)
		{
			if (params != null && params.length > 0)
			{
				String cmd = params[0].toLowerCase();

				if (("ely").startsWith(cmd))
				{
					if(player.getCommonData().getRace() == Race.ASMODIANS)
						continue;
				}

				if (("asmo").startsWith(cmd))
				{
					if(player.getCommonData().getRace() == Race.ELYOS)
						continue;
				}

				if (("member").startsWith(cmd) || ("premium").startsWith(cmd))
				{
					if(player.getPlayerAccount().getMembership() == 0)
						continue;
				}
			}			

			PacketSendUtility.sendMessage(admin, "Char: " + player.getName() + " - Race: " +  player.getCommonData().getRace().name() + " - Acc: " +  player.getAcountName());
		}		
	}
}