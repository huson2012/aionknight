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

package gameserver.model.gameobjects.player;

import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Player macrosses collection, contains all player macrosses.
 * <p/>
 * Created on: 13.07.2009 16:28:23
 * 
 * @author Aquanox
 */
public class MacroList
{
	/**
	 * Class logger
	 */
	private static final Logger			logger	= Logger.getLogger(MacroList.class);

	/**
	 * Container of macrosses, position to xml.
	 */
	private final Map<Integer, String>	macrosses;

	/**
	 * Creates an empty macro list
	 */
	public MacroList()
	{
		this.macrosses = new HashMap<Integer, String>();
	}

	/**
	 * Create new instance of <tt>MacroList</tt>.
	 * 
	 * @param arg
	 */
	public MacroList(Map<Integer, String> arg)
	{
		this.macrosses = arg;
	}

	/**
	 * Returns map with all macrosses
	 * 
	 * @return all macrosses
	 */
	public Map<Integer, String> getMacrosses()
	{
		return Collections.unmodifiableMap(macrosses);
	}

	/**
	 * Add macro to the collection.
	 * 
	 * @param macroPosition
	 *           Macro order.
	 * @param macroXML
	 *           Macro Xml contents.
	 * @return <tt>true</tt> if macro addition was successful, and it can be stored into database. Otherwise
	 *        <tt>false</tt>.
	 */
	public synchronized boolean addMacro(int macroPosition, String macroXML)
	{
		if(macrosses.containsKey(macroPosition))
		{
			macrosses.remove(macroPosition);
			macrosses.put(macroPosition, macroXML);
			return false;
		}

		macrosses.put(macroPosition, macroXML);
		return true;
	}

	/**
	 * Remove macro from the list.
	 * 
	 * @param macroPosition
	 * @return <tt>true</tt> if macro deletion was successful, and changes can be stored into database. Otherwise
	 *        <tt>false</tt>.
	 */
	public synchronized boolean removeMacro(int macroPosition)
	{
		String m = macrosses.remove(macroPosition);
		if(m == null)//
		{
			logger.warn("Trying to remove non existing macro.");
			return false;
		}
		return true;
	}

	/**
	 * Returns count of available macrosses.
	 * 
	 * @return count of available macrosses.
	 */
	public int getSize()
	{
		return macrosses.size();
	}

	/**
	 * Returns an entry set of macro id to macro contents.
	 */
	public Set<Entry<Integer, String>> entrySet()
	{
		return Collections.unmodifiableSet(getMacrosses().entrySet());
	}
}
