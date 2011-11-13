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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import gameserver.skill.model.SkillTemplate;

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