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

package gameserver.services;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_GAME_TIME;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class GameTimeService
{
	private static Logger	log	= Logger.getLogger(GameTimeService.class);
	
	public static final GameTimeService getInstance()
	{
		return SingletonHolder.instance;
	}

	private final static int GAMETIME_UPDATE = 3 * 60000;

	private GameTimeService()
	{
		/**
		 * Update players with current game time
		 */
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				log.debug("Sending current game time to all players");
				World.getInstance().doOnAllPlayers(new Executor<Player>(){
					@Override
					public boolean run(Player object)
					{
						PacketSendUtility.sendPacket(object, new SM_GAME_TIME());
						return true;
					}
				});
			}
		}, GAMETIME_UPDATE, GAMETIME_UPDATE);
		
		log.info("GameTimeService started. Update interval:" + GAMETIME_UPDATE);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GameTimeService instance = new GameTimeService();
	}
}