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
package gameserver.model.templates.siege;

import gameserver.model.siege.SiegeRace;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * @author Sylar
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Guard")
public class SiegeGuardTemplate
{
	@XmlAttribute(name = "npcid_dr")
	protected int		npcid_Drakan;
	@XmlAttribute(name = "npcid_da")
	protected int		npcid_Asmodians;
	@XmlAttribute(name = "npcid_li")
	protected int 		npcid_Elyos;
	
	@XmlElement(name = "loc")
	protected List<SiegeSpawnLocationTemplate> spawnLocations;

	/**
	 * @return the spawnLocations
	 */
	public List<SiegeSpawnLocationTemplate> getSpawnLocations()
	{
		return spawnLocations;
	}
	
	public int getNpcId(SiegeRace race)
	{
		switch(race)
		{
			case ASMODIANS: return npcid_Asmodians;
			case BALAUR: return npcid_Drakan;
			case ELYOS: return npcid_Elyos;
			default: return 0;
		}
	}
	
}
