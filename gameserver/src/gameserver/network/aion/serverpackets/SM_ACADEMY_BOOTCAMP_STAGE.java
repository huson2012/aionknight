/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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

package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

/**
 * @author kosyachok, Pashatr
 *
 */
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