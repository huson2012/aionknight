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

import org.apache.log4j.Logger;
import loginserver.controller.AccountController;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

/**
 * 
 * @author nrg
 * @author drsaluml
 * @author Rossdale
 *
 */
public class CM_MAC extends GsClientPacket {
	
	private static final Logger	log	= Logger.getLogger(CM_MAC.class);

	private int accountId;
	private String address;

	public CM_MAC(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x10);
	}
	
	@Override
	protected void readImpl() {
		accountId = readD();
		address = readS();
	}
	
	@Override
	protected void runImpl() {
		if(!AccountController.refreshAccountsLastMac(accountId, address))
			log.error("[WARN] We just weren't able to update account_data.last_mac for accountId "+accountId);
	}
}
