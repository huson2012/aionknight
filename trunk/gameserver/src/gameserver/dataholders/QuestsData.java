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

import gnu.trove.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import gameserver.model.templates.QuestTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quests")
public class QuestsData
{

	@XmlElement(name = "quest", required = true)
	protected List<QuestTemplate>		questsData;
	private TIntObjectHashMap<QuestTemplate>	questData	= new TIntObjectHashMap<QuestTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		questData.clear();
		for(QuestTemplate quest : questsData)
		{
			questData.put(quest.getId(), quest);
		}
	}

	public QuestTemplate getQuestById(int id)
	{
		return questData.get(id);
	}

	public int size()
	{
		return questData.size();
	}

	public List<QuestTemplate> getQuestsData()
	{
		return questsData;
	}

	public void setQuestsData(List<QuestTemplate> questsData)
	{
		this.questsData = questsData;
		afterUnmarshal(null, null);
	}	
}