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

package gameserver.model.templates.walker;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "routestep")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteStep
{
	@XmlAttribute(name = "step", required = true)
	private int step;
	
	@XmlAttribute(name = "loc_x", required = true)
	private float locX;

	@XmlAttribute(name = "loc_y", required = true)
	private float locY;
	
	@XmlAttribute(name = "loc_z", required = true)
	private float locZ;
	
	@XmlAttribute(name = "rest_time", required = true)
	private int time;

	public RouteStep()
	{
	}

	public RouteStep(float x, float y, float z)
	{
		locX = x;
		locY = y;
		locZ = z;
	}

	public float getX()
	{
		return locX;
	}

	public float getY()
	{
		return locY;
	}
	
	public float getZ()
	{
		return locZ;
	}
	
	public int getRouteStep()
	{
		return step;
	}
	
	public int getRestTime()
	{
		return time;
	}
}