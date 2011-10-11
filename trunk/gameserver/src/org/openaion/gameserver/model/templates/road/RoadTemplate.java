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

package org.openaion.gameserver.model.templates.road;

import org.openaion.gameserver.model.utils3d.Point3D;

// Referenced classes of package org.openaion.gameserver.model.templates.road:
//            RoadPoint, RoadExit

public class RoadTemplate
{

    public String getName()
    {
        return name;
    }

    public int getMap()
    {
        return map;
    }

    public float getRadius()
    {
        return radius;
    }

    public RoadPoint getCenter()
    {
        return center;
    }

    public RoadPoint getP1()
    {
        return p1;
    }

    public RoadPoint getP2()
    {
        return p2;
    }

    public RoadExit getRoadExit()
    {
        return roadExit;
    }

    public RoadTemplate()
    {
    }

    public RoadTemplate(String s, int i, Point3D point3d, Point3D point3d1, Point3D point3d2)
    {
        name = s;
        map = i;
        radius = 6F;
        center = new RoadPoint(point3d);
        p1 = new RoadPoint(point3d1);
        p2 = new RoadPoint(point3d2);
    }

    protected String name;
    protected int map;
    protected float radius;
    protected RoadPoint center;
    protected RoadPoint p1;
    protected RoadPoint p2;
    protected RoadExit roadExit;
}