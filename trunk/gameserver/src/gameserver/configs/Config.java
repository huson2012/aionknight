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

package gameserver.configs;

import commons.configuration.ConfigurableProcessor;
import commons.database.DatabaseConfig;
import commons.utils.PropertiesUtils;
import gameserver.GameServer;
import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.*;
import gameserver.configs.network.FloodConfig;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;
import org.apache.log4j.Logger;
import java.util.Properties;

public class Config
{
	protected static final Logger log = Logger.getLogger(Config.class);
	public static void load()
	{
		try
		{
			Properties props = PropertiesUtils.load(GameServer.CONFIGURATION_FILE); 
			ConfigurableProcessor.process(Config.class, props);			
			ConfigurableProcessor.process(AdminConfig.class, props);
			ConfigurableProcessor.process(LegionConfig.class, props);
			ConfigurableProcessor.process(DropConfig.class, props);
			ConfigurableProcessor.process(RateConfig.class, props);
			ConfigurableProcessor.process(CacheConfig.class, props);
			ConfigurableProcessor.process(ShutdownConfig.class, props);
			ConfigurableProcessor.process(TaskManagerConfig.class, props);
			ConfigurableProcessor.process(GroupConfig.class, props);
			ConfigurableProcessor.process(CustomConfig.class, props);
			ConfigurableProcessor.process(EnchantsConfig.class, props);
			ConfigurableProcessor.process(FallDamageConfig.class, props);
			ConfigurableProcessor.process(GSConfig.class, props);
			ConfigurableProcessor.process(NpcMovementConfig.class, props);
			ConfigurableProcessor.process(PeriodicSaveConfig.class, props);
			ConfigurableProcessor.process(PricesConfig.class, props);
			ConfigurableProcessor.process(SiegeConfig.class, props);
			ConfigurableProcessor.process(ThreadConfig.class, props);
			ConfigurableProcessor.process(NetworkConfig.class, props);
			ConfigurableProcessor.process(DatabaseConfig.class, props);
			ConfigurableProcessor.process(HTMLConfig.class, props);
			ConfigurableProcessor.process(FloodConfig.class, props);
			ConfigurableProcessor.process(EventConfig.class, props);
		}
		
		catch(Exception e)
		{
			log.fatal("Can't load gameserver configuration: ", e);
			throw new Error("Can't load gameserver configuration: ", e);
		}
		IPConfig.load();
	}
}