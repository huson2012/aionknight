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

import gameserver.model.templates.GuildTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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