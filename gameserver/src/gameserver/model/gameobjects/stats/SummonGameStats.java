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
package gameserver.model.gameobjects.stats;


import gameserver.model.gameobjects.Summon;
import gameserver.model.templates.stats.SummonStatsTemplate;

/**

 *
 */
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
