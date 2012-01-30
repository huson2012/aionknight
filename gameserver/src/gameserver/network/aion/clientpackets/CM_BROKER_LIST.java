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

public class CM_BROKER_LIST extends AionClientPacket
{
	@SuppressWarnings("unused")
	private int brokerId;
	private int sortType;
	private int page;
	private int listMask;
	
	public CM_BROKER_LIST(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		this.brokerId = readD();
		this.sortType = readC(); // 1 - name; 2 - level; 4 - totalPrice; 6 - price for piece
		this.page = readH();
		this.listMask = readH();		
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		BrokerService.getInstance().showRequestedItems(player, listMask, sortType, page);
	}
}