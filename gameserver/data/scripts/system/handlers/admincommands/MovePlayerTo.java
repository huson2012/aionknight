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
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import gameserver.world.WorldMapType;

public class MovePlayerTo extends AdminCommand
{
	public MovePlayerTo()
	{
		super("moveplayerto");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVEPLAYERTO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 5)
		{
			PacketSendUtility.sendMessage(admin, "syntax //moveplayerto <player name> <world Id> <X> <Y> <Z>");
			return;
		}

		Player playerToMove = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (playerToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		
		int worldId;
		float x, y, z;
		
		try
		{
			worldId = Integer.parseInt(params[1]);
			x = Float.parseFloat(params[2]);
			y = Float.parseFloat(params[3]);
			z = Float.parseFloat(params[4]);
		}
		catch(NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "WorldID, x, y and z should be numbers!");
			return;
		}
		
		if (WorldMapType.getWorld(worldId) == null)
		{
			PacketSendUtility.sendMessage(admin, "Illegal WorldId %d " + worldId );
		}
		else
		{
			TeleportService.teleportTo(playerToMove, worldId, x, y, z, 0);
			PacketSendUtility.sendMessage(admin, "Teleported player " + playerToMove.getName() + " to " + x + " " + y + " " + z + " [" + worldId + "]");
			PacketSendUtility.sendMessage(playerToMove, "You have been teleported by an Administrator.");
		}
	}
}