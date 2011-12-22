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

import commons.database.dao.DAOManager;
import gameserver.configs.main.CustomConfig;
import gameserver.dao.AbyssRankDAO;
import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AbyssRankingService
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log		= Logger.getLogger(AbyssRankingService.class);
	private final HashMap<Race, ArrayList<AbyssRankingResult>> inviduals;
	private final HashMap<Race, ArrayList<AbyssRankingResult>> legions;
	
	private long timeofUpdate = 0;
	/**
	 * TODO SYNCHRONIZE WITH LEGION SERVICE!
	 */
	private AbyssRankingService()
	{
		inviduals = new HashMap<Race, ArrayList<AbyssRankingResult>>();
		legions = new HashMap<Race, ArrayList<AbyssRankingResult>>();
		this.load();
	}

	public static AbyssRankingService getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * Reload the Abyss Ranking system
	 */
	public void reload()
	{
		
	}
	
	/**
	 * Load the Abyss Ranking system
	 * Called only on start of server
	 */
	private void load()
	{
		log.info("AbyssRankService: Loaded!");
		
		scheduleUpdate();
	}

	/**
	 * Should be called only from load()
	 */
	private void scheduleUpdate()
	{
		String[] time = CustomConfig.TOP_RANKING_TIME.split(":");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
		calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		calendar.set(Calendar.SECOND, Integer.parseInt(time[2]));
				
		long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
		
		final Executor<Player> playerUpdateRanking = new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				getDAO().loadAbyssRank(p);
				PacketSendUtility.sendPacket(p, new SM_ABYSS_RANK(p.getAbyssRank()));
				return true;
			}
		};
		
		final Executor<Player> playerSaveToDB = new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				//reset daily/weekly kills/ap
				p.getAbyssRank().doUpdate();
				//saves to db
				getDAO().storeAbyssRank(p);
				return true;
			}
		};
		
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				//save to db
				World.getInstance().doOnAllPlayers(playerSaveToDB);
				
				//update rankings
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						getDAO().updatePlayerRanking();
						getDAO().updateLegionRanking();
					}
				}, 15000);
				
				//update players in-game
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						World.getInstance().doOnAllPlayers(playerUpdateRanking);
						reloadRankings();
					}
				}, 30000);
			}
		}, delay, CustomConfig.TOP_RANKING_DELAY * 60 * 60 * 1000);
	}
	
	private void reloadRankings()
	{
		inviduals.clear();
		legions.clear();
		inviduals.put(Race.ELYOS,getDAO().getAbyssRankingPlayers(Race.ELYOS));
		inviduals.put(Race.ASMODIANS,getDAO().getAbyssRankingPlayers(Race.ASMODIANS));
		legions.put(Race.ASMODIANS,getDAO().getAbyssRankingLegions(Race.ASMODIANS));
		legions.put(Race.ELYOS,getDAO().getAbyssRankingLegions(Race.ELYOS));

		timeofUpdate = System.currentTimeMillis();
		
		log.info("AbyssRankService: Rankings were reloaded!");
	}
	
	/**
	 * Returns Inviduals and Legions Rankings
	 * 
	 * @param race
	 * @return
	 */
	public ArrayList<AbyssRankingResult> getInviduals(Race race)
	{
		return inviduals.get(race);
	}
	public ArrayList<AbyssRankingResult> getLegions(Race race)
	{
		return legions.get(race);
	}
	public long getTimeOfUpdate()
	{
		return timeofUpdate;
	}
	/**
	 * Retuns {@link gameserver.dao.AbyssRankDAO} , just a shortcut
	 * 
	 * @return {@link gameserver.dao.AbyssRankDAO}
	 */
	private AbyssRankDAO getDAO()
	{
		return DAOManager.getDAO(AbyssRankDAO.class);
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AbyssRankingService instance = new AbyssRankingService();
	}
}