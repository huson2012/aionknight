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
import gameserver.model.legion.LegionHistory;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LEGION_TABS;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.Collection;

public class CM_LEGION_TABS extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_LEGION_TABS.class);

	/**
	 * exOpcode and the rest
	 */
	private int					page;
	private int					tab;

	/**
	 * Constructs new instance of CM_LEGION packet
	 * 
	 * @param opcode
	 */
	public CM_LEGION_TABS(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		page = readD();
		tab = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		Collection<LegionHistory> history = activePlayer.getLegion().getLegionHistory();

		/**
		 * Max page is 3 for legion history
		 */
		if(page > 3)
			return;

		/**
		 * If history size is less than page*8 return
		 */
		if(history.size() < page * 8)
			return;

		switch(tab)
		{
			/**
			 * History Tab
			 */
			case 0:
				log.debug("Requested History Tab Page: " + page);
				if(!history.isEmpty())
					PacketSendUtility.sendPacket(activePlayer, new SM_LEGION_TABS(history, page));
				break;
			/**
			 * Reward Tab
			 */
			case 1:
				log.debug("Requested Reward Tab Page: " + page);
				break;
		}
	}
}