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

package commons.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorIterator<V> implements Iterator<V>
{
	private Iterator<? extends Iterable<V>>	firstLevelIterator;
	private Iterator<V>						secondLevelIterator;

	public IteratorIterator(Iterable<? extends Iterable<V>> itit)
	{
		this.firstLevelIterator = itit.iterator();
	}

	@Override
	public boolean hasNext()
	{
		if(secondLevelIterator != null && secondLevelIterator.hasNext())
			return true;

		while(firstLevelIterator.hasNext())
		{
			Iterable<V> iterable = firstLevelIterator.next();

			if(iterable != null)
			{
				secondLevelIterator = iterable.iterator();

				if(secondLevelIterator.hasNext())
					return true;
			}
		}
		return false;
	}

	@Override
	public V next()
	{
		if(secondLevelIterator == null || !secondLevelIterator.hasNext())
			throw new NoSuchElementException();
		return secondLevelIterator.next();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("This operation is not supported.");
	}
}