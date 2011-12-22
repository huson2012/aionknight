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

package gameserver.ai.desires.impl;

import commons.utils.Rnd;
import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.dataholders.DataManager;
import gameserver.model.ShoutEventType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.npcskill.NpcSkillList;
import gameserver.model.templates.npcskill.NpcSkillTemplate;
import gameserver.services.NpcShoutsService;
import gameserver.skill.SkillEngine;
import gameserver.skill.effect.*;
import gameserver.skill.model.Skill;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.exceptionhandlers.exception_enums;
import java.util.List;

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
