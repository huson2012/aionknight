/**
 * This file is part of Aion-Knight <www.Aion-Knight.org>.
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
 
package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import gameserver.services.EmpyreanCrucibleService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;

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