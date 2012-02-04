/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package usercommands;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.world.World;
import java.util.ArrayList;
import java.util.List;

public class GMList extends UserCommand
{
	public GMList ()
	{
		super("gmlist"); // Список присутствующих в игре Гейм-Мастеров
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		final List<Player> admins = new ArrayList<Player>();
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			
			@Override
			public boolean run(Player object)
			{
				if(object.getAccessLevel() > 0)
				{
					admins.add(object);
				}
				return true;
			}
		}, true);
		
		if(admins.size() > 0)
		{
			PacketSendUtility.sendMessage(player, admins.size() + " GM(s) online: ");
			for(Player a : admins)
			{
				PacketSendUtility.sendMessage(player, a.getName());
			}
		}
		else
		PacketSendUtility.sendMessage(player, "No GM is online currently!");
	}
}