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

import gameserver.utils.MathUtil;
import java.awt.*;

public class CylinderArea extends AbstractArea
{
	private final int centerX;
	private final int centerY;
	private final int radius;
	public CylinderArea(Point center, int radius, int minZ, int maxZ)
	{
		this(center.x, center.y, radius, minZ, maxZ);
	}

	public CylinderArea(int x, int y, int radius, int minZ, int maxZ)
	{
		super(minZ, maxZ);
		this.centerX = x;
		this.centerY = y;
		this.radius = radius;
	}

	@Override
	public boolean isInside2D(int x, int y)
	{
		return MathUtil.getDistance(centerX, centerY, x, y) < radius;
	}

	@Override
	public double getDistance2D(int x, int y)
	{
		if(isInside2D(x, y))
		{
			return 0;
		}
		else
		{
			return Math.abs(MathUtil.getDistance(centerX, centerY, x, y) - radius);
		}
	}

	@Override
	public double getDistance3D(int x, int y, int z)
	{
		if(isInside3D(x, y, z))
		{
			return 0;
		}
		else if(isInsideZ(z))
		{
			return getDistance2D(x, y);
		}
		else
		{
			if(z < getMinZ())
			{
				return MathUtil.getDistance(centerX, centerY, getMinZ(), x, y, z);
			}
			else
			{
				return MathUtil.getDistance(centerX, centerY, getMaxZ(), x, y, z);
			}
		}
	}

	@Override
	public Point getClosestPoint(int x, int y)
	{
		if(isInside2D(x, y))
		{
			return new Point(x, y);
		}
		else
		{
			int vX = x - this.centerX;
			int vY = y - this.centerY;
			double magV = MathUtil.getDistance(centerX, centerY, x, y);
			double pointX = centerX + vX / magV * radius;
			double pointY = centerY + vY / magV * radius;
			return new Point((int) Math.round(pointX), (int) Math.round(pointY));
		}
	}
}
