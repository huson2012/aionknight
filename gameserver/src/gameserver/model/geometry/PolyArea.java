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
import java.util.Collection;

public class PolyArea extends AbstractArea
{
	private final int[] xPoints;
	private final int[] yPoints;
	private final Polygon poly;
	public PolyArea(Collection<Point> points, int zMin, int zMax)
	{
		this(points.toArray(new Point[points.size()]), zMin, zMax);
	}

	public PolyArea(Point[] points, int zMin, int zMax)
	{
		super(zMin, zMax);

		if(points.length < 3)
		{
			throw new IllegalArgumentException("Not enough points, needed at least 3 but got " + points.length);
		}

		this.xPoints = new int[points.length];
		this.yPoints = new int[points.length];

		Polygon polygon = new Polygon();
		for(int i = 0, n = points.length; i < n; i++)
		{
			Point p = points[i];
			polygon.addPoint(p.x, p.y);
			xPoints[i] = p.x;
			yPoints[i] = p.y;
		}
		this.poly = polygon;
	}

	@Override
	public boolean isInside2D(int x, int y)
	{
		return poly.contains(x, y);
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
			Point cp = getClosestPoint(x, y);
			return MathUtil.getDistance(cp.x, cp.y, x, y);
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
			Point3D cp = getClosestPoint(x, y, z);
			return MathUtil.getDistance(cp.getX(), cp.getY(), cp.getZ(), x, y, z);
		}
	}

	@Override
	public Point getClosestPoint(int x, int y)
	{

		Point closestPoint = null;
		double closestDistance = 0;

		for(int i = 0; i < xPoints.length; i++)
		{
			int nextIndex = i + 1;
			if(nextIndex == xPoints.length)
			{
				nextIndex = 0;
			}

			int p1x = xPoints[i];
			int p1y = yPoints[i];
			int p2x = xPoints[nextIndex];
			int p2y = yPoints[nextIndex];

			Point point = MathUtil.getClosestPointOnSegment(p1x, p1y, p2x, p2y, x, y);

			if(closestPoint == null)
			{
				closestPoint = point;
				closestDistance = MathUtil.getDistance(closestPoint.x, closestPoint.y, x, y);
			}
			else
			{
				double newDistance = MathUtil.getDistance(point.x, point.y, x, y);
				if(newDistance < closestDistance)
				{
					closestPoint = point;
					closestDistance = newDistance;
				}
			}
		}

		return closestPoint;
	}
}
