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
