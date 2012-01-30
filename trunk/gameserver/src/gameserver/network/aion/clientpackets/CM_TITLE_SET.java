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
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.stats.listeners.TitleChangeListener;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_TITLE_LIST;
import gameserver.utils.PacketSendUtility;

public class CM_TITLE_SET extends AionClientPacket 
{
   /**
	* Title id
	*/
	private int titleId;

	/**
	* Constructs new instance of <tt>CM_TITLE_SET </tt> packet
	*
	* @param opcode
	*/
	public CM_TITLE_SET(int opcode)
	{
		super(opcode);
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void readImpl()
	{
		titleId = readC();
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		boolean isValidTitle = false;

		if(titleId != -1)
		{
			//check title exploit
			for(Title title : player.getTitleList().getTitles())
			{
				if(title.getTitleId() == titleId)
				{
					isValidTitle = true;
					break;
				}
			}

			if(!isValidTitle)
				return;
		}

		sendPacket(new SM_TITLE_LIST(titleId));
		PacketSendUtility.broadcastPacket(player, (new SM_TITLE_LIST(player.getObjectId(), titleId)));

		if(player.getCommonData().getTitleId() > 0)
			if (player.getGameStats() != null)
				TitleChangeListener.onTitleChange(player.getGameStats(), player.getCommonData().getTitleId(), false);

		player.getCommonData().setTitleId(titleId);
		if (player.getGameStats() != null)
		{
			TitleChangeListener.onTitleChange(player.getGameStats(), titleId, true);
		}
	}
}
