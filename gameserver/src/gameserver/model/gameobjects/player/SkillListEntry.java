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

public class SkillListEntry
{
	private int skillId;
	private int skillLvl;
	private boolean isStigma;
	
	/**
	 * for crafting skills
	 */
	private int currentXp;
	
	private PersistentState persistentState;

	public SkillListEntry(int skillId, boolean isStigma, int skillLvl, PersistentState persistentState)
	{
		this.skillId = skillId;
		this.skillLvl = skillLvl;
		this.isStigma = isStigma;
		this.persistentState = persistentState;
	}

	/**
	 * @return the skillId
	 */
	public int getSkillId()
	{
		return skillId;
	}

	/**
	 * @return the skillLvl
	 */
	public int getSkillLevel()
	{
		return skillLvl;
	}
	
	/**
	 * 
	 * @return isStigma
	 */
	public boolean isStigma()
	{
		return this.isStigma;
	}
	
	/**
	 * 
	 * @return the skill name
	 */
	public String getSkillName()
	{
		return DataManager.SKILL_DATA.getSkillTemplate(skillId).getName();
	}

	/**
	 * @param skillLvl the skillLvl to set
	 */
	public void setSkillLvl(int skillLvl)
	{
		this.skillLvl = skillLvl;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * @return The skill extra lvl
	 */
	public int getExtraLvl()
	{
		switch(skillId)
		{
			case 30002:
			case 30003:
				return skillLvl/100;
			case 40001:
			case 40002:
			case 40003:
			case 40004:
			case 40007:
			case 40008:
				if(skillLvl > 449 && skillLvl < 500)
					return 5;
				else if(skillLvl > 499 && skillLvl < 550)
					return 6;
				else
					return skillLvl/100;
		}
		return 0;
	}
	/**
	 * @return the currentXp
	 */
	public int getCurrentXp()
	{
		return currentXp;
	}

	/**
	 * @param currentXp the currentXp to set
	 */
	public void setCurrentXp(int currentXp)
	{
		this.currentXp = currentXp;
	}
	
	/**
	 * 
	 * @param xp
	 */
	public boolean addSkillXp(int xp)
	{
		if(currentXp == 0 && skillLvl != 1)
			currentXp = (int)(17.99*(skillLvl-1)*(skillLvl-1)+341.1*(skillLvl-1)-235.8);
		else
			currentXp += xp;
		int nextLevelXp = (int)(17.99*skillLvl*skillLvl+341.1*skillLvl-235.8);
		if(currentXp >= nextLevelXp)
		{
			setSkillLvl(skillLvl + 1);
			return true;
		}
		return false;
	}

	/**
	 * @return the pState
	 */
	public PersistentState getPersistentState()
	{
		return persistentState;
	}

	/**
	 * @param persistentState the pState to set
	 */
	public void setPersistentState(PersistentState persistentState)
	{
		switch(persistentState)
		{
			case DELETED:
				if(this.persistentState == PersistentState.NEW)
					this.persistentState = PersistentState.NOACTION;
				else
					this.persistentState = PersistentState.DELETED;
				break;
			case UPDATE_REQUIRED:
				if(this.persistentState != PersistentState.NEW)
					this.persistentState = PersistentState.UPDATE_REQUIRED;
				break;
			default:
				this.persistentState = persistentState;
		}
	}	
}
