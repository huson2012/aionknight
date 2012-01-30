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
import java.util.ArrayList;
import java.util.List;

public class CM_BROKER_SEARCH extends AionClientPacket
{
	@SuppressWarnings("unused")
	private int brokerId;
	private int sortType;
	private int page;
	private int mask;
	private int items_length;
	private List<Integer> items_id;

	public CM_BROKER_SEARCH(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		this.brokerId = readD();
		this.sortType = readC(); // 1 - name; 2 - level; 4 - totalPrice; 6 - price for piece
		this.page = readH();
		this.mask = readH();
		this.items_length = readH();
		this.items_id = new ArrayList<Integer>();
		for (int i = 0; i<this.items_length; i++)
			this.items_id.add(readD());
	}

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		BrokerService.getInstance().showRequestedItems(player, mask, sortType, page, items_id, true);
	}
}
