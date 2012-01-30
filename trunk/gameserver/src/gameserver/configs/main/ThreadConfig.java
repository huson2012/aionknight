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

package gameserver.configs.main;

import commons.configuration.Property;

public class ThreadConfig
{		
	@Property(key = "thread.basepoolsize", defaultValue = "2")
	public static int BASE_THREAD_POOL_SIZE;
	
	@Property(key = "thread.threadpercore", defaultValue = "4")
	public static int EXTRA_THREAD_PER_CORE;
	
	@Property(key = "thread.runtime", defaultValue = "5000")
	public static long MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING;
	
	public static int THREAD_POOL_SIZE;
	
	public static void load()
	{
		final int baseThreadPoolSize = BASE_THREAD_POOL_SIZE;
		final int extraThreadPerCore = EXTRA_THREAD_PER_CORE;

		THREAD_POOL_SIZE = baseThreadPoolSize + Runtime.getRuntime().availableProcessors() * extraThreadPerCore;
	}
}