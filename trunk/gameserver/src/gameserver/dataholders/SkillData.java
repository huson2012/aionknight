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