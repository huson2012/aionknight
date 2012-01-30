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

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.id.SkillEffectId;
import gameserver.model.gameobjects.stats.modifiers.AddModifier;
import gameserver.model.gameobjects.stats.modifiers.RateModifier;
import gameserver.model.gameobjects.stats.modifiers.SetModifier;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import gameserver.skill.change.Change;
import gameserver.skill.model.Effect;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.TreeSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BufEffect")
public abstract class BufEffect extends EffectTemplate
{
	private static final Logger log = Logger.getLogger(BufEffect.class);
	
	@Override
	public void applyEffect(final Effect effect)
	{
		if (isOnFly())
		{
			ActionObserver observer = new ActionObserver(ObserverType.STATECHANGE) {
				@Override
				public void stateChanged (CreatureState state, boolean isSet)
				{
					if (state == CreatureState.FLYING)
					{
						if (isSet)
						{
							if (!effect.getEffected().getEffectController().hasAbnormalEffect(effect.getSkillId()))
								effect.addToEffectedController();
						}
						else if (!effect.getEffected().isInState(CreatureState.FLYING))
						{
							effect.endEffect();
						}
					}
				}
			};
			
			effect.getEffected().getObserveController().addObserver(observer);
			
			//add observer only for non-passives
			if (!effect.getSkillTemplate().isPassive())
				effect.setActionObserver(observer, position);
		} else {
			effect.addToEffectedController();
		}
	}

	/**
	 * Will be called from effect controller when effect ends
	 */
	@Override
	public void endEffect(Effect effect)
	{
		Creature effected = effect.getEffected();
		int skillId = effect.getSkillId();
		effected.getGameStats().endEffect(SkillEffectId.getInstance(skillId, effectid, position));
		
		//remove observer for non passive skill
		if (!effect.getSkillTemplate().isPassive())
		{
			ActionObserver observer = effect.getActionObserver(position);
			if (observer != null)
				effect.getEffected().getObserveController().removeObserver(observer);
		}
	}
	/**
	 * Will be called from effect controller when effect starts
	 */
	@Override
	public void startEffect(Effect effect)
	{
		if(change == null)
			return;
	
		Creature effected = effect.getEffected();		
		CreatureGameStats<? extends Creature> cgs = effected.getGameStats();

		TreeSet<StatModifier> modifiers = getModifiers(effect);
		SkillEffectId skillEffectId = getSkillEffectId(effect);
		
		if (!modifiers.isEmpty())
		{
			cgs.addModifiers(skillEffectId, modifiers);
			
			if(effect.getEffected() instanceof Player)
			{
				Player player = (Player)effect.getEffected();
				PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			}
			else if(effect.getEffected() instanceof Summon)
			{
				Summon s = (Summon)effect.getEffected();
				PacketSendUtility.sendPacket(s.getMaster(), new SM_SUMMON_UPDATE(s));
			}
		}
	}

	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected SkillEffectId getSkillEffectId(Effect effect)
	{
		int skillId = effect.getSkillId();
		return SkillEffectId.getInstance(skillId, effectid, position);
	}
	
	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected TreeSet<StatModifier> getModifiers(Effect effect)
	{
		int skillId = effect.getSkillId();
		int skillLvl = effect.getSkillLevel();
		
		TreeSet<StatModifier> modifiers = new TreeSet<StatModifier> ();
		
		for(Change changeItem : change)
		{
			if(changeItem.getStat() == null)
			{
				log.warn("Skill stat has wrong name for skillid: " + skillId);
				continue;
			}

			int valueWithDelta = changeItem.getValue() + changeItem.getDelta() * skillLvl;

			switch(changeItem.getFunc())
			{
				case ADD:
					modifiers.add(AddModifier.newInstance(changeItem.getStat(),valueWithDelta,true));
					break;
				case PERCENT:
					modifiers.add(RateModifier.newInstance(changeItem.getStat(),valueWithDelta,true));
					break;
				case REPLACE:
					modifiers.add(SetModifier.newInstance(changeItem.getStat(),valueWithDelta, true));
					break;
			}
		}
		return modifiers;
	}

	@Override
	public void onPeriodicAction(Effect effect)
	{
		// TODO Auto-generated method stub
		
	}
}