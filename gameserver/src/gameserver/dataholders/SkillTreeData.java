/**
 * This file is part of Aion-Knight [http://www.aion-knight.ru]
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
import gameserver.model.PlayerClass;
import gameserver.model.Race;
import gameserver.skill.model.learn.SkillClass;
import gameserver.skill.model.learn.SkillLearnTemplate;
import gameserver.skill.model.learn.SkillRace;

@XmlRootElement(name = "skill_tree")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillTreeData
{	
	@XmlElement(name = "skill")
	private List<SkillLearnTemplate> skillTemplates;
	
	private final TIntObjectHashMap<ArrayList<SkillLearnTemplate>> templates = new TIntObjectHashMap<ArrayList<SkillLearnTemplate>>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(SkillLearnTemplate template : skillTemplates)
		{
			addTemplate(template);
		}
		skillTemplates = null;
	}
	
	private void addTemplate(SkillLearnTemplate template)
	{
		SkillRace race = template.getRace();
		if(race == null)
			race = SkillRace.ALL;

		int hash = makeHash(template.getClassId().ordinal(), race.ordinal(), template.getMinLevel());
		ArrayList<SkillLearnTemplate> value = templates.get(hash);
		if(value == null)
		{
			value = new ArrayList<SkillLearnTemplate>();
			templates.put(hash, value);
		}
			
		value.add(template);
	}

	public TIntObjectHashMap<ArrayList<SkillLearnTemplate>> getTemplates()
	{
		return templates;
	}

	public SkillLearnTemplate[] getTemplatesFor(PlayerClass playerClass, int level, Race race)
	{
		List<SkillLearnTemplate> newSkills = new ArrayList<SkillLearnTemplate>();
		
		List<SkillLearnTemplate> classRaceSpecificTemplates = 
			templates.get(makeHash(playerClass.ordinal(), race.ordinal(), level));
		List<SkillLearnTemplate> classSpecificTemplates = 
			templates.get(makeHash(playerClass.ordinal(), SkillRace.ALL.ordinal(), level));
		List<SkillLearnTemplate> generalTemplates = 
			templates.get(makeHash(SkillClass.ALL.ordinal(), SkillRace.ALL.ordinal(), level));
		
		if(classRaceSpecificTemplates != null)
			newSkills.addAll(classRaceSpecificTemplates);
		if(classSpecificTemplates != null)
			newSkills.addAll(classSpecificTemplates);
		if(generalTemplates != null)
			newSkills.addAll(generalTemplates);
		
		return newSkills.toArray(new SkillLearnTemplate[newSkills.size()]);
	}

	public int size()
	{
		int size = 0;
		for(Integer key : templates.keys())
			size += templates.get(key).size();
		return size;
	}
	
	private static int makeHash(int classId, int race, int level)
	{
		int result = classId << 8;
        result = (result | race) << 8;
        return result | level;
	}
}