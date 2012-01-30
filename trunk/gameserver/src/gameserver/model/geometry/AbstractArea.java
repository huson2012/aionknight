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

public abstract class AbstractArea implements Area
{
	private final int minZ;
	private final int maxZ;
	protected AbstractArea(int minZ, int maxZ)
	{
		if(minZ > maxZ)
		{
			throw new IllegalArgumentException("minZ(" + minZ + ") > maxZ(" + maxZ + ')');
		}
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	@Override
	public boolean isInside2D(Point point)
	{
		return isInside2D(point.x, point.y);
	}

	@Override
	public boolean isInside3D(Point3D point)
	{
		return isInside3D(point.getX(), point.getY(), point.getZ());
	}

	@Override
	public boolean isInside3D(int x, int y, int z)
	{
		return isInsideZ(z) && isInside2D(x, y);
	}

	@Override
	public boolean isInsideZ(Point3D point)
	{
		return isInsideZ(point.getZ());
	}

	@Override
	public boolean isInsideZ(int z)
	{
		return z >= minZ && z <= maxZ;
	}

	@Override
	public double getDistance2D(Point point)
	{
		return getDistance2D(point.x, point.y);
	}

	@Override
	public double getDistance3D(Point3D point)
	{
		return getDistance3D(point.getX(), point.getY(), point.getZ());
	}

	@Override
	public Point getClosestPoint(Point point)
	{
		return getClosestPoint(point.x, point.y);
	}

	@Override
	public Point3D getClosestPoint(Point3D point)
	{
		return getClosestPoint(point.getX(), point.getY(), point.getZ());
	}

	@Override
	public Point3D getClosestPoint(int x, int y, int z)
	{
		Point closest2d = getClosestPoint(x, y);

		int zCoord;

		if(isInsideZ(z))
		{
			zCoord = z;
		}
		else if(z < minZ)
		{
			zCoord = minZ;
		}
		else
		{
			zCoord = maxZ;
		}

		return new Point3D(closest2d.x, closest2d.y, zCoord);
	}

	public int getMinZ()
	{
		return minZ;
	}

	public int getMaxZ()
	{
		return maxZ;
	}
}
