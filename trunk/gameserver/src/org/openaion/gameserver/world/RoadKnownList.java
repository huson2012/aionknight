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

package org.openaion.gameserver.world;

import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.road.Road;
import org.openaion.gameserver.model.templates.road.RoadTemplate;
import org.openaion.gameserver.utils.MathUtil;

// Referenced classes of package org.openaion.gameserver.world:
//            KnownList

public class RoadKnownList extends KnownList
{

    public RoadKnownList(VisibleObject visibleobject)
    {
        super(visibleobject);
    }

    protected boolean checkObjectInRange(VisibleObject visibleobject, VisibleObject visibleobject1)
    {
        Road road = (Road)visibleobject;
        return MathUtil.isIn3dRange(visibleobject, visibleobject1, road.getTemplate().getRadius() * 2.0F);
    }
}