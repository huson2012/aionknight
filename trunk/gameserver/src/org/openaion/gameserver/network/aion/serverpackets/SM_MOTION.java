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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * @author Mugen 
 */
public class SM_MOTION extends AionServerPacket
{
	private boolean readFirst;
	private int	leranNinja = 0;
	private int	learnHober = 0;
	private int	waitingMotion;
	private int	runningMotion;
	private int	jumpingMotion;
	private int	restMotion;
	private int	motionId;
	private int	status;
	private boolean toSelf;
	private int 	objectId;

	public SM_MOTION(Player player, boolean readFirst)
	{
		this.readFirst = readFirst;
		this.leranNinja = player.getLearnNinja();
		this.learnHober = player.getLearnHober();
	}

	public SM_MOTION(Player player, int motionId, int status, boolean readFirst, boolean toSelf)
	{
		this.readFirst = readFirst;
		this.motionId = motionId;
		this.status = status;
		this.toSelf = toSelf;
	}

	public SM_MOTION(Player player, boolean readFirst, boolean toSelf)
	{
		this.readFirst = readFirst;
		this.toSelf = toSelf;
		this.objectId = player.getObjectId();
		this.waitingMotion = player.getWaitingMotion();
		this.runningMotion = player.getRunningMotion();
		this.jumpingMotion = player.getJumpingMotion();
		this.restMotion = player.getRestMotion();
	}

	protected void writeImpl (AionConnection con, ByteBuffer buf)
    {
		if (readFirst)
		{
	        writeC(buf, 0x01); // 0x01 Learn Motion
	        writeH(buf, 0x08); // Number of Motions
	        int i = 0;
	        int count = 0;

	        if (leranNinja == 1 && learnHober == 1) { i = 1; count = 8; }
			else if (leranNinja == 1 && learnHober == 0) { i = 1; count = 4; }
			else if (leranNinja == 0 && learnHober == 1) { i = 5; count = 8; }
			else { i = 0; count = 0; }

	        for (;i<=count;i++)
			{
		       	writeC(buf, i);
		        writeD(buf, 0x00);
		        writeH(buf, 0x00);
			}
		}
		else
		{
	        if (toSelf)
			{
		        writeC(buf, 0x05); // 0x05 - Update Own Motion Status
	        	writeH(buf, motionId);
		       	writeC(buf, status);
			}
			else
			{
				writeC(buf, 0x07); // 0x07 - Update Motion Status for Other Players to See
				writeD(buf, objectId); // objectID of Player
				writeH(buf, waitingMotion);
				writeH(buf, runningMotion);
				writeH(buf, jumpingMotion);
				writeH(buf, restMotion);
			}
		}
    }
}
