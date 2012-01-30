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

import gameserver.model.templates.petskill.PetSkillTemplate;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pet_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetSkillData
{
	@XmlElement(name = "pet_skill")
	private List<PetSkillTemplate> petSkills;
	private TIntObjectHashMap<TIntIntHashMap> petSkillData = new TIntObjectHashMap<TIntIntHashMap>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(PetSkillTemplate petSkill : petSkills)
		{
			TIntIntHashMap orderSkillMap = petSkillData.get(petSkill.getOrderSkill());
			if(orderSkillMap == null)
			{
				orderSkillMap = new TIntIntHashMap();
				petSkillData.put(petSkill.getOrderSkill(), orderSkillMap);
			}
				orderSkillMap.put(petSkill.getPetId(), petSkill.getSkillId());
		}		
	}
	
	public int size()
	{
		return petSkillData.size();
	}
	
	public int getPetOrderSkill(int orderSkill, int petNpcId)
	{
		try{
			return petSkillData.get(orderSkill).get(petNpcId);
		} catch(NullPointerException ex) {
			return -1;
		}		
	}
}