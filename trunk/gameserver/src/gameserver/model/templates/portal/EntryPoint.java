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

package gameserver.model.templates.portal;

import gameserver.model.Race;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryPoint")
public class EntryPoint
{
	@XmlAttribute(name = "mapid")
	protected int	mapId;
	@XmlAttribute(name = "x")
	protected float	x;
	@XmlAttribute(name = "y")
	protected float	y;
	@XmlAttribute(name = "z")
	protected float	z;
	@XmlAttribute(name = "race")
	protected Race	race;
	/**
	 * @return the mapId
	 */
	public int getMapId()
	{
		return mapId;
	}
	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}
	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}
	/**
	 * @return the z
	 */
	public float getZ()
	{
		return z;
	}
	/**
	 * @return the race
	 */
	public Race getRace()
	{
		return race;
	}
}
