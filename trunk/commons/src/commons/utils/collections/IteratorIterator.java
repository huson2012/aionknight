/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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