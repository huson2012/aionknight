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

package gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="bind_points")
@XmlAccessorType(XmlAccessType.NONE)

public class BindPointTemplate
{
	@XmlAttribute(name = "name", required = true)
	private String		name;

	@XmlAttribute(name = "npcid")
	private int		npcId;

	@XmlAttribute(name = "bindid")
	private int		bindId;
	
	@XmlAttribute(name = "mapid")
	private int	 	mapId = 0;

	@XmlAttribute(name = "posX")
	private float	 	x = 0;

	@XmlAttribute(name = "posY")
	private float	 	y = 0;

	@XmlAttribute(name = "posZ")
	private float	 	z = 0;

	@XmlAttribute(name = "price")
	private int	 	price = 0;

	public String getName()
	{
		return name;
	}

	public int getNpcId()
	{
		return npcId;
	}

	public int getBindId()
	{
		return bindId;
	}

	public int getZoneId()
	{
		return mapId;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}

	public int getPrice()
	{
		return price;
	}
}
