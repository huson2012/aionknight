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

package gameserver.world.zone;

import gameserver.model.templates.zone.Point2D;
import gameserver.model.templates.zone.ZoneTemplate;
import java.util.Collection;

public class ZoneInstance
{
	private int	corners;
	private float xCoordinates[];
	private float yCoordinates[];
	private ZoneTemplate template;
	private Collection<ZoneInstance> neighbors;
	public ZoneInstance(ZoneTemplate template)
	{
		this.template = template;
		this.corners = template.getPoints().getPoint().size();
		xCoordinates = new float[corners];
		yCoordinates = new float[corners];
		for(int i = 0; i < corners; i++)
		{
			Point2D point = template.getPoints().getPoint().get(i);
			xCoordinates[i] = point.getX();
			yCoordinates[i] = point.getY();
		}
	}

	public int getCorners()
	{
		return corners;
	}

	public float[] getxCoordinates()
	{
		return xCoordinates;
	}

	public float[] getyCoordinates()
	{
		return yCoordinates;
	}

	public Collection<ZoneInstance> getNeighbors()
	{
		return neighbors;
	}

	public void setNeighbors(Collection<ZoneInstance> neighbours)
	{
		this.neighbors = neighbours;
	}

	public ZoneTemplate getTemplate()
	{
		return template;
	}

	public float getTop()
	{
		return template.getPoints().getTop();
	}

	public float getBottom()
	{
		return template.getPoints().getBottom();
	}

	public boolean isBreath()
	{
		return template.isBreath();
	}

	public int getPriority()
	{
		return template.getPriority();
	}
}
