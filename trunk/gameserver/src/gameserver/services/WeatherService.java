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

import commons.utils.Rnd;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_WEATHER;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.MapRegion;
import gameserver.world.World;
import gameserver.world.WorldMap;
import javolution.util.FastMap;

import java.util.*;

/**
 * This service in future should schedule job that is changing weather sometimes in region and probably sends to all
 * players
 */
public class WeatherService
{

	private final long WEATHER_DURATION	= 2 * 60 * 60 * 1000;
	private final long CHECK_INTERVAL = 2 * 60 * 1000;
	private Map<WeatherKey, Integer>	worldWeathers;

	public static final WeatherService getInstance()
	{
		return SingletonHolder.instance;
	}

	private WeatherService()
	{
		worldWeathers = new FastMap<WeatherKey, Integer>().shared();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){
			/**
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run()
			{
				checkWeathersTime();
			}
		}, CHECK_INTERVAL, CHECK_INTERVAL);
	}

	/**
	 * Key class used to store date of key creation (for rolling weather usage)
	 */
	private class WeatherKey
	{
		/**
		 * Date of creation of the weather for this region
		 */
		private Date created;
		/**
		 * Region affected to this type of weather (in worldWeathers map)
		 */
		private WorldMap map;

		/**
		 * Parametered Constructor
		 * 
		 * @param date creation date
		 * @param worldMap map to link
		 */
		public WeatherKey(Date date, WorldMap worldMap)
		{
			this.created = date;
			this.map = worldMap;
		}

		/**
		 * @return the map
		 */
		public WorldMap getMap()
		{
			return map;
		}
		
		@Override
		public int hashCode()
		{
			int hashCode = 0;
			hashCode += 1000000007 * created.hashCode();
			if (map != null)
				hashCode += 1000000009 * map.hashCode();
			return hashCode;
		}
		
		@Override
		public boolean equals(Object that)
		{
		    if (this == that) 
		    	return true;
		    if (!(that instanceof WeatherKey))
		    	return false;
		    WeatherKey other = (WeatherKey)that;
		    return other.created.equals(this.created) && 
		    	(other.map == null && this.map == null || other.map.equals(this.map));
		}

		/**
		 * Returns true if the key is out of date relating to constant WEATHER_DURATION, false
		 * either
		 * 
		 * @return true or false
		 */
		public boolean isOutDated()
		{
			Date now = new Date();
			long nowTime = now.getTime();
			long createdTime = created.getTime();
			long delta = nowTime - createdTime;
			return (delta > WEATHER_DURATION);
		}

	}

	/**
	 * triggered every CHECK_INTERVAL
	 */
	private void checkWeathersTime()
	{
		List<WeatherKey> toBeRefreshed = new ArrayList<WeatherKey>();
		for(Map.Entry<WeatherKey, Integer> keyPair : worldWeathers.entrySet())
		{
			WeatherKey key = keyPair.getKey();
			if(key.isOutDated())
			{
				toBeRefreshed.add(key);
			}
		}
		for(WeatherKey key : toBeRefreshed)
		{
			worldWeathers.remove(key);
			onWeatherChange(key.getMap(), null);
		}
	}

	/**
	 * @return a random WeatherType as an integer (0->8)
	 */
	private int getRandomWeather()
	{
		return Rnd.get(0, 8);
	}

	/**
	 * When a player connects, it loads his weather
	 * 
	 * @param player
	 */
	public void loadWeather(Player player)
	{
		WorldMap worldMap = player.getActiveRegion().getParent().getParent();
		onWeatherChange(worldMap, player);
	}

	/**
	 * Return the correct key from the worldWeathers Map by the worldMap
	 * 
	 * @param map
	 * @return
	 */
	private WeatherKey getKeyFromMapByWorldMap(WorldMap map)
	{
		for(Map.Entry<WeatherKey, Integer> keyPair : worldWeathers.entrySet())
		{
			WeatherKey key = keyPair.getKey();
			if(key.getMap().equals(map))
			{
				return key;
			}
		}
		WeatherKey newKey = new WeatherKey(new Date(), map);
		worldWeathers.put(newKey, getRandomWeather());
		return newKey;
	}

	/**
	 * @param worldMap
	 * @return the WeatherType of the {@link WorldMap} for this session
	 */
	private int getWeatherTypeByRegion(WorldMap worldMap)
	{
		WeatherKey key = getKeyFromMapByWorldMap(worldMap);
		return worldWeathers.get(key);
	}

	/**
	 * Allows server to reinitialize Weathers for all regions
	 */
	public void resetWeather()
	{
		Set<WeatherKey> loadedWeathers = new HashSet<WeatherKey>(worldWeathers.keySet());
		worldWeathers.clear();
		for(WeatherKey key : loadedWeathers)
		{
			onWeatherChange(key.getMap(), null);
		}
	}

	/**
	 * Allows server to change a specific {@link MapRegion}'s WeatherType
	 * 
	 * @param regionId
	 *           the regionId to be changed of WeatherType
	 * @param weatherType
	 *           the new WeatherType
	 */
	public void changeRegionWeather(int regionId, Integer weatherType)
	{
		WorldMap worldMap = World.getInstance().getWorldMap(regionId);
		WeatherKey key = getKeyFromMapByWorldMap(worldMap);
		worldWeathers.put(key, weatherType);
		onWeatherChange(worldMap, null);
	}

	/**
	 * 
	 * triggers the update of weather to all players
	 * 
	 * @param world
	 * @param worldMap
	 * @param player
	 *           if null -> weather is broadcasted to all players in world
	 */
	private void onWeatherChange(final WorldMap worldMap, Player player)
	{
		if(player == null)
		{
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player currentPlayer)
				{
					if(!currentPlayer.isSpawned() || !currentPlayer.isOnline())
						return true;
					
					WorldMap currentPlayerWorldMap = currentPlayer.getActiveRegion().getParent().getParent();
					if(currentPlayerWorldMap.equals(worldMap))
					{
						PacketSendUtility.sendPacket(currentPlayer, new SM_WEATHER(
							getWeatherTypeByRegion(currentPlayerWorldMap)));
					}
					return true;
				}
			});
		}
		else
		{
			PacketSendUtility.sendPacket(player, new SM_WEATHER(getWeatherTypeByRegion(worldMap)));
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final WeatherService instance = new WeatherService();
	}
}
