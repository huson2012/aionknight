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
 
package gameserver.dataholders;

import gameserver.model.templates.chest.ChestTemplate;
import gnu.trove.THashMap;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "chest_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChestData
{
	@XmlElement(name = "chest")
	private List<ChestTemplate> chests;
	private TIntObjectHashMap<ChestTemplate> chestData	= new TIntObjectHashMap<ChestTemplate>();
	private TIntObjectHashMap<ArrayList<ChestTemplate>> instancesMap = new TIntObjectHashMap<ArrayList<ChestTemplate>>();
	private THashMap<String, ChestTemplate> namedChests = new THashMap<String, ChestTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		chestData.clear();
		instancesMap.clear();
		namedChests.clear();
		
		for(ChestTemplate chest : chests)
		{
			chestData.put(chest.getNpcId(), chest);
			if(chest.getName() != null && !chest.getName().isEmpty())
				namedChests.put(chest.getName(), chest);
		}
	}
	
	public int size()
	{
		return chestData.size();
	}

	public ChestTemplate getChestTemplate(int npcId)
	{
		return chestData.get(npcId);
	}

	public List<ChestTemplate> getChests()
	{
		return chests;
	}

	public void setChests(List<ChestTemplate> chests)
	{
		this.chests = chests;
		afterUnmarshal(null, null);
	}
}