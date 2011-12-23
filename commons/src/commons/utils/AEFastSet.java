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

package commons.utils;

import java.util.Iterator;
import java.util.Set;
import javolution.util.FastMap;
import javolution.util.FastCollection.Record;

@SuppressWarnings("unchecked")
public class AEFastSet<E> extends AEFastCollection<E> implements Set<E>
{
	private static final Object	NULL = new Object();
	private final FastMap<E, Object> map;

	public AEFastSet()
	{
		map = new FastMap<E, Object>();
	}

	public AEFastSet(int capacity)
	{
		map = new FastMap<E, Object>(capacity);
	}

	public AEFastSet(Set<? extends E> elements)
	{
		map = new FastMap<E, Object>(elements.size());

		addAll(elements);
	}

	public boolean isShared()
	{
		return map.isShared();
	}

	@Override
	public Record head()
	{
		return map.head();
	}

	@Override
	public Record tail()
	{
		return map.tail();
	}

	@Override
	public E valueOf(Record record)
	{
		return ((FastMap.Entry<E, Object>) record).getKey();
	}

	@Override
	public void delete(Record record)
	{
		map.remove(((FastMap.Entry<E, Object>) record).getKey());
	}

	@Override
	public void delete(Record record, E value)
	{
		map.remove(value);
	}

	public boolean add(E value)
	{
		return map.put(value, NULL) == null;
	}

	public void clear()
	{
		map.clear();
	}

	public boolean contains(Object o)
	{
		return map.containsKey(o);
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	public Iterator<E> iterator()
	{
		return map.keySet().iterator();
	}

	public boolean remove(Object o)
	{
		return map.remove(o) != null;
	}

	public int size()
	{
		return map.size();
	}

	@Override
	public String toString()
	{
		return super.toString() + "-" + map.keySet().toString();
	}
}