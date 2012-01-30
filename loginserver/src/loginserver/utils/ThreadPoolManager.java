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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import commons.network.DisconnectionTask;
import commons.network.DisconnectionThreadPool;

public class ThreadPoolManager implements DisconnectionThreadPool
{
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(ThreadPoolManager.class);

    /**
     * Instance of ThreadPoolManager
     */
    private static final ThreadPoolManager instance = new ThreadPoolManager();

    /**
     * STPE for normal scheduled tasks
     */
    private final ScheduledThreadPoolExecutor scheduledThreadPool;
    /**
     * STPE for disconnection tasks
     */
    private final ScheduledThreadPoolExecutor disconnectionScheduledThreadPool;
    /**
     * TPE for execution of gameserver client packets
     */
    private final ThreadPoolExecutor gameServerPacketsThreadPool;

    /**
     * @return ThreadPoolManager instance.
     */
    public static ThreadPoolManager getInstance()
    {
        return instance;
    }

    /**
     * Constructor.
     */
    private ThreadPoolManager()
    {
        scheduledThreadPool = new ScheduledThreadPoolExecutor(4, new PriorityThreadFactory("ScheduledThreadPool", Thread.NORM_PRIORITY));
        //scheduledThreadPool.setRemoveOnCancelPolicy(true);

        disconnectionScheduledThreadPool = new ScheduledThreadPoolExecutor(4, new PriorityThreadFactory("ScheduledThreadPool", Thread.NORM_PRIORITY));
        //disconnectionScheduledThreadPool.setRemoveOnCancelPolicy(true);

        gameServerPacketsThreadPool = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("GS Packet Pool", Thread.NORM_PRIORITY + 3));

    }

    /**
     * Schedule
     *
     * @param <T>
     * @param r
     * @param delay
     * @return ScheduledFuture
     */
    @SuppressWarnings("Unchecked")
    public <T extends Runnable> ScheduledFuture<T> schedule(T r, long delay)
    {
        try
        {
            if (delay < 0)
            {
                delay = 0;
            }
            return (ScheduledFuture<T>) scheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException e)
        {
            return null; /** shutdown, ignore */
        }
    }

    /**
     * Schedule at fixed rate
     *
     * @param <T>
     * @param r
     * @param initial
     * @param delay
     * @return ScheduledFuture
     */
    @SuppressWarnings("Unchecked")
    public <T extends Runnable> ScheduledFuture<T> scheduleAtFixedRate(T r, long initial, long delay)
    {
        try
        {
            if (delay < 0)
            {
                delay = 0;
            }
            if (initial < 0)
            {
                initial = 0;
            }
            return (ScheduledFuture<T>) scheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException e)
        {
            return null;
        }
    }

    /**
     * Executes Runnable - GameServer Client packet.
     *
     * @param pkt
     */
    public void executeGsPacket(Runnable pkt)
    {
        gameServerPacketsThreadPool.execute(pkt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void scheduleDisconnection(DisconnectionTask dt, long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        scheduledThreadPool.schedule(dt, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitForDisconnectionTasks()
    {
        try
        {
            disconnectionScheduledThreadPool.shutdown();
            disconnectionScheduledThreadPool.awaitTermination(6, TimeUnit.MINUTES);
        }
        catch (Exception e)
        {
            log.warn("[!] Error in ScheduledThreadPool", e);
        }
    }

    /**
     * PriorityThreadFactory creating new threads for ThreadPoolManager
     */
    private static class PriorityThreadFactory implements ThreadFactory
    {
        /**
         * Priority of new threads
         */
        private final int prio;
        /**
         * Thread group name
         */
        private final String name;
        /**
         * Number of created threads
         */
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        /**
         * ThreadGroup for created threads
         */
        private final ThreadGroup group;

        /**
         * Constructor.
         *
         * @param name
         * @param prio
         */
        public PriorityThreadFactory(String name, int prio)
        {
            this.prio = prio;
            this.name = name;
            group = new ThreadGroup(this.name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(group, r);
            t.setName(name + "-" + threadNumber.getAndIncrement());
            t.setPriority(prio);
            t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
            return t;
        }
    }

    /**
     * Shutdown all thread pools.
     */
    public void shutdown()
    {
        try
        {
            scheduledThreadPool.shutdown();
            gameServerPacketsThreadPool.shutdown();
            scheduledThreadPool.awaitTermination(2, TimeUnit.SECONDS);
            gameServerPacketsThreadPool.awaitTermination(2, TimeUnit.SECONDS);
            log.info("All ThreadPools are now stopped");
        }
        catch (InterruptedException e)
        {
            log.error("[!] Can't shutdown ThreadPoolManager", e);
        }
    }
}