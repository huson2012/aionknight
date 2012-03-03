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

package gameserver.network.aion.clientpackets;

import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.loginserver.BannedMacManager;

import org.apache.log4j.Logger;

/**
 * In this packet client is sending Mac Address - haha.
 */
public class CM_MAC_ADDRESS extends AionClientPacket
{
	private static final Logger                             log         = Logger.getLogger(CM_MAC_ADDRESS.class);
	/**
	 * Mac Addres send by client in the same format as: ipconfig /all [ie: xx-xx-xx-xx-xx-xx]
	 * 
	 */
	@SuppressWarnings("unused")
	private String	macAddress;

	/**
	 * Constructs new instance of <tt>CM_MAC_ADDRESS </tt> packet
	 * @param opcode
	 */
	public CM_MAC_ADDRESS(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
        readC();
        short counter = (short)readH();
        for(short i = 0; i < counter; i++)
        readD();
		macAddress = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
	   if(BannedMacManager.getInstance().isBanned(macAddress)) {
	           AionConnection client = getConnection();
	            //TODO some information packets
	            log.info("[MAC_AUDIT] "+macAddress+" ("+this.getConnection().getIP()+") was kicked due to mac ban");
	            client.close(false);
	        }
	        else
	            this.getConnection().setMacAddress(macAddress);
	}
}
