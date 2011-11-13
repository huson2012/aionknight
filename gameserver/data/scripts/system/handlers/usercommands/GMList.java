/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package usercommands;

import java.util.ArrayList;
import java.util.List;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.world.World;

public class GMList extends UserCommand
{
	public GMList ()
	{
		super("gmlist");
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
			PacketSendUtility.sendMessage(player, admins.size() + " GM(s) online :");
			for(Player a : admins)
			{
				PacketSendUtility.sendMessage(player, a.getName());
			}
		}
		else
		PacketSendUtility.sendMessage(player, "No GM is online currently.");
	}
}