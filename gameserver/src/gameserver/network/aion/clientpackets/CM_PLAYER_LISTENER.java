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

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.DredgionInstanceService;
import gameserver.services.EmotionService;
import gameserver.services.HTMLService;
import gameserver.services.TitleService;
import java.util.Calendar;

public class CM_PLAYER_LISTENER extends AionClientPacket
{
	/**
	 * this CM is send every five minutes by client.
	 */
	public CM_PLAYER_LISTENER(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
	}

	/**c
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		if(player == null)
			return;

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

		TitleService.checkPlayerTitles(player);

		if(CustomConfig.RETAIL_EMOTIONS)
			EmotionService.removeExpiredEmotions(player);

		if(CustomConfig.ENABLE_SURVEYS)
			HTMLService.onPlayerLogin(player);

		//send dredgion instance entry
		if(CustomConfig.ENABLE_DREDGION){		
    		if((hour >= 0 && hour <= 1) || (hour >= 12 && hour <= 13) || (hour >= 20 && hour <= 21))
    		{
    			if(!player.getReceiveEntry())
    			{
    				DredgionInstanceService.getInstance().sendDredgionEntry(player);
    				player.setReceiveEntry(true);
    			}
    		}
		}
	}
}
