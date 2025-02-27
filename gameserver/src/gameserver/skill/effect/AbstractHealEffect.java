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

package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skill.model.Effect;
import gameserver.skill.model.HealType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractHealEffect")
public abstract class AbstractHealEffect extends EffectTemplate
{
	@XmlAttribute(required = true)
	protected int value;

	@XmlAttribute
	protected int delta;

	@XmlAttribute
	protected boolean percent;
	
	public void calculate(Effect effect, HealType healType, boolean applyBoost)
	{
		int valueWithDelta = value + delta * effect.getSkillLevel();

		int healValue = 0;
		
		int currentValue = getCurrentStatValue(effect);
		int maxCurValue = getMaxCurStatValue(effect);
		int possibleHealValue = 0;
		if(percent)
		{
			possibleHealValue = maxCurValue * valueWithDelta / 100;
		}
		else // non percent heal
		{
			// +100 = 100% heal min value for all class.
			// Boost heal formula = boost heal value / 10 = additional % heal value.
			//each player start with boost_heal 100, therefore -100
			float boostHeal = 0;
			float healRate = 1.0f;
			if (applyBoost && effect.getEffector() instanceof Player)
			{
				boostHeal = ((float)(effect.getEffector().getGameStats().getCurrentStat(StatEnum.BOOST_HEAL)-100) / 1000f);
				healRate = effect.getEffector().getController().getHealRate();
			}
			
			possibleHealValue = (int)((float)valueWithDelta * (healRate + boostHeal));
		}
		
		healValue = maxCurValue - currentValue < possibleHealValue ? (maxCurValue - currentValue) : possibleHealValue;

		if (healType == HealType.HP && effect.getEffected().getEffectController().isAbnormalSet(EffectId.DISEASE))
			healValue = 0;
		
		effect.setReserved1(-healValue);
		
		switch (healType)
		{
			case MP:
				effect.setMpValue(healValue);
				break;
			default:
				effect.setHealValue(healValue);
				break;
		}
		
		super.calculate(effect);
	}
	
	public void applyEffect(Effect effect, HealType healtype)
	{
		Creature effected = effect.getEffected();
		int healValue = 0;
		switch (healtype)
		{
			case MP:
				healValue = effect.getMpValue();
				break;
			default:
				healValue = effect.getHealValue();
				break;
		}
		
		if (healValue == 0)
			return;

		switch (healtype)
		{
			case FP:
				if(effected instanceof Player)
					((Player)effected).getLifeStats().increaseFp(TYPE.FP, healValue, effect.getSkillId(), 170);
				break;
			case HP:
				if (this.getClass().toString().contains("Item"))
					effected.getLifeStats().increaseHp(TYPE.HP, healValue, effect.getSkillId(), 170);
				else//TODO find proper type for healed hp
					effected.getLifeStats().increaseHp(TYPE.HP, healValue, effect.getSkillId(), 170);
				break;
			case MP:
				if (this.getClass().toString().contains("Item"))
					effected.getLifeStats().increaseMp(TYPE.MP, healValue, effect.getSkillId(), 170);
				else
					effected.getLifeStats().increaseMp(TYPE.HEALED_MP, healValue, effect.getSkillId(), 170);
				break;
			case DP:
				((Player) effect.getEffected()).getCommonData().addDp(healValue);
				break;
		}
	}
	
	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected int getCurrentStatValue(Effect effect)
	{
		return effect.getEffected().getLifeStats().getCurrentHp();
	}
	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected int getMaxCurStatValue(Effect effect)
	{
		return effect.getEffected().getLifeStats().getMaxHp();
	}
}
