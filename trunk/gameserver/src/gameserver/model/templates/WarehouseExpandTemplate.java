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

import gameserver.model.templates.expand.Expand;
import gameserver.utils.Util;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "warehouse_npc")
@XmlAccessorType(XmlAccessType.FIELD)
public class WarehouseExpandTemplate
{
	@XmlElement(name = "expand", required = true)
	protected List<Expand>	warehouseExpands;

	/**
	 * NPC ID
	 */
	@XmlAttribute(name = "id", required = true)
	protected int				id;

	/**
	 * NPC name
	 */
	@XmlAttribute(name = "name", required = true)
	protected String			name	= "";

	public int getNpcId()
	{
		return id;
	}

	/**
	 * Gets the value of the material property.
	 */
	public List<Expand> getWarehouseExpand()
	{
		return this.warehouseExpands;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName()
	{
		return Util.convertName(name);
	}
	
	/**
	 * Returns true if list contains level
	 * @return true or false
	 */
	public boolean contains(int level)
	{
		for(Expand expand : warehouseExpands)
		{
			if(expand.getLevel() == level)
				return true;
		}
		return false;
	}
	
	/**
	 * Returns true if list contains level
	 * @return expand
	 */
	public Expand get(int level)
	{
		for(Expand expand : warehouseExpands)
		{
			if(expand.getLevel() == level)
				return expand;
		}
		return null;
	}	
}
