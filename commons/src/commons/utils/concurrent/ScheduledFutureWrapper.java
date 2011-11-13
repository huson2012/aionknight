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

package commons.utils.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class ScheduledFutureWrapper implements ScheduledFuture<Object>
{
	private final ScheduledFuture<?>	future;

	public ScheduledFutureWrapper(ScheduledFuture<?> future)
	{
		this.future = future;
	}

	@Override
	public long getDelay(TimeUnit unit)
	{
		return future.getDelay(unit);
	}

	@Override
	public int compareTo(Delayed o)
	{
		return future.compareTo(o);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return future.cancel(false);
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException
	{
		return future.get();
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		return future.get(timeout, unit);
	}

	@Override
	public boolean isCancelled()
	{
		return future.isCancelled();
	}

	@Override
	public boolean isDone()
	{
		return future.isDone();
	}
}