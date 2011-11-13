/**
 * This file is part of Aion-Knight <Aion-Knight.smfnew.com>.
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


import commons.utils.Rnd;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;


/**
 * 
 * @author Rhys2002
 * 
 */
public class CM_CLIENT_COMMAND_ROLL extends AionClientPacket
{
	private int	maxRoll;
	private int roll;
	
	public CM_CLIENT_COMMAND_ROLL(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		maxRoll = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();	
		if (player == null)
			return;
		
		roll = Rnd.get(1, maxRoll);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400126, roll, maxRoll));
		PacketSendUtility.broadcastPacket(player, new SM_SYSTEM_MESSAGE(1400127, player.getName(), roll, maxRoll));
	}
}
