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
 
package gameserver.model.gameobjects.stats;

import gameserver.model.gameobjects.Summon;
import gameserver.model.templates.stats.SummonStatsTemplate;

public class SummonGameStats extends CreatureGameStats<Summon>
{

	/**
	 * 
	 * @param owner
	 * @param statsTemplate
	 */
	public SummonGameStats(Summon owner, SummonStatsTemplate statsTemplate)
	{
		super(owner);
		initStat(StatEnum.MAXHP, statsTemplate.getMaxHp());
		initStat(StatEnum.MAXMP, statsTemplate.getMaxMp());
		initStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, statsTemplate.getMainHandAttack());
		initStat(StatEnum.PHYSICAL_DEFENSE, statsTemplate.getPdefense());
		initStat(StatEnum.MAGICAL_RESIST, statsTemplate.getMresist());
		initStat(StatEnum.ATTACK_SPEED, 2000);
		initStat(StatEnum.ATTACK_RANGE, (owner.getObjectTemplate().getAttackRange() * 1000));
		initStat(StatEnum.SPEED, Math.round(statsTemplate.getRunSpeed() * 1000));
		initStat(StatEnum.REGEN_HP, owner.getLevel() + 3);
		initStat(StatEnum.KNOWLEDGE, 100);
		initStat(StatEnum.ACCURACY, 1000);
		initStat(StatEnum.MAGICAL_ACCURACY, 1000);
	}
}
