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

import gameserver.model.gameobjects.AionObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_PLAYER_ID extends AionServerPacket
{
    private AionObject playerAionObject;
    private boolean init = false;
    private int instanceId = 0;
    private int remainingTime = 0;

    public SM_PLAYER_ID(AionObject playerAionObject)
    {
        this.playerAionObject = playerAionObject;
    }
    
    public SM_PLAYER_ID(AionObject playerAionObject, boolean init)
    {
    	this.playerAionObject = playerAionObject;
    	this.init = init;
    }
    
    public SM_PLAYER_ID(AionObject playerAionObject, int id, int time)
    {
    	this.playerAionObject = playerAionObject;
    	this.instanceId = id;
    	this.remainingTime = time;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(!init)
		{
			writeH(buf, 0x2);
	        writeD(buf, 0x0);
	        writeH(buf, 0x1);
	        writeD(buf, playerAionObject.getObjectId());
	        if(instanceId != 0 && remainingTime != 0)
	        {
	        	writeH(buf, 0x1); //instance info or not
	        	writeD(buf, instanceId); //instance ID
	        	writeD(buf, 0x0); //unk
	        	writeD(buf, remainingTime); //remaingTime in seconds
	        	writeH(buf, 0x0); //unk
	        	writeS(buf, playerAionObject.getName());
	        }
	        else
	        {
	        	writeH(buf, 0x0); //instance info or not
	        	writeS(buf, playerAionObject.getName());
	        }
		}
		else
		{
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
		}
	}
}