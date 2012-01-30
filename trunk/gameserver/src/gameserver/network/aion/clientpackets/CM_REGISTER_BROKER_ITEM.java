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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.BrokerService;
import org.apache.log4j.Logger;

public class CM_REGISTER_BROKER_ITEM extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_REGISTER_BROKER_ITEM.class);
	@SuppressWarnings("unused")
	private int brokerId;
	private int itemUniqueId;
	private long price;
	private int itemCount;
	
	public CM_REGISTER_BROKER_ITEM(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		this.brokerId = readD();
		this.itemUniqueId = readD();
		this.price = readQ();
		this.itemCount = readH();
	}

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if (price < 1 || itemCount < 1)
		{
			log.warn("[AUDIT] Possible client hack Player: "+player.getName()+" Account name: "+player.getAcountName()+toString());
			return;
		}
		BrokerService.getInstance().registerItem(player, itemUniqueId, price, itemCount);
	}
}