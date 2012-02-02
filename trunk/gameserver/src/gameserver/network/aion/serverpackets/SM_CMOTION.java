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

import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CMOTION extends AionServerPacket
{
	private CmotionList	cmotionList;
	private byte control;
	private int	active;
	private int cmotionid;
	
	public SM_CMOTION(Player player)
	{
		this.control = 1;
		this.cmotionList = player.getCmotionList();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public SM_CMOTION(int active, int id)
	{
		this.cmotionid = id;
		this.active = active;
		this.control = 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		switch (control)
		{
			case 1:
			{
				writeC(buf, 0x01); 
				writeH(buf, 0x09);
				for(Cmotion cmotion : cmotionList.getCmotions())
				{  
					writeC(buf, cmotion.getCmotionId());
					writeD(buf, 0);
					writeH(buf, 0);				
				}
			}
			break;
			case 2:
			{
				writeC(buf, 0x05); 
				writeH(buf, cmotionid);
				writeC(buf, active);
			}
			break;
		}
	}
}
