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

package gameserver.skill;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.skill.model.ActivationAttribute;
import gameserver.skill.model.Skill;
import gameserver.skill.model.SkillTemplate;

public class SkillEngine
{	
	public static final SkillEngine skillEngine = new SkillEngine();
	
	/**
	 * should not be instantiated directly
	 */
	private SkillEngine()
	{	
		
	}
	
	/**
	 * This method is used for skills that were learned by player
	 * 
	 * @param player
	 * @param skillId
	 * @return Skill
	 */
	public Skill getSkillFor(Player player, int skillId, VisibleObject firstTarget)
	{
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		
		if(template == null)
			return null;
		
		// player doesn't have such skill and ist not provoked
		if(template.getActivationAttribute() != ActivationAttribute.PROVOKED)
		{
			if(!player.getSkillList().isSkillPresent(skillId))
				return null;
		}
		
		
		Creature target = null;
		if(firstTarget instanceof Creature)
			target = (Creature) firstTarget;

		return new Skill(template, player, target);
	}
	
	/**
	 * This method is used for not learned skills (item skills etc)
	 * 
	 * @param creature
	 * @param skillId
	 * @param skillLevel
	 * @return Skill
	 */
	public Skill getSkill(Creature creature, int skillId, int skillLevel, VisibleObject firstTarget)
	{
		return this.getSkill(creature, skillId, skillLevel, firstTarget, null);
	}
	
	public Skill getSkill(Creature creature, int skillId, int skillLevel, VisibleObject firstTarget, ItemTemplate itemTemplate)
	{
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		
		if(template == null)
			return null;
		
		Creature target = null;
		if(firstTarget instanceof Creature)
			target = (Creature) firstTarget;
		return new Skill(template, creature, skillLevel, target, itemTemplate);
	}

	public static SkillEngine getInstance()
	{
		return skillEngine;
	}
}