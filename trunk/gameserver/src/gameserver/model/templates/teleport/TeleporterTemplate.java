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

package gameserver.model.templates.teleport;

import gameserver.model.Race;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="teleporter_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TeleporterTemplate
{
	@XmlAttribute(name = "npc_id", required = true)
	private int npcId;
	
	@XmlAttribute(name = "name", required = true)
	private String name = "";

	@XmlAttribute(name = "teleportId", required = true)
	private int	 teleportId = 0;
	
	@XmlAttribute(name = "type", required = true)
	private TeleportType type;
	
	@XmlAttribute(name = "race")
	private Race race;

	@XmlElement(name = "locations")
	private TeleLocIdData teleLocIdData;

	/**
	 * @return the npcId
	 */
	public int getNpcId()
	{
		return npcId;
	}

	/**
	 * @return the name of npc
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the teleportId
	 */
	public int getTeleportId()
	{
		return teleportId;
	}

	/**
	 * @return the type
	 */
	public TeleportType getType()
	{
		return type;
	}

	/**
	 * @return the race
	 */
	public Race getRace()
	{
		return race;
	}

	/**
	 * @return the teleLocIdData
	 */
	public TeleLocIdData getTeleLocIdData()
	{
		return teleLocIdData;
	}
}
