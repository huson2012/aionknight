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

import gameserver.skill.model.SkillTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "skill_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillData
{	
	@XmlElement(name = "skill_template")
	private List<SkillTemplate> skillTemplates;
	private TIntObjectHashMap<SkillTemplate> skillData = new TIntObjectHashMap<SkillTemplate>();
	private TIntObjectHashMap<List<SkillTemplate>> delayData = new TIntObjectHashMap<List<SkillTemplate>>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		skillData.clear();
		delayData.clear();
		for(SkillTemplate skillTempalte: skillTemplates)
		{
			skillData.put(skillTempalte.getSkillId(), skillTempalte);
			
			List<SkillTemplate> sts = delayData.get(skillTempalte.getDelayId());
			if (sts == null)
				sts = new ArrayList<SkillTemplate>();
			sts.add(skillTempalte);
			delayData.put(skillTempalte.getDelayId(), sts);
		}
	}

	public SkillTemplate getSkillTemplate(int skillId)
	{
		return skillData.get(skillId);
	}
	
	public List<SkillTemplate> getSkillTemplatesForDelayId(int delayId)
	{
		return delayData.get(delayId);
	}

	public int size()
	{
		return skillData.size();
	}

	public List<SkillTemplate> getSkillTemplates()
	{
		return skillTemplates;
	}

	public void setSkillTemplates(List<SkillTemplate> skillTemplates)
	{
		this.skillTemplates = skillTemplates;
		afterUnmarshal(null, null);
	}
}