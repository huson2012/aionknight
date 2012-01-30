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

package gameserver.network.loginserver.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.rates.Rates;
import gameserver.world.World;

public class CM_LS_CONTROL_RESPONSE extends LsClientPacket
{
	private int	type;
	private boolean result;
	private String playerName;
	private byte param;
	private String adminName;
	private int	accountId;

	public CM_LS_CONTROL_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readC();
		result = readC() == 1;
		adminName = readS();
		playerName = readS();
		param = (byte) readC();
		accountId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		World world = World.getInstance();
		Player admin = world.findPlayer(Util.convertName(adminName));
		Player player = world.findPlayer(Util.convertName(playerName));
		LoginServer.getInstance().accountUpdate(accountId, param, type);
		switch (type)
		{
			case 1:
				if(!result)
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, playerName + " access level has been updated to " + param );
				   if(player != null)
				   {
				      PacketSendUtility.sendMessage(player, "Your access level have been updated to " + param + " by " + adminName);
				   }
				}
				else
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
				}
			break;
			case 2:
				if(!result)
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, playerName + " membership has been updated to " + param );
				   if(player != null)
				   {
				      player.setRates(Rates.getRatesFor(param));
				      PacketSendUtility.sendMessage(player, "Your membership have been updated to " + param + " by " + adminName);
				   }
				}
				else
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
				}
			break;
		}
	}
}
