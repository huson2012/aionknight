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

package commons.services;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.xml.DOMConfigurator;
import commons.log4j.JuliToLog4JHandler;
import commons.log4j.ThrowableAsMessageAwareFactory;
import commons.log4j.exceptions.Log4jInitializationError;

/**
 * This class represents simple wrapper for loggers that initializes logging system.
 * <p/>
 * 
 * Default {@link org.apache.log4j.spi.LoggerFactory} can by configured by system property
 * {@value #LOGGER_FACTORY_CLASS_PROPERTY}
 * <p/>
 * 
 * Default logger factory is {@link commons.log4j.ThrowableAsMessageAwareFactory}
 */
public class LoggingService
{
	/**
	 * Property that represents {@link org.apache.log4j.spi.LoggerFactory} class
	 */
	public static final String	LOGGER_FACTORY_CLASS_PROPERTY	= "log4j.loggerfactory";

	/**
	 * Default log4j configuration file
	 */
	public static final String	LOGGER_CONFIG_FILE				= "config/log4j.xml";

	/**
	 * Is Logging initialized or not?
	 */
	private static boolean		initialized;

	/**
	 * Initializes logging system with {@link #LOGGER_CONFIG_FILE default} config file
	 * 
	 * @throws commons.log4j.exceptions.Log4jInitializationError
	 *            if can't initialize logging
	 */
	public static void init() throws Log4jInitializationError
	{
		File f = new File(LOGGER_CONFIG_FILE);

		if(!f.exists())
		{
			throw new Log4jInitializationError("Missing file " + f.getPath());
		}

		try
		{
			init(f.toURI().toURL());
		}
		catch(MalformedURLException e)
		{
			throw new Log4jInitializationError("Can't initalize logging", e);
		}
	}

	/**
	 * Initializes logging system with config file from URL
	 * 
	 * @param url
	 *           config file location
	 * @throws commons.log4j.exceptions.Log4jInitializationError
	 *            if can't initialize logging
	 */
	public static void init(URL url) throws Log4jInitializationError
	{
		synchronized(LoggingService.class)
		{
			if(initialized)
			{
				return;
			}
			else
			{
				initialized = true;
			}
		}

		try
		{
			DOMConfigurator.configure(url);
		}
		catch(Exception e)
		{
			throw new Log4jInitializationError("Can't initialize logging", e);
		}

		overrideDefaultLoggerFactory();

		// Initialize JULI to Log4J bridge
		Logger logger = LogManager.getLogManager().getLogger("");
		for(Handler h : logger.getHandlers())
		{
			logger.removeHandler(h);
		}

		logger.addHandler(new JuliToLog4JHandler());
	}

	/**
	 * This method uses some reflection to hack default log4j log facrory.
	 * <p/>
	 * 
	 * Log4j uses this Hierarchy for loggers that don't have exact name match and element categoryFactory for loggers
	 * with names that matches specified names in log4j.xml.
	 * <p/>
	 * 
	 * See log4j.xml for detailed description of Log4j behaviour.
	 */
	private static void overrideDefaultLoggerFactory()
	{
		// Hack here, we have to overwrite default logger factory
		Hierarchy lr = (Hierarchy) org.apache.log4j.LogManager.getLoggerRepository();
		try
		{
			Field field = lr.getClass().getDeclaredField("defaultFactory");
			field.setAccessible(true);
			String cn = System.getProperty(LOGGER_FACTORY_CLASS_PROPERTY, ThrowableAsMessageAwareFactory.class
				.getName());
			Class<?> c = Class.forName(cn);
			field.set(lr, c.newInstance());
			field.setAccessible(false);
		}
		catch(NoSuchFieldException e)
		{
			// never thrown
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			// never thrown
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			throw new Log4jInitializationError("Can't found log4j logger factory class", e);
		}
		catch(InstantiationException e)
		{
			throw new Log4jInitializationError("Can't instantiate log4j logger factory", e);
		}
	}
}
