/*
 * This file is part of aion-unique <www.aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.network.aion.clientpackets;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import ru.aionknight.gameserver.services.EmpyreanCrucibleService;
import ru.aionknight.gameserver.services.TeleportService;
import ru.aionknight.gameserver.utils.PacketSendUtility;

/**
 * @author dns
 * Used on dark poeta, when you get rank F, to be teleported outside instance.
 *
 */
public class CM_EXIT_LOCATION extends AionClientPacket
{

	public CM_EXIT_LOCATION(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{

	}

	@Override
	protected void runImpl()
	{	
		final Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer.getInDarkPoeta() || activePlayer.getInDredgion()){
			switch(activePlayer.getCommonData().getRace().getRaceId())
			{
			case 0:
				TeleportService.teleportTo(activePlayer, 110010000, 1, 1444.9f, 1577.2f, 572.9f, 0);
				break;
			case 1:
				TeleportService.teleportTo(activePlayer, 120010000, 1, 1657.5f, 1398.7f, 194.7f, 0);
				break;
			}
		} else if(activePlayer.getInEmpyrean()){
			switch(activePlayer.getCommonData().getRace().getRaceId())
			{
			case 0:
				TeleportService.teleportTo(activePlayer, 110070000, 450, 252, 127, 0); 
				break;
			case 1:
				TeleportService.teleportTo(activePlayer, 120080000, 549, 205, 94, 0);
				break;
			}
			activePlayer.setInEmpyrean(false);
			if(activePlayer.getPlayerGroup().getGroupLeader().getObjectId() == activePlayer.getObjectId())
			activePlayer.getPlayerGroup().getEmpyreanCrucible().groupUnreg();			
		}
		PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_SCORE(0, 14400000, 2097152, 0, 0, 0, 0));
	}
}
