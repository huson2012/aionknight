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

package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

public class SM_ACADEMY_BOOTCAMP_STAGE extends AionServerPacket
{
	private int arenaId;
    private int stage;
    private int round;
    private boolean win;
    
    public SM_ACADEMY_BOOTCAMP_STAGE(int stage, int round, boolean win)
    {
    	int arenaId = 1;
		switch(stage)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				arenaId = 1;
				break;
			case 5:
				arenaId = 2;
				break;
			case 6:
				arenaId = 3;
				break;
			case 7:
				arenaId = 4;
				break;
			case 8:
				arenaId = 5;
				break;				
			case 9:
				arenaId = 6;
				break;
			case 10:
				arenaId = 7;
				break;
			default:
				arenaId = 1;
				break;				
		}
    	
    	this.arenaId = arenaId;
        this.stage = stage;
        this.round = round;
        this.win = win;
    }
    
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, 2);
        writeD(buf, 0);
        
        int stagevalue = (100000 * arenaId) + (1000 * stage) + (win ? 100 : 0) + round;
        writeD(buf, stagevalue);
    }
}