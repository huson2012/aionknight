/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.configs;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openaion.commons.configuration.ConfigurableProcessor;
import org.openaion.commons.database.DatabaseConfig;
import org.openaion.commons.utils.PropertiesUtils;

import ru.aionknight.gameserver.GameServer;
import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.configs.main.CacheConfig;
import ru.aionknight.gameserver.configs.main.CustomConfig;
import ru.aionknight.gameserver.configs.main.DropConfig;
import ru.aionknight.gameserver.configs.main.EnchantsConfig;
import ru.aionknight.gameserver.configs.main.EventConfig;
import ru.aionknight.gameserver.configs.main.FallDamageConfig;
import ru.aionknight.gameserver.configs.main.GSConfig;
import ru.aionknight.gameserver.configs.main.GroupConfig;
import ru.aionknight.gameserver.configs.main.HTMLConfig;
import ru.aionknight.gameserver.configs.main.LegionConfig;
import ru.aionknight.gameserver.configs.main.NpcMovementConfig;
import ru.aionknight.gameserver.configs.main.PeriodicSaveConfig;
import ru.aionknight.gameserver.configs.main.PricesConfig;
import ru.aionknight.gameserver.configs.main.RateConfig;
import ru.aionknight.gameserver.configs.main.ShutdownConfig;
import ru.aionknight.gameserver.configs.main.SiegeConfig;
import ru.aionknight.gameserver.configs.main.TaskManagerConfig;
import ru.aionknight.gameserver.configs.main.ThreadConfig;
import ru.aionknight.gameserver.configs.network.FloodConfig;
import ru.aionknight.gameserver.configs.network.IPConfig;
import ru.aionknight.gameserver.configs.network.NetworkConfig;


/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(Config.class);

	/**
	 * Initialize all configs in org.openaion.gameserver.configs package
	 */
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