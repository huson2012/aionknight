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
