/**
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


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_MOTION;
import ru.aionknight.gameserver.utils.PacketSendUtility;

/**
 * @author Mugen 
 */
public class CM_MOTION extends AionClientPacket
{
	private int motionId;
	private int status;
	
	public CM_MOTION(int opcode)
	{
		super(opcode);
	}

	@Override
    protected void readImpl()
    {
        readC();
        motionId = readH(); // 0~8
        status = readC(); // 1~4
    }

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		switch (motionId)
		{
			case 0:
				switch (status)
				{
					case 1:
						player.setWaitingMotion(motionId);
						break;
					case 2:
						player.setRunningMotion(motionId);
						break;
					case 3:
						player.setJumpingMotion(motionId);
						break;
					case 4:
						player.setRestMotion(motionId);
						break;
				}
			case 1: case 5:
				player.setWaitingMotion(motionId);
				break;
			case 2: case 6:
				player.setRunningMotion(motionId);
				break;
			case 3: case 7:
				player.setJumpingMotion(motionId);
				break;
			case 4: case 8:
				player.setRestMotion(motionId);
				break;
		}

		PacketSendUtility.sendPacket(player, new SM_MOTION(player, motionId, status, false, true));
		PacketSendUtility.broadcastPacket(player, new SM_MOTION(player, false, false));
	}
}
