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
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.PunishmentService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import java.util.NoSuchElementException;

public class SendToPrison extends AdminCommand
{

	public SendToPrison()
	{
		super("sprison");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PRISON)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params.length == 0 || params.length != 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //sprison <player name> <time delay>");
			return;
		}

		try
		{
			Player playerToPrison = World.getInstance().findPlayer(Util.convertName(params[0]));
			int delay = Integer.parseInt(params[1]);

			if (playerToPrison != null)
			{
				PunishmentService.setIsInPrison(playerToPrison, true, delay);
				if (CustomConfig.CHANNEL_ALL_ENABLED)
					playerToPrison.banFromWorld(admin.getName(), "you have been sent to prison", 0);
				PacketSendUtility.sendMessage(admin, "Player " + playerToPrison.getName() + " has been sent to prison for "
					+ delay + ".");
			}
		}
		catch (NoSuchElementException nsee)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
		}
		catch (Exception e)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
		}
	}
}