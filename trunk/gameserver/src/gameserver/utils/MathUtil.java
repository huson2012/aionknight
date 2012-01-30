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

package gameserver.utils;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.geometry.Point3D;
import java.awt.*;

public class MathUtil
{
	/**
	 * Returns distance between two 2D points
	 * 
	 * @param point1
	 *           first point
	 * @param point2
	 *           second point
	 * @return distance between points
	 */
	public static double getDistance(Point point1, Point point2)
	{
		return getDistance(point1.x, point1.y, point2.x, point2.y);
	}

	/**
	 * Returns distance between two sets of coords
	 * 
	 * @param x1
	 *           first x coord
	 * @param y1
	 *           first y coord
	 * @param x2
	 *           second x coord
	 * @param y2
	 *           second y coord
	 * @return distance between sets of coords
	 */
	public static double getDistance(int x1, int y1, int x2, int y2)
	{
		// using long to avoid possible overflows when multiplying
		long dx = x2 - x1;
		long dy = y2 - y1;

		// return Math.hypot(x2 - x1, y2 - y1); // Extremely slow
		// return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // 20 times faster than hypot
		return Math.sqrt(dx * dx + dy * dy); // 10 times faster then previous line
	}

	/**
	 * Returns distance between two 3D points
	 * 
	 * @param point1
	 *           first point
	 * @param point2
	 *           second point
	 * @return distance between points
	 */
	public static double getDistance(Point3D point1, Point3D point2)
	{
		return getDistance(point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
	}

	/**
	 * Returns distance between 3D set of coords
	 * 
	 * @param x1
	 *           first x coord
	 * @param y1
	 *           first y coord
	 * @param z1
	 *           first z coord
	 * @param x2
	 *           second x coord
	 * @param y2
	 *           second y coord
	 * @param z2
	 *           second z coord
	 * @return distance between coords
	 */
	public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;

		// We should avoid Math.pow or Math.hypot due to perfomance reasons
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	/**
	 * 
	 * @param object
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static double getDistance(VisibleObject object , float x, float y , float z)
	{
		return getDistance(object.getX(), object.getY(), object.getZ(), x, y, z);
	}
	
	/**
	 * 
	 * @param object
	 * @param object
	 * @return
	 */
	public static double getDistance(VisibleObject object1,VisibleObject object2)
	{
		return getDistance(object1.getX(), object1.getY(), object1.getZ(), object2.getX(), object2.getY(), object2.getZ());
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param ss
	 *           segment start point
	 * @param se
	 *           segment end point
	 * @param p
	 *           point to found closest point on segment
	 * @return closest point on segment to p
	 */
	public static Point getClosestPointOnSegment(Point ss, Point se, Point p)
	{
		return getClosestPointOnSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param sx1
	 *           segment x coord 1
	 * @param sy1
	 *           segment y coord 1
	 * @param sx2
	 *           segment x coord 2
	 * @param sy2
	 *           segment y coord 2
	 * @param px
	 *           point x coord
	 * @param py
	 *           point y coord
	 * @return closets point on segment to point
	 */
	public static Point getClosestPointOnSegment(int sx1, int sy1, int sx2, int sy2, int px, int py)
	{
		double xDelta = sx2 - sx1;
		double yDelta = sy2 - sy1;

		if((xDelta == 0) && (yDelta == 0))
		{
			throw new IllegalArgumentException("Segment start equals segment end");
		}

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point closestPoint;
		if(u < 0)
		{
			closestPoint = new Point(sx1, sy1);
		}
		else if(u > 1)
		{
			closestPoint = new Point(sx2, sy2);
		}
		else
		{
			closestPoint = new Point((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
		}

		return closestPoint;
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param ss
	 *           segment start point
	 * @param se
	 *           segment end point
	 * @param p
	 *           point to found closest point on segment
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(Point ss, Point se, Point p)
	{
		return getDistanceToSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param sx1
	 *           segment x coord 1
	 * @param sy1
	 *           segment y coord 1
	 * @param sx2
	 *           segment x coord 2
	 * @param sy2
	 *           segment y coord 2
	 * @param px
	 *           point x coord
	 * @param py
	 *           point y coord
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(int sx1, int sy1, int sx2, int sy2, int px, int py)
	{
		Point closestPoint = getClosestPointOnSegment(sx1, sy1, sx2, sy2, px, py);
		return getDistance(closestPoint.x, closestPoint.y, px, py);
	}

	/**
	 * Checks whether two given instances of AionObject are within given range.
	 * 
	 * @param object1
	 * @param object2
	 * @param range
	 * @return true if objects are in range, false otherwise
	 */
	public static boolean isInRange(VisibleObject object1, VisibleObject object2, float range)
	{
		if(object1.getWorldId() != object2.getWorldId() || object1.getInstanceId() != object2.getInstanceId())
			return false;
		
		float dx = (object2.getX() - object1.getX());
		float dy = (object2.getY() - object1.getY());
		return dx * dx + dy * dy < range * range;
	}
	
	/**
	 * Checks whether two given instances of AionObject are within given range.
	 * Includes Z-Axis check.
	 * @param object1
	 * @param object2
	 * @param range
	 * @return true if objects are in range, false otherwise
	 */
	public static boolean isIn3dRange(VisibleObject object1, VisibleObject object2, float range)
	{
		if(object1 == null || object2 == null)
			return false;
		
		if(object1.getWorldId() != object2.getWorldId() || object1.getInstanceId() != object2.getInstanceId())
			return false;
		
		float dx = (object2.getX() - object1.getX());
		float dy = (object2.getY() - object1.getY());
		float dz = (object2.getZ() - object1.getZ());
		return dx * dx + dy * dy + dz * dz < range * range;
	}
	
	/**
	 * 
	 * @param obj1X
	 * @param obj1Y
	 * @param obj2X
	 * @param obj2Y
	 * @return float
	 */
	public final static float calculateAngleFrom(float obj1X, float obj1Y, float obj2X, float obj2Y)
	{
		float angleTarget = (float) Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0)
			angleTarget = 360 + angleTarget;
		return angleTarget;
	}
	
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return float
	 */
	public static float calculateAngleFrom(VisibleObject obj1, VisibleObject obj2)
	{
		return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}
	
	/**
	 * 
	 * @param clientHeading
	 * @return float
	 */
	public final static float convertHeadingToDegree(byte clientHeading)
	{
		return clientHeading * 3;
	}
}
