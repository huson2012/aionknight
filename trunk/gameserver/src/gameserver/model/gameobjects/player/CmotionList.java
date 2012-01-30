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

import java.util.Collection;
import java.util.LinkedHashMap;

public class CmotionList
{
	private LinkedHashMap<Integer, Cmotion> cmotions;
	private Player owner;

	public CmotionList()
	{
	    this.cmotions = new LinkedHashMap<Integer, Cmotion>();
	    this.owner = null;
	}

	public void setOwner(Player owner)
	{
		this.owner = owner;
	}

	public Player getOwner()
	{
		return owner;
	}

	public boolean add(int id, boolean active, long date, long expires_time)
	{
		if(!cmotions.containsKey(id))
		{
			cmotions.put(id, new Cmotion(id, active, date, expires_time));
			return true;
		}
		return false;
	}

	public void remove(int id)
	{
		if(cmotions.containsKey(id))
		{
			cmotions.remove(id);
		}
	}

	public Cmotion get(int id)
	{
		if(cmotions.containsKey(id))
			return cmotions.get(id);

		return null;
	}

	public boolean canAdd(int id)
	{
        return !cmotions.containsKey(id);

    }

	public int size()
	{
		return cmotions.size();
	}

	public Collection<Cmotion> getCmotions()
	{

		return cmotions.values();
	}
}
