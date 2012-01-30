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

public class EmotionList
{
	private LinkedHashMap<Integer, Emotion> emotions;
	private Player owner;

	public EmotionList()
	{
	    this.emotions = new LinkedHashMap<Integer, Emotion>();
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

	public boolean add(int id, long date, long expires_time)
	{
		if(!emotions.containsKey(id))
		{
			emotions.put(id, new Emotion(id, date, expires_time));
			return true;
		}
		return false;
	}

	public void remove(int id)
	{
		if(emotions.containsKey(id))
		{
			emotions.remove(id);
		}
	}

	public Emotion get(int id)
	{
		if(emotions.containsKey(id))
			return emotions.get(id);

		return null;
	}

	public boolean canAdd(int id)
	{
        return !emotions.containsKey(id);

    }

	public int size()
	{
		return emotions.size();
	}

	public Collection<Emotion> getEmotions()
	{

		return emotions.values();
	}
}
