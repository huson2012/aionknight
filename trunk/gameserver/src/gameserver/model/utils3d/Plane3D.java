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

package gameserver.model.utils3d;

import org.apache.log4j.Logger;

public class Plane3D
{
	private static final double[] column = new double[] { 1, 1, 1 };
	private Point3D p0;
	private Point3D p1;
	private Point3D p2;
	private double a;
	private double b;
	private double c;
	private double d;
	private double normalization;
	public Plane3D (Point3D p0, Point3D p1, Point3D p2)
	{
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		
		Matrix3D equation = new Matrix3D(new double[][] {
			{ p0.x, p0.y, p0.z },
			{ p1.x, p1.y, p1.z },
			{ p2.x, p2.y, p2.z }
		});
		
		double D = equation.determinant();
		
		d = 1;
		a = ((-d)/D) * equation.replaceColumn(0, column).determinant() ;
		b = ((-d)/D) * equation.replaceColumn(1, column).determinant() ;
		c = ((-d)/D) * equation.replaceColumn(2, column).determinant() ;
		
		normalization = Math.sqrt(a * a + b * b + c * c);
	}
	
	public Point3D getCenter ()
	{
		return p0;
	}
	
	public double getPointDistance (Point3D p)
	{
		double n = a * p.x + b * p.y + c * p.z + d;
		return n/normalization;
	}
	
	public boolean intersect (Point3D l0, Point3D l1)
	{
		double distanceL0 = getPointDistance(l0);
		double distanceL1 = getPointDistance(l1);

        return (distanceL0 > 0 && distanceL1 < 0) || (distanceL0 < 0 && distanceL1 > 0);
    }
	
	public Point3D intersection (Point3D la, Point3D lb)
	{
		double[] v1 = new double[] {
			la.x - p0.x,
			la.y - p0.y,
			la.z - p0.z
		};
		
		Matrix3D m1 = new Matrix3D (new double[][] {
			{ la.x - lb.x, p1.x - p0.x, p2.x - p0.x },
			{ la.y - lb.y, p1.y - p0.y, p2.y - p0.y },
			{ la.z - lb.z, p1.z - p0.z, p2.z - p0.z }
		});
		
		double[] formula = null;
		Point3D result = null;
		
		try {
			formula = m1.inverse().multiply(v1);
			
			result = new Point3D();
			result.x = la.x + (lb.x - la.x) * formula[0];
			result.y = la.y + (lb.y - la.y) * formula[0];
			result.z = la.z + (lb.z - la.z) * formula[0];
		}
		catch (RuntimeException e)
		{
			Logger.getLogger(getClass()).debug(m1+"(determinant: "+m1.determinant()+") * [ "+v1[0]+ ',' +v1[1]+ ',' +v1[0]+" ]: "+e.getMessage(), e);
		}
		return result;
	}
}