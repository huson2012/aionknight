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

import commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LoginServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Ban extends AdminCommand
{
	public Ban()
	{
		super("ban");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_BAN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //ban <player name> <account | ip | full> <time in minutes>");
			return;
		}

		// We need to get player's account ID
		String name = Util.convertName(params[0]);
		int accountId = 0;

		// First, try to find player in the World
		Player player = World.getInstance().findPlayer(name);
		if (player != null)
			accountId = player.getClientConnection().getAccount().getId();

		// Second, try to get account ID of offline player from database
		if (accountId == 0)
			accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(name);

		// Third, fail
		if (accountId == 0)
		{
			PacketSendUtility.sendMessage(admin, "Player " + name + " was not found!");
			PacketSendUtility.sendMessage(admin, "Syntax: //ban <player name> <account | ip | full> <time in minutes>");
			return;
		}

		byte type = 3; // Default: full
		if (params.length > 1)
		{
			// Smart Matching
			String stype = params[1].toLowerCase();
			if (("account").startsWith(stype))
				type = 1;
			else if (("ip").startsWith(stype))
				type = 2;
			else if (("full").startsWith(stype))
				type = 3;
			else
			{
				PacketSendUtility.sendMessage(admin, "Syntax: //ban <player name> <account | ip | full> <time in minutes>");
				return;
			}
		}

		int time = 0; // Default: infinity
		if (params.length > 2)
		{
			try
			{
				time = Integer.parseInt(params[2]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Syntax: //ban <player name> <account | ip | full> <time in minutes>");
				return;
			}
		}

		LoginServer.getInstance().sendBanPacket(type, accountId, "", time, admin.getObjectId());
	}
}
