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

package gameserver.model.templates.zone;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Points")
public class Points
{
	@XmlElement(required = true)
	protected List<Point2D>	point;
	@XmlAttribute(name = "top")
	protected float			top;
	@XmlAttribute(name = "bottom")
	protected float			bottom;
	@XmlAttribute
	protected String		type;

	/**
	 * 
	 * @return
	 */
	public List<Point2D> getPoint()
	{
		if(point == null)
		{
			point = new ArrayList<Point2D>();
		}
		return this.point;
	}

	/**
	 * @return the top
	 */
	public float getTop()
	{
		return top;
	}

	/**
	 * @return the bottom
	 */
	public float getBottom()
	{
		return bottom;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

}
