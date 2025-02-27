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

package gameserver.controllers.attack;

import commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.SkillElement;
import gameserver.model.gameobjects.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.siege.Influence;
import gameserver.model.templates.item.WeaponType;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.stats.StatFunctions;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttackUtil
{
	/**
	 * @param attacker
	 * @param attacked
	 * @return List<AttackResult>
	 */
	public static List<AttackResult> calculatePhysicalAttackResult(Creature attacker, Creature attacked)
	{
		List<AttackResult> attackList = new ArrayList<AttackResult>();
		
		float damageMultiplier = attacker.getObserveController().getBasePhysicalDamageMultiplier();
		CreatureGameStats<?> gameStats = attacker.getGameStats();

		int damage = StatFunctions.calculateBaseDamageToTarget(attacker, attacked);

		AttackStatus status = calculatePhysicalStatus(attacker, attacked);
		switch(status)
		{
			case DODGE:
				break;
			case BLOCK:
				int shieldDamageReduce = attacked.getGameStats().getCurrentStat(StatEnum.DAMAGE_REDUCE);
				damage -= Math.round((damage * shieldDamageReduce) / 100);
				break;
			case PARRY:
				damage *= 0.6;
			break;
		}

		int criticalReduce = attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE);
		if (calculatePhysicalAttackerStatus(attacker, attacked, 1) == AttackStatus.CRITICAL)
		{
			switch(status)
			{
				case DODGE:
					status = AttackStatus.CRITICAL_DODGE;
					break;
				case BLOCK:
					status = AttackStatus.CRITICAL_BLOCK;
					break;
				case PARRY:
					status = AttackStatus.CRITICAL_PARRY;
					break;
				default:
					status = AttackStatus.CRITICAL;
				break;
			}

			WeaponType weaponType = null;
			if (attacker instanceof Player)
				weaponType = ((Player)attacker).getEquipment().getMainHandWeaponType();
			
			if (weaponType != null)
			damage = calculateWeaponCritical(damage, criticalReduce, weaponType);
		}
		
		damage = Math.round(damage * damageMultiplier);
		damage = adjustDamages(attacker, attacked, damage);
		
		switch (status)
		{
			case DODGE:
			case CRITICAL_DODGE:
				damage = 0;
				break;
			default:
				if (damage <= 0)
					damage = 1;
			break;
		}
		
		int hitCount = Rnd.get(1,gameStats.getCurrentStat(StatEnum.MAIN_HAND_HITS));
		
		attackList.addAll(splitDamage(hitCount, damage, status));

		if(attacker instanceof Player && ((Player)attacker).getEquipment().getSubHandWeaponType() != null)
		{
			int subHandDamage = StatFunctions.calculateSubHandPhysicDamageToTarget(attacker, attacked);
			
			AttackStatus subHandStatus;
			switch(status)
			{
				case DODGE:
					subHandStatus = AttackStatus.SUBHAND_DODGE;
					break;
				case CRITICAL_DODGE:
					subHandStatus = AttackStatus.SUBHAND_CRITICAL_DODGE;
					break;
				case BLOCK:
					subHandStatus = AttackStatus.SUBHAND_BLOCK;
					break;
				case CRITICAL_BLOCK:
					subHandStatus = AttackStatus.SUBHAND_CRITICAL_BLOCK;
					break;
				case PARRY:
					subHandStatus = AttackStatus.SUBHAND_PARRY;
					break;
				case CRITICAL_PARRY:
					subHandStatus = AttackStatus.SUBHAND_CRITICAL_PARRY;
					break;
				case CRITICAL:
					subHandStatus = AttackStatus.SUBHAND_CRITICAL;
					break;
				default:
					subHandStatus = AttackStatus.SUBHAND_NORMALHIT;
				break;
			}
			
			subHandDamage = calculateSubHandResult(attacker, attacked, subHandDamage, subHandStatus);
			subHandDamage = Math.round(subHandDamage * damageMultiplier);
			
			switch (subHandStatus)
			{
				case SUBHAND_DODGE:
				case SUBHAND_CRITICAL_DODGE:
					subHandDamage = 0;
					break;
				default:
					if (subHandDamage <= 0)
						subHandDamage = 1;
				break;
			}

			int subHandHits =  Rnd.get(1,gameStats.getCurrentStat(StatEnum.SUB_HAND_HITS));
			attackList.addAll(splitDamage(subHandHits, subHandDamage, subHandStatus));
		}

		if (CustomConfig.CRITICAL_EFFECTS && status == AttackStatus.CRITICAL)
			launchEffectOnCritical((Player)attacker,attacked);
			attacked.getObserveController().checkShieldStatus(attackList, attacker);
		
		return attackList;
	}

	public static List<AttackResult> calculateMagicalAttackResult(Creature attacker, Creature attacked)
	{
		List<AttackResult> attackList = new ArrayList<AttackResult>();
		
		float damageMultiplier = attacker.getObserveController().getBaseMagicalDamageMultiplier();
		
		SkillElement element = attacker.getAttackType().getElement();
		
		int damage = StatFunctions.calculateMagicalAttackToTarget(attacker, attacked, element);

		// Calculate status
		AttackStatus status = calculateMagicalStatus(attacker, attacked);
		
		if (status == AttackStatus.CRITICAL)
		{
			float criticalReduce = (float)attacked.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE);
			damage *= 1.5f;
			damage = Math.round(damage - (damage * criticalReduce / 1000f));
		}
		
		// Adjusting baseDamages according to attacker and target level
		damage = adjustDamages(attacker, attacked, damage);

		// Damage multiplier
		damage = Math.round(damage * damageMultiplier);
		
		if (damage <= 0)
			damage = 1;
		
		switch (status)
		{
			case RESIST:
			case CRITICAL_RESIST:
				damage = 0;
			break;
		}
		
		attackList.add(new AttackResult(damage, status));
		attacked.getObserveController().checkShieldStatus(attackList, attacker);
		
		return attackList;
	}
	
	public static List<AttackResult> splitDamage(int hitCount, int damage, AttackStatus status)
	{
		List<AttackResult> attackList = new ArrayList<AttackResult>();

		for (int i=0; i < hitCount; i++) 
		{
			int damages = damage;

			if (i!=0)
			{
				damages = Math.round(damage * 0.1f);
			}
			
			attackList.add(new AttackResult(damages, status));
		}
		return attackList;
	}

	/**
	 * used to calculate subhand attack
	 * 
	 * @param attacker
	 * @param attacked
	 * @param damage
	 * @param status
	 * @return
	 */
	private static int calculateSubHandResult(Creature attacker, Creature attacked, int damage, AttackStatus status)
	{
		switch(status)
		{
			case SUBHAND_DODGE:
			case SUBHAND_CRITICAL_DODGE:
				damage = 0;
			break;
			case SUBHAND_BLOCK:
			case SUBHAND_CRITICAL_BLOCK:
				int shieldDamageReduce = attacked.getGameStats().getCurrentStat(StatEnum.DAMAGE_REDUCE);
				damage -= Math.round((damage * shieldDamageReduce) / 100);
			break;
			case SUBHAND_PARRY:
			case SUBHAND_CRITICAL_PARRY:
				damage *= 0.6;
			break;
		}
		
		// Effector status
		int criticalReduce = attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE);
		switch(status)
		{
			case SUBHAND_CRITICAL_DODGE:
			case SUBHAND_CRITICAL_BLOCK:
			case SUBHAND_CRITICAL_PARRY:
			case SUBHAND_CRITICAL:
				WeaponType weaponType = ((Player)attacker).getEquipment().getSubHandWeaponType();
				damage = calculateWeaponCritical(damage, criticalReduce, weaponType);
			break;
		}

		// Adjust damage
		damage = adjustDamages(attacker, attacked, damage);
	
		return damage;
	}
	
	/**
	 * [Critical]
	 *	Spear : x1.8
	 *	Sword : x2.2
	 *	Dagger : x2.3
	 *	Mace : x2.0
	 *	Greatsword : x1.8
	 *	Orb : x1.5
	 *	Spellbook : x1.5
	 *	Bow : x1.7
	 *	Staff : x1.7
	 *
	 * @param damages
	 * @param weaponType
	 * @return
	 */
	private static int calculateWeaponCritical(int damages, int criticalReduce, WeaponType weaponType)
	{
		switch(weaponType)
		{
			case DAGGER_1H:
				damages = Math.round(damages * 2.3f);
				break;
			case SWORD_1H:
				damages = Math.round(damages * 2.2f);
				break;
			case MACE_1H:
				damages = Math.round(damages * 2);
				break;
			case SWORD_2H:
			case POLEARM_2H:
				damages = Math.round(damages * 1.8f);
				break;
			case STAFF_2H:
			case BOW:
				damages = Math.round(damages * 1.7f);
				break;
			default:
				damages = Math.round(damages * 1.5f);
			break;
		}
		/** adjust damage with PSYCHICAL_CRITICAL_DAMAGE_REDUCE
		 * 10 PSYCHICAL_CRITICAL_DAMAGE_REDUCE = -1% from damage
		 */
		damages = Math.round((float)damages - ((float)damages * (float)criticalReduce / 1000f));
		
		return damages;
	}
	
	/**
	 * @param effect
	 * @param skillDamage
	 */
	public static void calculatePhysicalSkillAttackResult(Effect effect, int skillDamage, int bonusDamage, int rng)
	{
		Creature effector = effect.getEffector();
		Creature effected = effect.getEffected();
		
		int damage = StatFunctions.calculatePhysicDamageToTarget(effector, effected, skillDamage, bonusDamage);
		
		// Effected status
		AttackStatus status = calculateSkillPhysicalStatus(effector, effected);
		switch(status)
		{
			case BLOCK:
				int shieldDamageReduce = ((Player)effected).getGameStats().getCurrentStat(StatEnum.DAMAGE_REDUCE);
				damage -= Math.round((damage * shieldDamageReduce) / 100);
				break;
			case PARRY:
				damage *= 0.6;
				break;
			default:
			break;
		}

		int criticalReduce = effect.getEffected().getGameStats().getCurrentStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE);
		if (calculatePhysicalAttackerStatus(effector, effected, effect.getCriticalProb()) == AttackStatus.CRITICAL)
		{
			switch(status)
			{
				case BLOCK:
					status = AttackStatus.CRITICAL_BLOCK;
					break;
				case PARRY:
					status = AttackStatus.CRITICAL_PARRY;
					break;
				default:
					status = AttackStatus.CRITICAL;
				break;
			}
			
			if (effector instanceof Player)
				damage = calculateWeaponCritical(damage, criticalReduce, ((Player)effector).getEquipment().getMainHandWeaponType());
			else
			damage *= 2;
		}
		
		// Multiply damage
		float damageMultiplier = effector.getObserveController().getBasePhysicalDamageMultiplier();
		damage *= damageMultiplier;
		
		// Adjust damamge
		damage = adjustDamages(effector, effected, damage);
		
		// Implementation of random damage for skills like Stunning Shot, etc
		if (rng > 0)
		{
			int randomChance = Rnd.get(100);
			
			switch (rng)
			{
				case 1:
					if (randomChance <= 40)
						damage /= 2f;
					else if (randomChance <= 70)
						damage *= 1.5f;
					break;
				case 2:
					if (randomChance <= 25)
						damage *= 3f;
					break;
				case 3:
					if (randomChance <= 70)
						damage *= 1.5f;
					break;
				case 6:
					if (randomChance <= 30)
						damage *= 2f;
					break;
				
				// TODO: rest of the cases
				default:
					Logger.getLogger(AttackUtil.class).debug("Missing handled RNG: "+rng+" for skillId: "+effect.getSkillId());
				break;
			}
		}
		
		if (damage <= 0)
			damage = 1;
		
		if (CustomConfig.CRITICAL_EFFECTS && status == AttackStatus.CRITICAL)
			launchEffectOnCritical((Player)effector,effected);
			
		calculateEffectResult(effect, effected, damage, status);
	}

	/**
	 * @param effect
	 * @param effected
	 * @param damage
	 * @param status
	 */
	private static void calculateEffectResult(Effect effect, Creature effected, int damage, AttackStatus status)
	{
		AttackResult attackResult = new AttackResult(damage, status, 0, 0, effect.getDamageType());
		effected.getObserveController().checkShieldStatus(Collections.singletonList(attackResult),effect.getEffector());
		effect.setReserved1(attackResult.getDamage());
		effect.setAttackStatus(attackResult.getAttackStatus());
		effect.setReflectorDamage(attackResult.getReflectedDamage());
		effect.setReflectorSkillId(attackResult.getSkillId());
		effect.setShieldType(attackResult.getShieldType());
	}

	/**
	 * @param effect
	 * @param skillDamage
	 * @param element
	 */
	public static void calculateMagicalSkillAttackResult(Effect effect, int skillDamage, SkillElement element, int bonusDamage, boolean applyKnowledge)
	{
		Creature effector = effect.getEffector();
		Creature effected = effect.getEffected();
		
		float damageMultiplier = effector.getObserveController().getBaseMagicalDamageMultiplier();
				
		int damage = StatFunctions.calculateMagicDamageToTarget(effector, effected, skillDamage, bonusDamage, element, applyKnowledge);  //TODO SkillElement
		
		float criticalReduce = (float)effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE);
		AttackStatus status = calculateMagicalEffectorStatus(effect);
		if (status == AttackStatus.CRITICAL)
		{
			damage *= 1.5f;
			damage = Math.round(damage - (damage * criticalReduce / 1000f));
		}
		
		// Adjusting baseDamages according to attacker and target level
		damage = adjustDamages(effector, effected, damage);
		
		// Damage multiplier
		damage = Math.round(damage * damageMultiplier);
		
		if (damage <= 0)
			damage = 1;
		
		calculateEffectResult(effect, effected, damage, status);
	}
	/**
	 * calculate damage for damage over time effects(bleed,poison,spellatk)
	 * @param effect
	 * @param skillDamage
	 * @param element
	 * @return
	 */
	
	public static int calculateMagicalOverTimeResult(Effect effect, int skillDamage, SkillElement element, int position, boolean canCrit)
	{
		Creature effector = effect.getEffector();
		Creature effected = effect.getEffected();
		
		float damageMultiplier = effector.getObserveController().getBaseMagicalDamageMultiplier();		
		int damage = StatFunctions.calculateMagicDamageToTarget(effector, effected, skillDamage, 0, element, true);  //TODO SkillElement

		
		float criticalReduce = (float)effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE);
		
		AttackStatus status = AttackStatus.NORMALHIT;
			
		if (position == 1 || canCrit)
			status = calculateMagicalEffectorStatus(effect);
		else
			status = effect.getAttackStatus();
		
		
		if (status == AttackStatus.CRITICAL)
		{
			damage *= 1.5f;
			damage = Math.round(damage - (damage * criticalReduce / 1000f));
		}
	
		
		// Adjusting baseDamages according to attacker and target level
		damage = adjustDamages(effector, effected, damage);

		// Damage multiplier
		damage = Math.round(damage * damageMultiplier);
		
		if (damage <= 0)
			damage = 1;
		
		return damage;
	}

	/**
	 * Manage attack status rate
	 * @source http://www.aionsource.com/forum/mechanic-analysis/42597-character-stats-xp-dp-origin-gerbator-team-july-2009-a.html
	 * @return AttackStatus
	 */
	public static AttackStatus calculatePhysicalStatus(Creature attacker, Creature attacked)
	{		
		if( Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalDodgeRate(attacker, attacked, 0) )
			return AttackStatus.DODGE;
		
		if( attacked instanceof Player && ((Player)attacked).getEquipment().getMainHandWeaponType() != null                  // PARRY can only be done with weapon, also weapon can have humanoid mobs,
			&& Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalParryRate(attacker, attacked) ) // But for now there isnt implementation of monster category
			return AttackStatus.PARRY;

		if( attacked instanceof Player && ((Player) attacked).getEquipment().isShieldEquipped()
			&& Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalBlockRate(attacker, attacked) )
			return AttackStatus.BLOCK;

		return AttackStatus.NORMALHIT;
	}
	
	public static AttackStatus calculateMagicalStatus(Creature attacker, Creature attacked)
	{		
		AttackStatus status = AttackStatus.NORMALHIT;
		if( Rnd.get( 0, 100 ) < StatFunctions.calculateMagicalResistRate(attacker, attacked, 0) )
			status = AttackStatus.RESIST;
		
		if(Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalCriticalRate(attacker, attacked, 1))
		{
			if (status == AttackStatus.RESIST)
				status = AttackStatus.CRITICAL_RESIST;
			else
				status = AttackStatus.CRITICAL;
		}
		
		return status;
	}	
	
	public static AttackStatus calculateSkillPhysicalStatus(Creature attacker, Creature attacked)
	{		
		if( attacked instanceof Player && ((Player)attacked).getEquipment().getMainHandWeaponType() != null                  // PARRY can only be done with weapon, also weapon can have humanoid mobs,
			&& Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalParryRate(attacker, attacked) ) // but for now there isnt implementation of monster category
			return AttackStatus.PARRY;

		if( attacked instanceof Player && ((Player) attacked).getEquipment().isShieldEquipped()
			&& Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalBlockRate(attacker, attacked) )
			return AttackStatus.BLOCK;

		return AttackStatus.NORMALHIT;
	}
	
	public static AttackStatus calculatePhysicalAttackerStatus(Creature attacker, Creature attacked, float criticalProb)
	{
		if( attacker instanceof Player && ((Player)attacker).getEquipment().getMainHandWeaponType() != null           // CRITICAL can only be done with weapon, weapon can have humanoid mobs also,
			&& Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalCriticalRate(attacker, attacked, criticalProb) ) // but for now there isnt implementation of monster category
			return AttackStatus.CRITICAL;
		
		return AttackStatus.NORMALHIT;
	}
	
	public static AttackStatus calculateMagicalEffectorStatus(Effect effect)
	{
		if(Rnd.get( 0, 100 ) < StatFunctions.calculateMagicalCriticalRate(effect.getEffector(), effect.getEffected(), effect.getCriticalProb()))
			return AttackStatus.CRITICAL;
		else
			return AttackStatus.NORMALHIT;
	}
	
	/**
	 * adjust baseDamages according to their level || is PVP?
	 *
	 * @ref:
	 *
	 * @param attacker lvl
	 * @param target lvl
	 * @param baseDamages
	 *
	 */
	public static int adjustDamages(Creature attacker, Creature target, int damages) {

		int attackerLevel = attacker.getLevel();
		int targetLevel = target.getLevel();
		int baseDamages = damages;

		// Fix this for better monster target condition please
		if ( (attacker instanceof Player) && target instanceof Npc) 
		{
			if(targetLevel > attackerLevel) 
			{
				float multipler = 0.0f;
				int differ = (targetLevel - attackerLevel);

				if( differ > 2 && differ < 10 ) 
				{
					multipler = (differ - 2f) / 10f;
					baseDamages -= Math.round((baseDamages * multipler));
				}
				else if (differ >= 10)
					baseDamages -= Math.round((baseDamages * 0.80f));
			}
		} // End of damage to monster

		// PVP damages is capped at 50% of the actual baseDamage
		else if((attacker instanceof Player)
			&& (target instanceof Player || target instanceof Summon)) 
		{
			baseDamages = Math.round(baseDamages * 0.60f);
			float pvpAttackBonus = attacker.getGameStats().getCurrentStat(StatEnum.PVP_ATTACK_RATIO) * 0.001f;
			float pvpDefenceBonus = target.getGameStats().getCurrentStat(StatEnum.PVP_DEFEND_RATIO) * 0.001f;
			baseDamages = Math.round((baseDamages * (1 + pvpAttackBonus - pvpDefenceBonus)) * StatFunctions.calculatePvPMultipler((Player)attacker));
			
			/**
			 *  Blessing of Azphel/Triniel
			 *
			 *	other 	my		boost
			 *
			 *	81+   	10-		20%
			 *	81+   	11+		15%
			 *	71+   	10- 	15%
			 *	71+   	11+ 	10%
			 */
			if(attacker.getActingCreature() instanceof Player && target.getActingCreature() instanceof Player)
			{
				Race attRace = ((Player)attacker.getActingCreature()).getCommonData().getRace();
				Race tarRace = ((Player)target.getActingCreature()).getCommonData().getRace();
				
				if (attRace != tarRace)
				{
					float asmo = Influence.getInstance().getAsmos();
					float elyos = Influence.getInstance().getElyos();
					switch (attRace)
					{
						case ASMODIANS:
							if (elyos >= 0.81f && asmo <= 0.10f)
								baseDamages *= 1.2;
							else if (elyos >= 0.81f || (elyos >= 0.71f && asmo <= 0.10f))
								baseDamages *= 1.15;
							else if(elyos >= 0.71f)
								baseDamages *= 1.1;
							break;
						case ELYOS:
							if (asmo >= 0.81f && elyos <= 0.10f)
								baseDamages *= 1.2;
							else if (asmo >= 0.81f || (asmo >= 0.71f && elyos <= 0.10f))
								baseDamages *= 1.15;
							else if(asmo >= 0.71f)
								baseDamages *= 1.1;
						break;
					}
				}
				
				// Custom DMG reduction according to lvl of attacker and target
				if(targetLevel > attackerLevel && CustomConfig.DMG_REDUCTION_LVL_DIFF_PVP) 
				{
					int diff = targetLevel - attackerLevel;
				
					if (diff > 2 && diff < 15)
					{
						baseDamages -=(diff * 0.04f);
					}
					else if (diff >= 15)
					{
						baseDamages *= 0.4; 
					}
				}
				
			}
		}
		else if((attacker instanceof Summon) || (attacker instanceof Servant) || (attacker instanceof Trap) || (attacker instanceof NpcWithCreator)
			&& (target instanceof Player || target instanceof Summon)) {
			baseDamages = Math.round(baseDamages * 0.60f);
			float pvpAttackBonus = attacker.getGameStats().getCurrentStat(StatEnum.PVP_ATTACK_RATIO) * 0.001f;
			float pvpDefenceBonus = target.getGameStats().getCurrentStat(StatEnum.PVP_DEFEND_RATIO) * 0.001f;
			baseDamages = Math.round(baseDamages * (1 + pvpAttackBonus - pvpDefenceBonus));
			}

		return baseDamages;
	}
	
	public static void launchEffectOnCritical(Player attacker, Creature attacked)
	{
		int skillId = 0;
		switch(attacker.getEquipment().getMainHandWeaponType())
		{
			case POLEARM_2H:
			case STAFF_2H:
			case SWORD_2H:
				skillId = 8218;
				break;
			case BOW:
				skillId = 8217;
				break;
		}
		if (skillId == 0)
			return;
		
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		if (template == null)
			return;
		
		if (Rnd.get(100) > 10) // Hard Coded - 15% chance
			return;
		
		Effect e = new Effect(attacker, attacked, template, template.getLvl(), template.getEffectsDuration());
		e.initialize();
		e.applyEffect();
	}
}