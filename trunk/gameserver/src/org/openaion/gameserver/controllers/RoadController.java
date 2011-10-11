/*
 * This file is part of Aion-Knight Emu.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight Emu.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * author^
 * Fr0st;
 * Mr.Chayka.
*/

package org.openaion.gameserver.controllers;

import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.openaion.gameserver.controllers.movement.RoadObserver;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.road.Road;

// Referenced classes of package org.openaion.gameserver.controllers:
//            CreatureController, ObserveController

public class RoadController extends CreatureController
{

    public RoadController()
    {
        observed = new FastMap();
        log = Logger.getLogger(RoadController.class);
    }

    public void see(VisibleObject visibleobject)
    {
        super.see(visibleobject);
        if(!(visibleobject instanceof Player))
        {
            return;
        } else
        {
            Player player = (Player)visibleobject;
            RoadObserver roadobserver = new RoadObserver((Road)getOwner(), player);
            player.getObserveController().addObserver(roadobserver);
            observed.put(player, roadobserver);
            log.debug((new StringBuilder()).append(((Creature)getOwner()).getName()).append(" sees ").append(player.getName()).toString());
            return;
        }
    }

    public void notSee(VisibleObject visibleobject, boolean flag)
    {
        super.notSee(visibleobject, flag);
        if(flag && (visibleobject instanceof Player))
        {
            Player player = (Player)visibleobject;
            RoadObserver roadobserver = (RoadObserver)observed.remove(player);
            roadobserver.moved();
            player.getObserveController().removeObserver(roadobserver);
            log.debug((new StringBuilder()).append(((Creature)getOwner()).getName()).append(" not sees ").append(player.getName()).toString());
        }
    }

    FastMap observed;
    private Logger log;
}