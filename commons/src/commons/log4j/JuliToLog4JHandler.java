/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
 *
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������)
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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