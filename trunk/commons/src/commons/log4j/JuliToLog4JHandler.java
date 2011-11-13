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