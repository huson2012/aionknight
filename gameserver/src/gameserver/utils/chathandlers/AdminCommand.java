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


package gameserver.utils.chathandlers;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.world.World;

/**
 * This is a base class representing admin command. Admin command is sent by player by typing a //command parameters in
 * chat
 */
public abstract class AdminCommand
{
	private final String	commandName;

	protected AdminCommand(String commandName)
	{
		this.commandName = commandName;
	}

	/**
	 * This method is responsible for number of arguments that comman will accept.<br>
	 * <br>
	 * Lets say user types command: <b>//doSomething arg1 arg2 arg3 arg4</b><br>
	 * If this method returns <b>-1</b>, then every arg that is separated by whitespace ( ) will be threatead as command
	 * parameter, example:
	 * <ul>
	 * <li>Command: doSomething</li>
	 * <li>Param: arg1</li>
	 * <li>Param: arg2</li>
	 * <li>Param: arg3</li>
	 * <li>Param: arg4</li>
	 * </ul>
	 * <br>
	 * Let's say this method returns <b>2</b>.<br>
	 * In such case it will be threated as:
	 * <ul>
	 * <li>Command: doSomething</li>
	 * <li>Param: arg1</li>
	 * <li>Param: arg2 arg3 arg4</li>
	 * </ul>
	 * so we will have only two params.<br>
	 *
	 * @return number of params in command
	 */
	public int getSplitSize()
	{
		return -1;
	}

	/**
	 * Returns the name of the command handled by this class.
	 * 
	 * @return command name
	 */
	public String getCommandName()
	{
		return commandName;
	}
	
	public Player parsePlayerParameter (String param, Player admin, String syntax)
	{
		Player player = null;
		World world = World.getInstance();
		int playerObjId = 0;
		boolean byName = false;
		
		if(param!=null)
		{
			try {
				playerObjId = Integer.parseInt(param);
				player = world.findPlayer(playerObjId);
			} catch (NumberFormatException e) {
				player = world.findPlayer(Util.convertName(param));
				byName = true;
			}
		}
		
		if (player == null)
		{
			if (admin.getTarget() == null)
			{
				if (byName)
				{
					PacketSendUtility.sendMessage(admin, "Player "+param+" is not online");
					return null;
				}
				PacketSendUtility.sendMessage(admin, "You must either select the player or indicate player parameter");
				PacketSendUtility.sendMessage(admin, syntax);
				return null;
			}
			else
			{
				VisibleObject adminTarget = admin.getTarget();
				if (adminTarget instanceof Player)
				{
					player = (Player)adminTarget;
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "The selected target is not a player");
					PacketSendUtility.sendMessage(admin, syntax);
					return null;
				}
			}
		}
		return player;
	}

	/**
	 * Execute admin command represented by this class, with a given list of parametrs.
	 * 
	 * @param admin
	 * @param params
	 */
	public abstract void executeCommand(Player admin, String[] params);
}