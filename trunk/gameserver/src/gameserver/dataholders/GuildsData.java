/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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