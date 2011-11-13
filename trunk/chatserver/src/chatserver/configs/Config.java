/**
 * This file is part of Aion-Knight Dev. Team <http://aion-knight.ru>.
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

package chatserver.configs;

import java.net.InetSocketAddress;
import java.util.Properties;
import org.apache.log4j.Logger;
import commons.configuration.ConfigurableProcessor;
import commons.configuration.Property;
import commons.utils.PropertiesUtils;

public class Config
{
	/**
	 * Логер для этого класса
	 */
	protected static final Logger log = Logger.getLogger(Config.class);

	/**
	 * IP адрес чат-сервера
	 */
	@Property(key = "chatserver.network.client.address", defaultValue = "localhost:10241")
	public static InetSocketAddress	CHAT_ADDRESS;

	/**
	 * IP адрес игрового сервера
	 */
	@Property(key = "chatserver.network.gameserver.address", defaultValue = "localhost:9021")
	public static InetSocketAddress			GAME_ADDRESS;
	
	/**
	 * Пароль для связи чат-сервера с игровым сервером
	 */
	@Property(key = "chatserver.network.gameserver.password", defaultValue = " ")
	public static String GAME_SERVER_PASSWORD;

	/**
	 * Чтение файлов конфигурации чат-сервера
	 */
	public static void load()
	{
		try
		{
			Properties[] props = PropertiesUtils.loadAllFromDirectory("./config");
			ConfigurableProcessor.process(Config.class, props);
		}
		catch (Exception e)
		{
			log.fatal("Can't load chatserver configuration", e);
			throw new Error("Can't load chatserver configuration", e);
		}
	}
}