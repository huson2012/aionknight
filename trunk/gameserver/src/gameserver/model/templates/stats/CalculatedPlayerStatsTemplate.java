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

package gameserver.model.templates.stats;

import gameserver.model.PlayerClass;
import gameserver.utils.stats.ClassStats;

public class CalculatedPlayerStatsTemplate extends PlayerStatsTemplate
{
	
	private PlayerClass playerClass;
	
	public CalculatedPlayerStatsTemplate(PlayerClass playerClass)
	{
		this.playerClass = playerClass;
	}
	
	@Override
	public int getAccuracy()
	{
		return ClassStats.getAccuracyFor(playerClass);
	}

	@Override
	public int getAgility()
	{
		return ClassStats.getAgilityFor(playerClass);
	}

	@Override
	public int getHealth()
	{
		return ClassStats.getHealthFor(playerClass);
	}

	@Override
	public int getKnowledge()
	{
		return ClassStats.getKnowledgeFor(playerClass);
	}

	@Override
	public int getPower()
	{
		return ClassStats.getPowerFor(playerClass);
	}

	@Override
	public int getWill()
	{
		return ClassStats.getWillFor(playerClass);
	}

	@Override
	public float getAttackSpeed()
	{
		return ClassStats.getAttackSpeedFor(playerClass) / 1000f;
	}

	@Override
	public int getBlock()
	{
		return ClassStats.getBlockFor(playerClass);
	}

	@Override
	public int getEvasion()
	{
		return ClassStats.getEvasionFor(playerClass);
	}

	@Override
	public float getFlySpeed()
	{
		// TODO Auto-generated method stub
		return ClassStats.getFlySpeedFor(playerClass);
	}

	@Override
	public int getMagicAccuracy()
	{
		return ClassStats.getMagicAccuracyFor(playerClass);
	}

	@Override
	public int getMainHandAccuracy()
	{
		return ClassStats.getMainHandAccuracyFor(playerClass);
	}

	@Override
	public int getMainHandAttack()
	{
		return ClassStats.getMainHandAttackFor(playerClass);
	}

	@Override
	public int getMainHandCritRate()
	{
		return ClassStats.getMainHandCritRateFor(playerClass);
	}

	@Override
	public int getMaxHp()
	{
		return ClassStats.getMaxHpFor(playerClass, 10); // level is hardcoded
	}

	@Override
	public int getMaxMp()
	{
		return 1000;
	}

	@Override
	public int getParry()
	{
		return ClassStats.getParryFor(playerClass);
	}

	@Override
	public float getRunSpeed()
	{
		return ClassStats.getSpeedFor(playerClass);
	}

	@Override
	public float getWalkSpeed()
	{
		return 1.5f;
	}

}
