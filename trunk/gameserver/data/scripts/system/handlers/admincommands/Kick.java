/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package admincommands;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.Util;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;
import ru.aionknight.gameserver.world.World;

/**
 * @author Elusive
 *
 */

public class Kick extends AdminCommand
{

	public Kick()
	{
		super("kick");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_KICK)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		if (params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //kick <player name>");
			return;
		}

		Player player = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (player == null)
		{
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		player.getClientConnection().close(new SM_QUIT_RESPONSE(), true);
		PacketSendUtility.sendMessage(admin, "You kicked player " + player.getName() + " from the game");
	}
}
