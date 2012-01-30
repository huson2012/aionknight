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

import gameserver.model.templates.npcskill.NpcSkillList;
import gnu.trove.TIntObjectHashMap;
import org.apache.log4j.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "npc_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcSkillData
{
	@XmlElement(name = "npcskills")
	private List<NpcSkillList> npcSkills;
	private TIntObjectHashMap<NpcSkillList>	npcSkillData = new TIntObjectHashMap<NpcSkillList>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(NpcSkillList npcSkill : npcSkills)
		{
			npcSkillData.put(npcSkill.getNpcId(), npcSkill);
			
			if(npcSkill.getNpcSkills() == null)
				Logger.getLogger(NpcSkillData.class).error("NO SKILL");
		}
		
	}
	
	public int size()
	{
		return npcSkillData.size();
	}
	
	public NpcSkillList getNpcSkillList(int id)
	{
		return npcSkillData.get(id);
	}
}