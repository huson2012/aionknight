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
import gameserver.model.trade.TradePSItem;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.PrivateStoreService;

public class CM_PRIVATE_STORE extends AionClientPacket
{

	/**
	 * Private store information
	 */
	private Player			activePlayer;
	private TradePSItem[]	tradePSItems;
	private int				itemCount;

	/**
	 * Constructs new instance of <tt>CM_PRIVATE_STORE </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_PRIVATE_STORE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		/**
		 * Define who wants to create a private store
		 */
		activePlayer = getConnection().getActivePlayer();

		/**
		 * Read the amount of items that need to be put into the player's store
		 */
		itemCount = readH();
		tradePSItems = new TradePSItem[itemCount];
		for(int i = 0; i < itemCount; i++)
		{
			tradePSItems[i] = new TradePSItem(readD(), readD(), readH(), readD());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		/**
		 * Add a check for now if private store is enabled or not
		 */

		/**
		 * Let PrivateStoreService handle everything
		 */
		if(itemCount > 0)
		{
			PrivateStoreService.addItem(activePlayer, tradePSItems);
		}
		else
		{
			PrivateStoreService.closePrivateStore(activePlayer);
		}
	}
}