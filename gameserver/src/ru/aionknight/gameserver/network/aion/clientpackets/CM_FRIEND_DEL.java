/*
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
package ru.aionknight.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;

import ru.aionknight.gameserver.model.gameobjects.player.Friend;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.services.SocialService;


/**
 * @author Ben
 *
 */
public class CM_FRIEND_DEL extends AionClientPacket
{

	private String 				targetName;
	private static Logger		log				= Logger.getLogger(CM_FRIEND_DEL.class);
	
	public CM_FRIEND_DEL(int opcode)
	{
		super(opcode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetName = readS();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		
		Player activePlayer = getConnection().getActivePlayer();
		Friend target = activePlayer.getFriendList().getFriend(targetName);
		if (target == null)
		{
			log.warn(activePlayer.getName() + " tried to delete friend " + targetName + " who is not his friend");
			sendPacket(SM_SYSTEM_MESSAGE.BUDDYLIST_NOT_IN_LIST);
		}
		else
		{
			SocialService.deleteFriend(activePlayer, target.getOid());
			
			
		}
		

	}

}
