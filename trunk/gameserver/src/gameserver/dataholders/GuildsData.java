/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
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
import gameserver.model.templates.GuildTemplate;

@XmlRootElement(name = "guild_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuildsData
{
	@XmlElement(name = "guild_template")
	private List<GuildTemplate> guildTemplates;
	private TIntObjectHashMap<GuildTemplate>	guildDataNpcId		= new TIntObjectHashMap<GuildTemplate>();
	private TIntObjectHashMap<GuildTemplate>	guildDataGuildId	= new TIntObjectHashMap<GuildTemplate>();
	private TIntObjectHashMap<GuildTemplate>	guildDataQuestId	= new TIntObjectHashMap<GuildTemplate>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		guildDataNpcId.clear();
		guildDataGuildId.clear();
		guildDataQuestId.clear();
		for(GuildTemplate guildTemplate: guildTemplates)
		{
			guildDataNpcId.put(guildTemplate.getNpcId(), guildTemplate);
			guildDataGuildId.put(guildTemplate.getGuildId(), guildTemplate);
			for(int i=0; i<guildTemplate.getGuildQuests().getGuildQuest().size(); i++)
				guildDataQuestId.put(guildTemplate.getGuildQuests().getGuildQuest().get(i).getGuildQuestId(), guildTemplate);
		}
	}

	public GuildTemplate getGuildTemplateByNpcId(int npcId)
	{
		return guildDataNpcId.get(npcId);
	}

	public GuildTemplate getGuildTemplateByGuildId(int guildId)
	{
		return guildDataGuildId.get(guildId);
	}

	public GuildTemplate getGuildTemplateByQuestId(int questId)
	{
		return guildDataQuestId.get(questId);
	}

	public int size()
	{
		return guildDataNpcId.size();
	}

	public List<GuildTemplate> getGuildTemplates()
	{
		return guildTemplates;
	}

	public void setGuildTemplates(List<GuildTemplate> guildTemplates)
	{
		this.guildTemplates = guildTemplates;
		afterUnmarshal(null, null);
	}
}