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

public class RoadPoint
{

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public RoadPoint()
    {
    }

    public RoadPoint(Point3D point3d)
    {
        x = (float)point3d.x;
        y = (float)point3d.y;
        z = (float)point3d.z;
    }

    private float x;
    private float y;
    private float z;
}