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

import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_LS_CHARACTER_COUNT;
import gameserver.services.AccountService;

/**
 * Packet sent by login server to request account characters count
 */
public class CM_LS_REQUEST_CHARACTER_COUNT extends LsClientPacket
{	
	private int	accountId;
	
	/**
	 * @param opcode
	 */
	public CM_LS_REQUEST_CHARACTER_COUNT(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		accountId = readD();
	}

	@Override
	protected void runImpl()
	{
		int characterCount = AccountService.getCharacterCountFor(accountId);
		sendPacket(new SM_LS_CHARACTER_COUNT(accountId, characterCount));
	}
}
