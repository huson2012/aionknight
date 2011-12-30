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
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class WorldUnban extends AdminCommand
{
	public WorldUnban()
	{
		super("wunban");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		String syntax = "Syntax: //wunban <player>";

		if (!CustomConfig.CHANNEL_ALL_ENABLED)
		{
			PacketSendUtility.sendMessage(admin, "<There is no such admin command: " + getCommandName() + ">");
			return;
		}

		if (admin.getAccessLevel() < AdminConfig.COMMAND_WORLDBAN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		String param = null;
		if (params.length>=1)
		{
			param = params[0];
		}
		
		Player player = parsePlayerParameter(param, admin, syntax);
		if (player==null)
		{
			return;
		}

		if (!player.isBannedFromWorld())
		{
			PacketSendUtility.sendMessage(admin, "Player "+player.getName()+" is not banned from the chat channels");
		}
		else
		{
			player.unbanFromWorld();
			PacketSendUtility.sendSysMessage(player, "You are no longer banned from the chat channels");
			PacketSendUtility.sendMessage(admin, "Player "+player.getName()+" is not banned from the chat channels");
		}
	}
}