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
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.FriendList.Status;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class GMOnline extends AdminCommand
{
	public GMOnline()
	{
		super("gmonline");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_GMONLINE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		int GMCount = 0;
		String sGMNames = "";
		for(Player player : World.getInstance().getPlayers())
		{
			if(player.isGM() && !player.isProtectionActive() && player.getFriendList().getStatus() != FriendList.Status.OFFLINE)
			{
				GMCount++;
				sGMNames += player.getName()+" : "+ returnStringStatus(player.getFriendList().getStatus()) +";\n";
			}
		}
		if(GMCount == 0)
		{
			PacketSendUtility.sendMessage(admin, "There is no GM online !");
		}
		else if(GMCount == 1)
		{
			PacketSendUtility.sendMessage(admin, "There is "+String.valueOf( GMCount )+" GM online !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "There are "+String.valueOf( GMCount )+" GMs online !");
		}
		if(GMCount != 0)
			PacketSendUtility.sendMessage(admin, "List : \n"+sGMNames);
	}
	private String returnStringStatus(Status p_status)
	{
		String return_string = "";
		if (p_status == FriendList.Status.ONLINE)
			return_string = "online";
		if (p_status == FriendList.Status.AWAY)
			return_string = "away";
		return return_string;
	}
}