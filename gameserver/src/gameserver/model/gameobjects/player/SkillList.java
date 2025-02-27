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

package gameserver.model.gameobjects.player;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.templates.item.ArmorType;
import gameserver.model.templates.item.WeaponType;
import gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import gameserver.skill.effect.*;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillList
{
	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SkillList.class);

	public static final String[]	split	= null;

	/**
	 * Container of skilllist, position to xml.
	 */
	private final Map<Integer, SkillListEntry> skills;
	
	private final List<SkillListEntry> deletedSkills;
	
	/**
	 * Current weapon mastery skills
	 */
	private final Map<WeaponType, Integer> weaponMasterySkills = new HashMap<WeaponType, Integer>();
	/**
	 * Current armor mastery skills
	 */
	private final Map<ArmorType, Integer> armorMasterySkills = new HashMap<ArmorType, Integer>();
	/**
	 * Current dual weapon mastery skill
	 */
	private int dualMasterySkill = 0;
	/**
	 * Current shield mastery skill
	 */
	private int shieldMasterySkill = 0;

	/**
	 * Creates an empty skill list
	 */
	public SkillList()
	{
		this.skills = new HashMap<Integer, SkillListEntry>();
		this.deletedSkills = new ArrayList<SkillListEntry>();
	}

	/**
	 * Create new instance of <tt>SkillList</tt>.
	 * @param arg 
	 */
	public SkillList(Map<Integer, SkillListEntry> arg)
	{
		this.skills = arg;
		this.deletedSkills = new ArrayList<SkillListEntry>();
		calculateUsedWeaponMasterySkills();
		calculateUsedArmorMasterySkills();
		calculateUsedDualMasterySkills();
		calculateUsedShieldMasterySkills();
	}
	
	/**
	 * Returns array with all skills
	 * @return SkillListEntry[]
	 */
	public SkillListEntry[] getAllSkills()
	{
		return skills.values().toArray(new SkillListEntry[skills.size()]);
	}
	
	/**
	 * 
	 * @return SkillListEntry[]
	 */
	public SkillListEntry[] getDeletedSkills()
	{
		return deletedSkills.toArray(new SkillListEntry[deletedSkills.size()]);
	}
	
	/**
	 * @param skillId
	 * @return SkillListEntry
	 */
	public SkillListEntry getSkillEntry(int skillId)
	{
		return skills.get(skillId);
	}

	/**
	 * Add Skill to the collection.
	 * @return <tt>true</tt> if Skill addition was successful, and it can be stored into database.
	 *     Otherwise <tt>false</tt>.
	 */
	public synchronized boolean addSkill(Player player, int skillId, int skillLevel, boolean msg)
	{
		SkillListEntry existingSkill = skills.get(skillId);
		if (existingSkill != null)
		{
			if(existingSkill.getSkillLevel() >= skillLevel)
			{
				return false;
			}
			existingSkill.setSkillLvl(skillLevel);
		}
		else
		{
			skills.put(skillId, new SkillListEntry(skillId, false, skillLevel, PersistentState.NEW));
		}
		if (msg)
			sendMessage(player, skillId, true);
		
		SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		
		//do passive skills recalculations
		if(skillTemplate.isPassive())
		{
			calculateUsedWeaponMasterySkills();
			calculateUsedArmorMasterySkills();
			calculateUsedDualMasterySkills();
			calculateUsedShieldMasterySkills();
		}
		return true;
	}
	
	/**
	 * @param skill
	 */
	public void addSkill(SkillListEntry skill)
	{
		SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skill.getSkillId());
		
		skills.put(skill.getSkillId(), skill);
		
		//do passive skills recalculations
		if(skillTemplate.isPassive())
		{
			calculateUsedDualMasterySkills();
		}
	}

	/**
	 * 
	 * @param player
	 * @param skillId
	 * @param xpReward
	 * @return
	 */
	public boolean addSkillXp(Player player, int skillId, int xpReward)
	{
		SkillListEntry  skillEntry =  getSkillEntry(skillId);
		switch(skillEntry.getSkillId())
		{
			case 30001:
				if(skillEntry.getSkillLevel() == 49)
					return false;
			case 30002:
			case 30003:
				switch (skillEntry.getSkillLevel())
				{
					case 99:
					case 199:
					case 299:
					case 399:
					case 499:
						return false;
				}
				player.getRecipeList().autoLearnRecipe(player, skillId, skillEntry.getSkillLevel());
				break;
			case 40001:
			case 40002:
			case 40003:
			case 40004:
			case 40007:
			case 40008:
				switch (skillEntry.getSkillLevel())
				{
					case 99:
					case 199:
					case 299:
					case 399:
					case 449:
					case 499:
					case 549:
						return false;
				}
				player.getRecipeList().autoLearnRecipe(player, skillId, skillEntry.getSkillLevel());
				break;
		}
		boolean updateSkill = skillEntry.addSkillXp(xpReward);
		if(updateSkill)
			sendMessage(player, skillId, false);
		return true;
	}
	/**
	 * Checks whether player have skill with specified skillId
	 * 
	 * @param skillId
	 * @return true or false
	 */
	public boolean isSkillPresent(int skillId)
	{
		return skills.containsKey(skillId);
	}
	
	/**
	 * @param skillId
	 * @return level of the skill with specified skillId
	 * 
	 */
	public int getSkillLevel(int skillId)
	{
		return skills.get(skillId).getSkillLevel();
	}
	
	/**
	 * 
	 * @param skillId
	 * @return
	 */
	public synchronized boolean removeSkill(int skillId)
	{
		SkillListEntry entry = skills.get(skillId);
		if(entry != null)
		{
			entry.setPersistentState(PersistentState.DELETED);
			deletedSkills.add(entry);
			skills.remove(skillId);
		}	
		if (entry != null && entry.isStigma())
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(entry.getSkillId());
			//do passive skills recalculations
			if(skillTemplate.isPassive())
				calculateUsedDualMasterySkills();
		}
		return entry != null;
	}
	/**
	 * Returns count of available skillist.
	 * @return count of available skillist.
	 */
	public int getSize()
	{
		return skills.size();
	}
	
	/**
	 * 
	 * @param player
	 * @param skillId
	 */
	private void sendMessage(Player player , int skillId, boolean craftGrade)
	{
		switch (skillId)
		{
			case 30001:
			case 30002:
			case 30003:
				if (craftGrade)
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330004));
				else
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330005));
				break;
			case 40001:
			case 40002:
			case 40003:
			case 40004:
			case 40005:
			case 40006:
			case 40007:
			case 40008:
			case 40009:
				if (craftGrade)
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330004));
				else
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330053));
				break;
			default:
				PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1300050));
		}
	}
	
	/**
	 * Calculates weapon mastery skills that will used during equip
	 */
	private void calculateUsedWeaponMasterySkills()
	{		
		Map<WeaponType, Integer> skillLevels = new HashMap<WeaponType, Integer>();
		for(SkillListEntry skillListEntry : getAllSkills())
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId());
			if(skillTemplate == null)
			{
				logger.warn("CHECKPOINT: no skill template found for " + skillListEntry.getSkillId());
				continue;
			}
			
			if(skillTemplate.isPassive())
			{
				if(skillTemplate.getEffects() == null)
					continue;
				
				EffectTemplate template = null;
				if((template = skillTemplate.getEffectTemplate(1)) instanceof WeaponMasteryEffect)
				{
					WeaponMasteryEffect wme = (WeaponMasteryEffect) template;
					if(skillLevels.get(wme.getWeaponType()) == null
						|| skillLevels.get(wme.getWeaponType()) < wme.getBasicLvl())
					{
						skillLevels.put(wme.getWeaponType(), wme.getBasicLvl());
						weaponMasterySkills.put(wme.getWeaponType(), skillTemplate.getSkillId());
					}
				}
			}
		}
	}
	
	/**
	 * Calculates armor mastery skills that will used during equip
	 */
	private void calculateUsedArmorMasterySkills()
	{		
		Map<ArmorType, Integer> skillLevels = new HashMap<ArmorType, Integer>();
		for(SkillListEntry skillListEntry : getAllSkills())
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId());
			if(skillTemplate == null)
			{
				logger.warn("CHECKPOINT: no skill template found for " + skillListEntry.getSkillId());
				continue;
			}
			
			if(skillTemplate.isPassive())
			{
				if(skillTemplate.getEffects() == null)
					continue;
				
				EffectTemplate template = null;
				if((template = skillTemplate.getEffectTemplate(1)) instanceof ArmorMasteryEffect)
				{
					ArmorMasteryEffect ame = (ArmorMasteryEffect) template;
					if(skillLevels.get(ame.getArmorType()) == null
						|| skillLevels.get(ame.getArmorType()) < ame.getBasicLvl())
					{
						skillLevels.put(ame.getArmorType(), ame.getBasicLvl());
						armorMasterySkills.put(ame.getArmorType(), skillTemplate.getSkillId());
					}
				}
			}
		}
	}
	
	/**
	 * Calculates dual weapon mastery skills that will used during equip
	 */
	private void calculateUsedDualMasterySkills()
	{		
		int dualMasteryLevel = 0;
		dualMasterySkill = 0;
		for(SkillListEntry skillListEntry : getAllSkills())
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId());
			if(skillTemplate == null)
			{
				logger.warn("CHECKPOINT: no skill template found for " + skillListEntry.getSkillId());
				continue;
			}
			
			if(skillTemplate.isPassive())
			{
				if(skillTemplate.getEffects() == null)
					continue;
				
				EffectTemplate template = null;
				if((template = skillTemplate.getEffectTemplate(1)) instanceof DualMasteryEffect)
				{
					DualMasteryEffect dme = (DualMasteryEffect) template;
					if(dualMasteryLevel <= dme.getBasicLvl())
					{
						dualMasteryLevel = dme.getBasicLvl();
						dualMasterySkill = skillTemplate.getSkillId();
					}
				}
			}
		}
	}
	
	/**
	 * Calculates shield mastery skill that will used during equip
	 */
	private void calculateUsedShieldMasterySkills()
	{		
		int skillLevel = 0;
		for(SkillListEntry skillListEntry : getAllSkills())
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId());
			if(skillTemplate == null)
			{
				logger.warn("CHECKPOINT: no skill template found for " + skillListEntry.getSkillId());
				continue;
			}
			
			if(skillTemplate.isPassive())
			{
				if(skillTemplate.getEffects() == null)
					continue;
				
				EffectTemplate template = null;
				if((template = skillTemplate.getEffectTemplate(1)) instanceof ShieldMasteryEffect)
				{
					ShieldMasteryEffect sme = (ShieldMasteryEffect) template;
					if(skillLevel < sme.getBasicLvl())
					{
						skillLevel = sme.getBasicLvl();
						shieldMasterySkill = skillTemplate.getSkillId();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param weaponType
	 * @return
	 */
	public Integer getWeaponMasterySkill(WeaponType weaponType)
	{
		return weaponMasterySkills.get(weaponType);
	}

	public Integer getDualMasterySkill()
	{
		return dualMasterySkill;
	}	
	/**
	 * 
	 * @param armorType
	 * @return
	 */
	public Integer getArmorMasterySkill(ArmorType armorType)
	{
		return armorMasterySkills.get(armorType);
	}

	public Integer getShieldMasterySkill()
	{
		return shieldMasterySkill;
	}
}
