/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.templates.teleport;

import gameserver.model.Race;

import javax.xml.bind.annotation.*;


/**
 * @author orz
 * 
 */
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
