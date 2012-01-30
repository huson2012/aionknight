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

package gameserver.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is representing an iterator, that is used to iterate through the collection that has format
 * Iterable&lt;Iterable&lt;V&gt;&gt;.<br>
 * 
 * <pre>
 * &lt;code&gt;
 * Usage:&lt;br&gt;
 * List&lt;List&lt;Integer&gt;&gt; someList = ....
 * IteratorIterator&lt;Integer&gt; iterator = new IteratorIterator&lt;Integer&gt;(someList)
 * 
 * OR:
 * 
 * Map&lt;Integer, Set&lt;SomeClass&gt;&gt; mapOfSets = ....
 * IteratorIterator&lt;SomeCLass&gt; iterator = new IteratorIterator&lt;SomeClass&gt;(mapsOfSets.values());
 * &lt;/code&gt;
 * </pre>
 * 
 * This iterator is not thread-safe. <br>
 * This iterator omits null values for first level collection, which means that if we have:
 * 
 * <pre>
 * &lt;code&gt;
 * Set&lt;Set&lt;Integer&gt;&gt; setOfSets = ....
 * setOfSets.add(null);
 * setOfSets.add(someSetOfIntegers); // Where someSetsOfIntegers is a set containing 1 and 2
 * 
 * IteratorIterator&lt;Integer&gt; it = new IteratorIterator&lt;Integer&gt;(setOfSets);
 * &lt;/code&gt;
 * </pre>
 * 
 * This <code>it</code> iterator will return only 2 values ( 1 and 2 )
 * 
 * @param <V>
 *           Type of the values over which this iterator iterates
 * 
 */
public class IteratorIterator<V> implements Iterator<V>
{
	/** 1st Level iterator */
	private Iterator<? extends Iterable<V>>	    firstLevelIterator;
	
	/** 2nd level iterator */
	private Iterator<V>						    secondLevelIterator;

	/**
	 * Constructor of <tt>IteratorIterator</tt>
	 * 
	 * @param itit
	 *           an Iterator that iterate over Iterable<Value>
	 */
	public IteratorIterator(Iterable<? extends Iterable<V>> itit)
	{
		this.firstLevelIterator = itit.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{
		if(secondLevelIterator != null && secondLevelIterator.hasNext())
			return true;

		while(firstLevelIterator.hasNext())
		{
			Iterable<V> iterable = firstLevelIterator.next();

			if (iterable != null)
			{
				secondLevelIterator = iterable.iterator();

				if(secondLevelIterator.hasNext())
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns next value of collection.<br>
	 * If there is no next value, then {@link NoSuchElementException} thrown.
	 */
	@Override
	public V next()
	{
		if (secondLevelIterator == null || !secondLevelIterator.hasNext())
			throw new NoSuchElementException();
		return secondLevelIterator.next();
	}

	/**
	 * <font color="red"><b>NOT IMPLEMENTED</b></font>
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("This operation is not supported.");
	}
}
