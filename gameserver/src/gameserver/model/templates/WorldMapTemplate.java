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

import gameserver.world.WorldType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="map")
@XmlAccessorType(XmlAccessType.NONE)
public class WorldMapTemplate
{
	/**
	 * Map name.
	 */
	@XmlAttribute(name = "name")
	private String	name	= "";

	/**
	 * Map nameid.
	 */
	@XmlAttribute(name = "nameid")
	private int	mapnameId = 0;

	/**
	 * Map Id.
	 */
	@XmlAttribute(name = "id", required = true)
	private Integer		mapId;

	/**
	 * Number of twin instances [players will be balanced so every one could exp easy]
	 */
	@XmlAttribute(name = "twin_count")
	private int		twinCount;

	/**
	 * Max user at twin instance.
	 */
	@XmlAttribute(name = "max_user")
	private int		maxUser;

	/**
	 * True if this map is a prison.
	 */
	@XmlAttribute(name = "prison")
	private boolean	prison	= false;

	/**
	 * True if this map is a instance.
	 */
	@XmlAttribute(name = "instance")
	private boolean	instance	= false;
	
	/**
	 * Return instanceMapId, 0If not an instance
	 */
	@XmlAttribute(name = "instanceMapId")
	private int instanceMapId = 0;
	
	@XmlAttribute(name = "cooldown")
	private int cooldown = 0;

	/**
	 * The minimum Z coord, under this player die immediately
	 */
	@XmlAttribute(name = "death_level", required = true)
	private int	deathlevel	= 0;

	/**
	 * water level on map
	 */
	@XmlAttribute(name = "water_level", required = true)
	private int	waterlevel	= 16;
	
	/**
	 * world type of map
	 */
	@XmlAttribute(name = "world_type")
	private WorldType worldType = WorldType.NONE;
	
	/**
	 * world size - boundaries for geo engine
	 */
	@XmlAttribute(name = "world_size", required = true)
	private int			worldSize	= 0;

	public String getName()
	{
		return name;
	}
	
	public Integer getMapNameId()
	{
		return mapnameId;
	}

	public Integer getMapId()
	{
		return mapId;
	}

	public int getTwinCount()
	{
		return twinCount;
	}

	public int getMaxUser()
	{
		return maxUser;
	}

	public boolean isPrison()
	{
		return prison;
	}

	/**
	 * @return the instance
	 */
	public boolean isInstance()
	{
		return instance;
	}
	/**
	 * @return the waterlevel
	 */
	public int getWaterLevel()
	{
		return waterlevel;
	}
	/**
	 * @return the level of death :)
	 */
	public int getDeathLevel()
	{
		return deathlevel;
	}
	
	/**
	 * @return the WorldType
	 */
	public WorldType getWorldType()
	{
		return worldType;
	}

	/**
	 * @return the instanceMapId
	 */
	public int getInstanceMapId()
	{
		return instanceMapId;
	}
	
	public int getCooldown()
	{
		return cooldown;
	}
	
	public int getWorldSize()
	{
		return worldSize;
	}
}