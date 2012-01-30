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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class CM_BAN_RESPONSE extends LsClientPacket
{
	private byte		type;
	private int			accountId;
	private String		ip;
	private int			time;
	private int			adminObjId;
	private boolean		result;

	public CM_BAN_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		this.type = (byte) readC();
		this.accountId = readD();
		this.ip = readS();
		this.time = readD();
		this.adminObjId = readD();
		this.result = readC() == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player admin = World.getInstance().findPlayer(adminObjId);
		
		if (admin == null)
			return;

		if (admin.getAccessLevel() < AdminConfig.COMMAND_BAN)
			return;
		
		// Some messages stuff
		String message;
		if (type == 1 || type == 3)
		{
			if (result)
			{
				if (time < 0)
					message = "Account " + accountId + " was successfully unbanned";
				else if (time == 0)
					message = "Accoun " + accountId + " was successfully banned";
				else
					message = "Account " + accountId + " was successfully banned for " + time + " minutes";
			}
			else
				message = "[!] Error occurred while banning player's account";
			PacketSendUtility.sendMessage(admin, message);
		}
		if (type == 2 || type == 3)
		{
			if (result)
			{
				if (time < 0)
					message = "IP mask " + ip + " was successfully removed from block list";
				else if (time == 0)
					message = "IP mask " + ip + " was successfully added to block list";
				else
					message = "IP mask " + ip + " was successfully added to block list for " + time + " minutes";
			}
			else
				message = "Error occurred while adding IP mask " + ip;
			PacketSendUtility.sendMessage(admin, message);
		}
	}
}
