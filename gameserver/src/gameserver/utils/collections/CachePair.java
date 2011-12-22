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

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CachePair<K extends Comparable, V> implements Comparable<CachePair> 
{
	public CachePair(K key, V value) 
	{
		this.key = key;
		this.value = value;
	}

	public K key;
	public V value;

	public boolean equals(Object obj) 
	{
		if (obj instanceof CachePair) 
		{
			CachePair p = (CachePair)obj;
			return key.equals(p.key) && value.equals(p.value);
		}
		return false;
	}

	public int compareTo(CachePair p) 
	{
		int v = key.compareTo(p.key);
		if (v == 0 && p.value instanceof Comparable) 
			return ((Comparable)value).compareTo(p.value);
		return v;
	}

	@Override
	public int hashCode() 
	{
		return key.hashCode() ^ value.hashCode();
	}

	@Override
	public String toString() 
	{
		return key + ": " + value;
	}
}
