/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package gameserver.utils;

import org.apache.log4j.Logger;
import java.lang.management.*;

public class DeadlockDetector implements Runnable
{
	private static final Logger log = Logger.getLogger(DeadlockDetector.class);

	private int checkInterval = 0;
	private static String INDENT = "    ";
	private StringBuilder sb = null;

	public DeadlockDetector(int checkInterval)
	{
		this.checkInterval = checkInterval * 1000; 
	}

	@Override
	public void run() 
	{
		boolean noDeadLocks = true;

		while(noDeadLocks)
		{
			try
			{
				ThreadMXBean bean = ManagementFactory.getThreadMXBean();
				long[] threadIds = bean.findDeadlockedThreads();

				if (threadIds != null) {
					log.error("Deadlock detected!");
					sb = new StringBuilder();
					noDeadLocks = false;

					ThreadInfo[] infos = bean.getThreadInfo(threadIds);
					sb.append("\nTHREAD LOCK INFO: \n");
					for (ThreadInfo threadInfo : infos) 
					{
						printThreadInfo(threadInfo);
						LockInfo[] lockInfos = threadInfo.getLockedSynchronizers();
						MonitorInfo[] monitorInfos = threadInfo.getLockedMonitors();

						printLockInfo(lockInfos);
						printMonitorInfo(threadInfo, monitorInfos);
					}
					
					sb.append("\nTHREAD DUMPS: \n");
					for (ThreadInfo ti : bean.dumpAllThreads(true, true)) 
					{  
						printThreadInfo(ti); 
					}
					log.error(sb.toString());
				}
				Thread.sleep(checkInterval);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}			
		}
	}

	private void printThreadInfo(ThreadInfo threadInfo) 
	{
		printThread(threadInfo);
		sb.append(INDENT + threadInfo.toString() + "\n");
		StackTraceElement[] stacktrace = threadInfo.getStackTrace();
		MonitorInfo[] monitors = threadInfo.getLockedMonitors();

		for (int i = 0; i < stacktrace.length; i++) 
		{
			StackTraceElement ste = stacktrace[i];
			sb.append(INDENT + "at " + ste.toString() + "\n");
			for (MonitorInfo mi : monitors) 
			{
				if (mi.getLockedStackDepth() == i) {
					sb.append(INDENT + "  - locked " + mi + "\n");
				}
			}
		}
	}

	private void printThread(ThreadInfo ti) 
	{
		sb.append("\nPrintThread\n");
		sb.append("\"" + ti.getThreadName() + "\"" + " Id="
				+ ti.getThreadId() + " in " + ti.getThreadState() + "\n");
		if (ti.getLockName() != null) 
		{
			sb.append(" on lock=" + ti.getLockName() + "\n");
		}
		if (ti.isSuspended())
		{
			sb.append(" (suspended)" + "\n");
		}
		if (ti.isInNative()) 
		{
			sb.append(" (running in native)" + "\n");
		}
		if (ti.getLockOwnerName() != null) 
		{
			sb.append(INDENT + " owned by " + ti.getLockOwnerName() + " Id="
					+ ti.getLockOwnerId() + "\n");
		}
	}

	private void printMonitorInfo(ThreadInfo threadInfo, MonitorInfo[] monitorInfos) 
	{
		sb.append(INDENT + "Locked monitors: count = " + monitorInfos.length + "\n");
		for (MonitorInfo monitorInfo : monitorInfos)
		{
			sb.append(INDENT + "  - " + monitorInfo + " locked at " + "\n");
			sb.append(INDENT + "      " + monitorInfo.getLockedStackDepth() + " "
					+ monitorInfo.getLockedStackFrame() + "\n");
		}
	}

	private void printLockInfo(LockInfo[] lockInfos) 
	{
		sb.append(INDENT + "Locked synchronizers: count = " + lockInfos.length + "\n");
		for (LockInfo lockInfo : lockInfos)
		{
			sb.append(INDENT + "  - " + lockInfo + "\n");
		}
	}
}