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

package commons.log4j;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;

public class JuliToLog4JHandler extends Handler
{
	@Override
	public void publish(LogRecord record)
	{
		String loggerName = record.getLoggerName();
		if(loggerName == null)
		{
			loggerName = "";
		}

		Logger log = Logger.getLogger(loggerName);
		org.apache.log4j.Level level = toLog4jLevel(record.getLevel());
		// noinspection ThrowableResultOfMethodCallIgnored
		log.log(java.util.logging.Logger.class.getName(), level, record.getMessage(), record.getThrown());
	}
	protected org.apache.log4j.Level toLog4jLevel(Level juliLevel)
	{
		if(Level.OFF.equals(juliLevel))
		{
			return org.apache.log4j.Level.OFF;
		}
		else if(Level.SEVERE.equals(juliLevel))
		{
			return org.apache.log4j.Level.ERROR;
		}
		else if(Level.WARNING.equals(juliLevel))
		{
			return org.apache.log4j.Level.WARN;
		}
		else if(Level.INFO.equals(juliLevel))
		{
			return org.apache.log4j.Level.INFO;
		}
		else if(Level.CONFIG.equals(juliLevel) || Level.FINE.equals(juliLevel))
		{
			return org.apache.log4j.Level.DEBUG;
		}
		else if(Level.FINER.equals(juliLevel) || Level.FINEST.equals(juliLevel))
		{
			return org.apache.log4j.Level.TRACE;
		}
		else if(Level.ALL.equals(juliLevel))
		{
			return org.apache.log4j.Level.ALL;
		}
		else
		{
			LogLog.warn("Warning: usage of custom JULI level: " + juliLevel.getName(), new Exception());
			return new CustomLog4jLevel(juliLevel.intValue(), juliLevel.getName(), juliLevel.intValue());
		}
	}

	@Override
	public void flush()
	{
	
	}

	@Override
	public void close() throws SecurityException
	{

	}

	protected static class CustomLog4jLevel extends org.apache.log4j.Level
	{

		private static final long	serialVersionUID	= 4014557380173323844L;

		protected CustomLog4jLevel(int level, String levelStr, int syslogEquivalent)
		{
			super(level, levelStr, syslogEquivalent);
		}
	}
}