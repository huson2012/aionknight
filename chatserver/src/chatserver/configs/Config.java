/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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