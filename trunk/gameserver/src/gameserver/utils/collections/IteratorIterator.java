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
