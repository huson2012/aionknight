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

package gameserver.world.container;

import gameserver.model.legion.Legion;
import gameserver.world.exceptions.DuplicateAionObjectException;
import javolution.util.FastMap;
import java.util.Iterator;
import java.util.Map;

public class LegionContainer implements Iterable<Legion>
{
	private final Map<Integer, Legion> legionsById = new FastMap<Integer, Legion>().shared();
	private final Map<String, Legion> legionsByName	= new FastMap<String, Legion>().shared();
	public void add(Legion legion)
	{
		if(legion == null || legion.getLegionName() == null)
			return;
		if(legionsById.put(legion.getLegionId(), legion) != null)
			throw new DuplicateAionObjectException();
		if(legionsByName.put(legion.getLegionName().toLowerCase(), legion) != null)
			throw new DuplicateAionObjectException();
	}

	public void remove(Legion legion)
	{
		legionsById.remove(legion.getLegionId());
		legionsByName.remove(legion.getLegionName().toLowerCase());
	}

	public Legion get(int legionId)
	{
		return legionsById.get(legionId);
	}

	public Legion get(String name)
	{
		return legionsByName.get(name.toLowerCase());
	}

	public boolean contains(int legionId)
	{
		return legionsById.containsKey(legionId);
	}

	public boolean contains(String name)
	{
		return legionsByName.containsKey(name.toLowerCase());
	}

	@Override
	public Iterator<Legion> iterator()
	{
		return legionsById.values().iterator();
	}
}