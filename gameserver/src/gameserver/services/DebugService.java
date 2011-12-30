/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
	private static final int ANALYZE_PLAYERS_INTERVAL = 30 * 60 * 1000;
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