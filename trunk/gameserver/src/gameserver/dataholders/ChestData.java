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