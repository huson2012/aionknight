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