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

package gameserver.model.templates.flyring;

import gameserver.model.utils3d.Point3D;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlyRing")
public class FlyRingTemplate
{
	@XmlAttribute(name = "name")
	protected String name;
	
	@XmlAttribute(name = "map")
	protected int map;
	
	@XmlAttribute(name = "radius")
	protected float radius;
	
	@XmlElement(name = "center")
	protected FlyRingPoint center;
	
	@XmlElement(name = "p1")
	protected FlyRingPoint p1;
	
	@XmlElement(name = "p2")
	protected FlyRingPoint p2;
	
	public String getName ()
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
	
	public FlyRingPoint getCenter()
	{
		return center;
	}
	
	public FlyRingPoint getP1()
	{
		return p1;
	}
	
	public FlyRingPoint getP2()
	{
		return p2;
	}
	
	public FlyRingTemplate () { }

    public FlyRingTemplate (String name, int mapId, Point3D center, Point3D p1, Point3D p2)
	{
		this.name = name;
		this.map = mapId;
		this.radius = 6;
		this.center = new FlyRingPoint(center);
		this.p1 = new FlyRingPoint(p1);
		this.p2 = new FlyRingPoint(p2);
	}
}
