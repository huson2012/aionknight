/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author jjhun 
 */
public class SM_CMOTION extends AionServerPacket
{
	private CmotionList	cmotionList;
	private byte 		control;
	private int		 	active;
	private int 		cmotionid;
	
	/**
	 * Active and list up
	 *
	 */

	public SM_CMOTION(Player player)
	{
		this.control	= 1;
		this.cmotionList 	= player.getCmotionList();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public SM_CMOTION(Player player, int active, int id)
	{
		this.cmotionid  = id;
		this.active 	= active;
		this.control	= 2;
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
