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

package gameserver.network.aion.clientpackets;

import gameserver.controllers.ReviveType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import org.apache.log4j.Logger;

public class CM_REVIVE extends AionClientPacket
{
	private int reviveId;
	
	/**
	 * Constructs new instance of <tt>CM_REVIVE </tt> packet
	 * @param opcode
	 */
	public CM_REVIVE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		reviveId = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		if (!activePlayer.getLifeStats().isAlreadyDead())
		{
			Logger.getLogger(this.getClass()).info("[AUDIT]Player "+activePlayer.getName()+" sending fake CM_REVIVE: Player is not dead!");
			return;
		}
		
		ReviveType reviveType = ReviveType.getReviveTypeById(reviveId);
		
		switch(reviveType)
		{
			case BIND_REVIVE:
				activePlayer.getReviveController().bindRevive();
				break;
			case REBIRTH_REVIVE:
				activePlayer.getReviveController().rebirthRevive();
				break;
			case ITEM_SELF_REVIVE:
				activePlayer.getReviveController().itemSelfRevive();
				break;
			case SKILL_REVIVE:
				activePlayer.getReviveController().skillRevive(true);
				break;
			case KISK_REVIVE:
				activePlayer.getReviveController().kiskRevive();
				break;
			case INSTANCE_ENTRY:
				activePlayer.getReviveController().instanceEntryRevive();
				break;
			default:
				break;
		}		
	}
}