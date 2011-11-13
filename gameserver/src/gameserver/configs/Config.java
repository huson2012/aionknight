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

package gameserver.configs;

import java.util.Properties;
import org.apache.log4j.Logger;
import commons.configuration.ConfigurableProcessor;
import commons.database.DatabaseConfig;
import commons.utils.PropertiesUtils;
import gameserver.GameServer;
import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CacheConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.configs.main.DropConfig;
import gameserver.configs.main.EnchantsConfig;
import gameserver.configs.main.EventConfig;
import gameserver.configs.main.FallDamageConfig;
import gameserver.configs.main.GSConfig;
import gameserver.configs.main.GroupConfig;
import gameserver.configs.main.HTMLConfig;
import gameserver.configs.main.LegionConfig;
import gameserver.configs.main.NpcMovementConfig;
import gameserver.configs.main.PeriodicSaveConfig;
import gameserver.configs.main.PricesConfig;
import gameserver.configs.main.RateConfig;
import gameserver.configs.main.ShutdownConfig;
import gameserver.configs.main.SiegeConfig;
import gameserver.configs.main.TaskManagerConfig;
import gameserver.configs.main.ThreadConfig;
import gameserver.configs.network.FloodConfig;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;

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