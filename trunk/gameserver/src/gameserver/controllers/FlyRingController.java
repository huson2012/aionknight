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

package gameserver.controllers;

import gameserver.controllers.movement.FlyRingObserver;
import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public class FlyRingController extends CreatureController<FlyRing>
{
	FastMap<Player, FlyRingObserver> observed = new FastMap<Player, FlyRingObserver>().shared();
	
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		
		if(!(object instanceof Player))
		{
			return;
		}
		
		Player p = (Player)object;
		FlyRingObserver observer = new FlyRingObserver ((FlyRing)getOwner(),p);
		p.getObserveController().addObserver(observer);
		observed.put(p, observer);
		Logger.getLogger(FlyRingController.class).debug(getOwner().getName()+" sees "+p.getName());
	}
	
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object,isOutOfRange);
		if (isOutOfRange && object instanceof Player)
		{
			Player p = (Player)object;
			if (observed.containsKey(p))
			{
				FlyRingObserver observer = observed.remove(p);
				observer.moved();
				p.getObserveController().removeObserver(observer);
			}
			Logger.getLogger(FlyRingController.class).debug(getOwner().getName()+" not sees "+p.getName());
		}
	}
}