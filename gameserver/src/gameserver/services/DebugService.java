/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.services;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.AionConnection;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class DebugService
{
	private static final Logger	log = Logger.getLogger(DebugService.class);
	private static final int	ANALYZE_PLAYERS_INTERVAL	= 30 * 60 * 1000;
	public static final DebugService getInstance()
	{
		return SingletonHolder.instance;
	}

	private DebugService()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				analyzeWorldPlayers();
			}

		}, ANALYZE_PLAYERS_INTERVAL, ANALYZE_PLAYERS_INTERVAL);
		log.info("[DEBUG SERVICE] Analyze iterval: "+ANALYZE_PLAYERS_INTERVAL);
	}

	private void analyzeWorldPlayers()
	{
		log.info("Analysis of world players: " + System.currentTimeMillis());

		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player player)
			{
				AionConnection connection = player.getClientConnection();
				if(connection == null)
				{
					log.warn(String.format("[DEBUG SERVICE] Player without connection: "
						+ "detected: ObjId %d, Name %s, Spawned %s", player.getObjectId(), player.getName(), player
						.isSpawned()));
					return true;
				}
				long lastPingTimeMS = connection.getLastPingTimeMS();
				long pingInterval = System.currentTimeMillis() - lastPingTimeMS;
				if(lastPingTimeMS > 0 && pingInterval > 300000)
				{
					log.warn(String.format("[DEBUG SERVICE] Player with large ping interval: "
						+ "ObjId %d, Name %s, Spawned %s, PingMS %d", player.getObjectId(), player.getName(), player
						.isSpawned(), pingInterval));
				}
				return true;
			}
		}, true);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DebugService instance = new DebugService();
	}
}