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

import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.world.exceptions.DuplicateAionObjectException;
import javolution.util.FastMap;
import java.util.Map;

public class KiskContainer
{
	private final Map<Integer, Kisk> kiskByPlayerObjectId = new FastMap<Integer, Kisk>().shared();
	
	public void add(Kisk kisk, Player player)
	{
		if (this.kiskByPlayerObjectId.put(player.getObjectId(), kisk) != null)
			throw new DuplicateAionObjectException();
	}
	
	public Kisk get(Player player)
	{
		return this.kiskByPlayerObjectId.get(player.getObjectId());
	}
	
	public void remove(Player player)
	{
		this.kiskByPlayerObjectId.remove(player.getObjectId());
	}

	public int getCount()
	{
		return this.kiskByPlayerObjectId.size();
	}
}
