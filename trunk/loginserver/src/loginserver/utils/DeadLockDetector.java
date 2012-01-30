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

package loginserver.utils;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import org.apache.log4j.Logger;
import commons.utils.ExitCode;

public class DeadLockDetector extends Thread
{
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(DeadLockDetector.class);
    /**
     * What should we do on DeadLock
     */
    public static final byte NOTHING = 0;
    /**
     * What should we do on DeadLock
     */
    public static final byte RESTART = 1;

    /**
     * how often check for deadlocks
     */
    private final int sleepTime;
    /**
     * ThreadMXBean
     */
    private final ThreadMXBean tmx;
    /**
     * What should we do on DeadLock
     */
    private final byte doWhenDL;

    /**
     * Create new DeadLockDetector with given values.
     *
     * @param sleepTime
     * @param doWhenDL
     */
    public DeadLockDetector(int sleepTime, byte doWhenDL)
    {
        super("DeadLockDetector");
        this.sleepTime = sleepTime * 1000;
        this.tmx = ManagementFactory.getThreadMXBean();
        this.doWhenDL = doWhenDL;
    }

    /**
     * Check if there is a DeadLock.
     */
    @Override
    public final void run()
    {
        boolean deadlock = false;
        while (!deadlock)
        {
            try
            {
                long[] ids = tmx.findDeadlockedThreads();

                if (ids != null)
                {
                    /** deadlock found :/ */
                    deadlock = true;
                    ThreadInfo[] tis = tmx.getThreadInfo(ids, true, true);
                    String info = "DeadLock Found!\n";
                    for (ThreadInfo ti : tis)
                    {
                        info += ti.toString();
                    }

                    for (ThreadInfo ti : tis)
                    {
                        LockInfo[] locks = ti.getLockedSynchronizers();
                        MonitorInfo[] monitors = ti.getLockedMonitors();
                        if (locks.length == 0 && monitors.length == 0)
                        {
                            /** this thread is deadlocked but its not guilty */
                            continue;
                        }

                        ThreadInfo dl = ti;
                        info += "Java-level deadlock:\n";
                        info += '\t' + dl.getThreadName() + " is waiting to lock " + dl.getLockInfo().toString() + " which is held by " + dl.getLockOwnerName() + '\n';
                        while ((dl = tmx.getThreadInfo(new long[]{dl.getLockOwnerId()}, true, true)[0]).getThreadId() != ti.getThreadId())
                        {
                            info += '\t' + dl.getThreadName() + " is waiting to lock " + dl.getLockInfo().toString() + " which is held by " + dl.getLockOwnerName() + '\n';
                        }
                    }
                    log.warn(info);

                    if (doWhenDL == RESTART)
                    {
                        System.exit(ExitCode.CODE_RESTART);
                    }
                }
                Thread.sleep(sleepTime);
            }
            catch (Exception e)
            {
                log.warn("DeadLockDetector: " + e, e);
            }
        }
    }
}