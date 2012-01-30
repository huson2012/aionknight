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
