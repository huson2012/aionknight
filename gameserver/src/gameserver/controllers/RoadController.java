/**
 * This file is part of Aion-Knight Emu.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight Emu. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * author^
 * Fr0st;
 * Mr.Chayka.
*/

package gameserver.controllers;

import gameserver.controllers.movement.RoadObserver;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.road.Road;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

	public class RoadController extends CreatureController<Road> {
		FastMap<Player, RoadObserver> observed = new FastMap<Player, RoadObserver>().shared();

	@Override
	public void see(VisibleObject object) {
		super.see(object);

		if (!(object instanceof Player)) {
			return;
		}

		Player p = (Player)object;
		RoadObserver observer = new RoadObserver((Road)getOwner(), p);
		p.getObserveController().addObserver(observer);
		observed.put(p, observer);
		Logger.getLogger(RoadController.class).debug(getOwner().getName() + " sees " + p.getName());
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if ((isOutOfRange) && ((object instanceof Player))) {
			Player p = (Player)object;
			RoadObserver observer = observed.remove(p);
			observer.moved();
			p.getObserveController().removeObserver(observer);
			Logger.getLogger(RoadController.class).debug(getOwner().getName() + " not sees " + p.getName());
		}
	}
}