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

package loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import loginserver.controller.BannedMacManager;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

/**
 * 
 * @author KID
 * @author drsaluml
 * @author Rossdale
 *
 */
public class CM_MACBAN_CONTROL  extends GsClientPacket {
	private byte type;
	private int playerId;
	private String address;
	private String details;
	private long time;

	public CM_MACBAN_CONTROL(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x11);
	}
	
	@Override
	protected void readImpl() {
		type = (byte) readC();
		playerId = readD();
		address = readS();
		details = readS();
		time = readQ();
	}
	
	@Override
	protected void runImpl() {
		BannedMacManager bmm = BannedMacManager.getInstance();
		switch(type)
		{
			case 0://unban
				bmm.unban(address, details);
				break;
			case 1://ban
				bmm.ban(address,playerId, time, details);
				break;
		}
	}
}
