/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.ai.desires.impl;

import java.util.List;

import org.openaion.commons.utils.Rnd;

import ru.aionknight.gameserver.ai.AI;
import ru.aionknight.gameserver.ai.desires.AbstractDesire;
import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.ShoutEventType;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.Monster;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.templates.npcskill.NpcSkillList;
import ru.aionknight.gameserver.model.templates.npcskill.NpcSkillTemplate;
import ru.aionknight.gameserver.services.NpcShoutsService;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.skill.effect.BackDashEffect;
import ru.aionknight.gameserver.skill.effect.EffectTemplate;
import ru.aionknight.gameserver.skill.effect.FpAttackEffect;
import ru.aionknight.gameserver.skill.effect.FpAttackInstantEffect;
import ru.aionknight.gameserver.skill.effect.PulledEffect;
import ru.aionknight.gameserver.skill.effect.RandomMoveLocEffect;
import ru.aionknight.gameserver.skill.effect.StaggerEffect;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.skill.model.SkillTemplate;
import ru.aionknight.gameserver.utils.ThreadPoolManager;
import ru.aionknight.gameserver.utils.exceptionhandlers.exception_enums;


/**
 * @author ATracer
 * 
 */
public class SkillUseDesire extends AbstractDesire
{

	protected Creature		owner;
	private NpcSkillList	skillList;
	private boolean			InitialSkillCasted	= false;

	/**
	 * @param owner
	 * @param desirePower
	 */
	public SkillUseDesire(Creature owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		this.skillList = ((Npc) owner).getNpcSkillList();
	}

	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if(owner.isCasting())
			return true;

		/**
		 * Demo mode - take random skill
		 */
		List<NpcSkillTemplate> skills = skillList.getNpcSkills();
		NpcSkillTemplate npcSkill = skills.get(Rnd.get(0, skills.size() - 1));

		// Temporary hack to disable GeoData and KnockBack/Stagger skills from NPCs
		// While we're there, check for pull or crash effects
		boolean acceptedSkill = false;
		boolean initialForcedSkill = false;
		int initialForcedSkillID = 0;
		int initialForcedSkillLvl = 0;
		for(int i = 0; i < 10; i++)
		{
			SkillTemplate sT = DataManager.SKILL_DATA.getSkillTemplate(npcSkill.getSkillid());
			if(sT != null && sT.getEffects() != null)
			{
				boolean hasDangerous = false;
				for(EffectTemplate e : sT.getEffects().getEffects())
				{
					if((e instanceof BackDashEffect || e instanceof RandomMoveLocEffect || e instanceof StaggerEffect))
						hasDangerous = true;
					if(e instanceof PulledEffect || e instanceof FpAttackEffect || e instanceof FpAttackEffect
						|| e instanceof FpAttackInstantEffect)
					{
						initialForcedSkill = true;
						initialForcedSkillID = sT.getSkillId();
						initialForcedSkillLvl = sT.getLvl();
					}
				}
				if(!hasDangerous)
				{
					acceptedSkill = true;
					break;
				}
			}
			npcSkill = skills.get(Rnd.get(0, skills.size() - 1));
		}
		if(!acceptedSkill)
			return false;

		/**
		 * Cast initial skill once
		 */
		if(initialForcedSkill == true && InitialSkillCasted == false)
		{
			Skill initialskill = SkillEngine.getInstance().getSkill(owner, initialForcedSkillID, initialForcedSkillLvl,
				owner.getTarget());
			initialskill.useSkill();
			InitialSkillCasted = true;
		}

		/**
		 * Demo mode - use probability from template
		 */

		int skillProbability = npcSkill.getProbability();
		if(Rnd.get(0, 100) < skillProbability)
		{
			Skill skill = SkillEngine.getInstance().getSkill(owner, npcSkill.getSkillid(), npcSkill.getSkillLevel(),
				owner.getTarget());

			if(skill != null)
			{
				if(owner instanceof Npc && owner.getTarget() instanceof Creature)
				{
					if(skill.getSkillTemplate() != null && skill.getSkillTemplate().getDuration() > 0)
						NpcShoutsService.getInstance().handleEvent((Npc) owner, (Creature) owner.getTarget(),
							ShoutEventType.CAST);
					else if(skill.getSkillTemplate() != null)
						NpcShoutsService.getInstance().handleEvent((Npc) owner, (Creature) owner.getTarget(),
							ShoutEventType.SKILL);
				}
				skill.useSkill();
			}

			// Iron Explosive Mine
			// 1. Gets aggro on player
			// 2. Casts skill and explodes
			// 3. Wait 1 sec then kill the bomb (one-shot NPC)

			// TODO : specific controller needed, there is much more than one npc id for mines.
			if(owner != null && owner instanceof Monster
				&& ((Monster) owner).getNpcId() == exception_enums.NPC_SIEGE_MINE_I)
			{
				ThreadPoolManager.getInstance().schedule(new Runnable(){

					@Override
					public void run()
					{
						owner.getController().die();
					}
				}, 500);
			}

		}

		return true;
	}

	@Override
	public void onClear()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public int getExecutionInterval()
	{
		return 1;
	}
}
