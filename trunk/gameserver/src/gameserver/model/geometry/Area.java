/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.model.geometry;

import java.awt.*;

public interface Area
{
	public boolean isInside2D(Point point);
	public boolean isInside2D(int x, int y);
	public boolean isInside3D(Point3D point);
	public boolean isInside3D(int x, int y, int z);
	public boolean isInsideZ(Point3D point);
	public boolean isInsideZ(int z);
	public double getDistance2D(Point point);
	public double getDistance2D(int x, int y);
	public double getDistance3D(Point3D point);
	public double getDistance3D(int x, int y, int z);
	public Point getClosestPoint(Point point);
	public Point getClosestPoint(int x, int y);
	public Point3D getClosestPoint(Point3D point);
	public Point3D getClosestPoint(int x, int y, int z);
	public int getMinZ();
	public int getMaxZ();
}