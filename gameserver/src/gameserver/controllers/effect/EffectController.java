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

package gameserver.controllers.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ABNORMAL_EFFECT;
import gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.DispelCategoryType;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTargetSlot;
import gameserver.skill.model.SkillType;
import gameserver.task.impl.PacketBroadcaster.BroadcastMode;
import gameserver.utils.PacketSendUtility;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import java.util.*;

public class EffectController
{
	private Creature owner;

	@SuppressWarnings("unused")
	private static final Logger	log			= Logger.getLogger(EffectController.class);
	
	protected Map<String, Effect> passiveEffectMap = new FastMap<String, Effect>().shared();
	protected Map<String, Effect> noshowEffects = new FastMap<String, Effect>().shared();
	protected Map<String, Effect> abnormalEffectMap = new FastMap<String, Effect>().shared();

	protected int abnormals;

	public EffectController(Creature owner)
	{
		this.owner = owner;
	}

	/**
	 * @return the owner
	 */
	public Creature getOwner()
	{
		return owner;
	}
	
	public boolean hasAbnormalEffect(int skillId)
	{
		for(Effect e : abnormalEffectMap.values())
		{
			if(e.getSkillId() == skillId)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param effect
	 */
	public void addEffect(Effect effect)
	{
		Map<String, Effect> mapToUpdate = getMapForEffect(effect);

		Effect existingEffect = mapToUpdate.get(effect.getStack());
		if(existingEffect != null)
		{
			// check stack level
			if(existingEffect.getSkillStackLvl() > effect.getSkillStackLvl())
				return;
			// check skill level (when stack level same)
			if(existingEffect.getSkillStackLvl() == effect.getSkillStackLvl()
				&& existingEffect.getSkillLevel() > effect.getSkillLevel())
				return;

			existingEffect.endEffect();
		}

		if(effect.isToggle() && mapToUpdate.size() >= 3)
		{
			Iterator<Effect> iter = mapToUpdate.values().iterator();
			Effect nextEffect = iter.next();
			nextEffect.endEffect();
			iter.remove();
		}

		mapToUpdate.put(effect.getStack(), effect);
		effect.startEffect(false);

		if(!effect.isPassive())
		{
			broadCastEffects();
		}
	}

	/**
	 * 
	 * @param effect
	 * @return
	 */
	private Map<String, Effect> getMapForEffect(Effect effect)
	{
		if(effect.isPassive())
			return passiveEffectMap;

		if(effect.isToggle())
			return noshowEffects;

		return abnormalEffectMap;
	}

	/**
	 * 
	 * @param stack
	 * @return abnormalEffectMap
	 */
	public Effect getAbnormalEffect(String stack)
	{
		return abnormalEffectMap.get(stack);
	}

	public void broadCastEffects()
	{
		owner.addPacketBroadcastMask(BroadcastMode.BROAD_CAST_EFFECTS);
	}

	/**
	 * Broadcasts current effects to all visible objects
	 */
	public void broadCastEffectsImp()
	{
		List<Effect> effects = getAbnormalEffects();
		PacketSendUtility.broadcastPacket(getOwner(),
			new SM_ABNORMAL_EFFECT(getOwner().getObjectId(), abnormals, effects));
	}

	/**
	 * Used when player see new player
	 * 
	 * @param player
	 */
	public void sendEffectIconsTo(Player player)
	{
		List<Effect> effects = getAbnormalEffects();
		PacketSendUtility.sendPacket(player, new SM_ABNORMAL_EFFECT(getOwner().getObjectId(),
			abnormals, effects));
	}

	/**
	 * 
	 * @param effect
	 */
	public void clearEffect(Effect effect)
	{
		Map<String, Effect> mapToUpdate = getMapForEffect(effect);
		
		mapToUpdate.remove(effect.getStack());
		
		broadCastEffects();
	}

	/**
	 * Removes the effect by skillid.
	 * 
	 * @param skillid
	 */
	public void removeEffect(int skillid)
	{
		for(Effect effect : abnormalEffectMap.values()){
			if(effect.getSkillId() == skillid){
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
			}
		}
	}

	/**
	 * Removes the effect by SkillSetException Number.
	 *
	 * @param SkillSetException Number
	 */
	public void removeEffectBySetNumber(final int setNumber)
	{
		for(Effect effect : abnormalEffectMap.values())
		{
			if(effect.getSkillSetException() == setNumber)
			{
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
			}
		}
		for(Effect effect : passiveEffectMap.values())
		{
			if(effect.getSkillSetException() == setNumber)
			{
				effect.endEffect();
				passiveEffectMap.remove(effect.getStack());
			}
		}
		for(Effect effect : noshowEffects.values())
		{
			if(effect.getSkillSetException() == setNumber)
			{
				effect.endEffect();
				noshowEffects.remove(effect.getStack());
			}
		}
	}

	/**
	 * 
	 * @param effectId
	 */
	public void removeEffectByEffectId(int effectId)
	{
		for(Effect effect : abnormalEffectMap.values()){
			if(effect.containsEffectId(effectId)){
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
			}
		}
	}
	
	/**
	 * 
	 * @param targetSlot
	 * @param count
	 */
	public void removeEffectByTargetSlot(SkillTargetSlot targetSlot, int count)
	{
		for(Effect effect : abnormalEffectMap.values())
		{
			if(count == 0)
				break;
			
			if(effect.getTargetSlot() == targetSlot.ordinal())
			{
				if (targetSlot == SkillTargetSlot.BUFF && effect.getTargetSlotLevel() > 1)
					continue;
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
				count--;
			}
		}
	}
	
	/**
	 * 
	 * @param dispelCat
	 * @param targetSlot
	 * @param count
	 */
	public void removeEffectByDispelCat(DispelCategoryType dispelCat, SkillTargetSlot targetSlot, int count)
	{
		for(Effect effect : abnormalEffectMap.values())
		{
			if(count == 0)
				break;
			if(effect.getTargetSlot() == targetSlot.ordinal())
			{
				boolean remove = false;
				switch (dispelCat)
				{
					case ALL:
						if (effect.getDispelCat() == DispelCategoryType.ALL ||
							effect.getDispelCat() == DispelCategoryType.DEBUFF_MENTAL ||
							effect.getDispelCat() == DispelCategoryType.DEBUFF_PHYSICAL)
							remove = true;
						break;
					case DEBUFF_MENTAL:
						if (effect.getDispelCat() == DispelCategoryType.ALL ||
							effect.getDispelCat() == DispelCategoryType.DEBUFF_MENTAL)
							remove = true;
						break;
					case DEBUFF_PHYSICAL:
						if (effect.getDispelCat() == DispelCategoryType.ALL ||
							effect.getDispelCat() == DispelCategoryType.DEBUFF_PHYSICAL)
							remove = true;
						break;
					case BUFF:
						if (effect.getDispelCat() == DispelCategoryType.BUFF && effect.getTargetSlotLevel() < 1)
							remove = true;
						break;
					case STUN:
						if (effect.getDispelCat() == DispelCategoryType.STUN)
							remove = true;
						break; 
				}
				
				if (remove)
				{
					effect.endEffect();
					abnormalEffectMap.remove(effect.getStack());
					count--;
				}
			}
		}
	}
	
	/**
	 * @param skillType
	 * @param value
	 */
	public void removeEffectBySkillType(SkillType skillType, int value)
	{
		for(Effect effect : abnormalEffectMap.values())
		{
			if(value == 0)
				break;

			if(effect.getSkillType() == skillType)
			{
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
				value--;
			}
		}
	}
	
	
	/**
	 * Removes the effect by skillid.
	 * 
	 * @param skillid
	 */
	public void removePassiveEffect(int skillid)
	{
		for(Effect effect : passiveEffectMap.values()){
			if(effect.getSkillId()==skillid){
				effect.endEffect();
				passiveEffectMap.remove(effect.getStack());
			}
		}
	}

	/**
	 * 
	 * @param skillid
	 */
	public void removeNoshowEffect(int skillid)
	{
		for(Effect effect : noshowEffects.values()){
			if(effect.getSkillId()==skillid){
				effect.endEffect();
				noshowEffects.remove(effect.getStack());
			}
		}
	}

	/**
	 * @see TargetSlot
	 * @param targetSlot
	 */
	public void removeAbnormalEffectsByTargetSlot(SkillTargetSlot targetSlot)
	{
		for(Effect effect : abnormalEffectMap.values()){
			if(effect.getTargetSlot() == targetSlot.ordinal()){
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
			}
		}
	}
	/**
	 * Removes all effects from controllers and ends them appropriately
	 * Passive effect will not be removed
	 */
	public void removeAllEffects()
	{
		for(Effect effect : abnormalEffectMap.values())
		{
			effect.endEffect();
		}
		abnormalEffectMap.clear();
		for(Effect effect : noshowEffects.values())
		{
			effect.endEffect();
		}
		noshowEffects.clear();
	}

	public void updatePlayerEffectIcons()
	{
		getOwner().addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_EFFECT_ICONS);
	}

	public void updatePlayerEffectIconsImpl()
	{
		List<Effect> effects = getAbnormalEffects();

		PacketSendUtility.sendPacket((Player) owner,
			new SM_ABNORMAL_STATE(effects, abnormals));
	}

	/**
	 * @return copy of anbornals list
	 */
	public List<Effect> getAbnormalEffects()
	{
		List<Effect> effects = new ArrayList<Effect>();
		Iterator<Effect> iterator = iterator();
		while(iterator.hasNext())
		{
			Effect effect = iterator.next();
			if(effect != null)
				effects.add(effect);
		}
		return effects;
	}

	/**
	 * ABNORMAL EFFECTS
	 */

	public void setAbnormal(int mask)
	{
		abnormals |= mask;
	}

	public void unsetAbnormal(int mask)
	{
		int count = 0;
		for(Effect effect : abnormalEffectMap.values())
		{
			if ((effect.getAbnormals() & mask) == mask)
				count++;
		}
		if (count<=1)
			abnormals &= ~mask;

        if (owner instanceof Player)
        {
            PacketSendUtility.sendPacket((Player) owner, new SM_SKILL_LIST((Player) owner));
        }
	}

	/**
	 * Used for checking unique abnormal states
	 * 
	 * @param effectId
	 * @return
	 */
	public boolean isAbnormalSet(EffectId effectId)
	{
		return (abnormals & effectId.getEffectId()) == effectId.getEffectId();
	}

	/**
	 * Used for compound abnormal state checks
	 * 
	 * @param effectId
	 * @return
	 */
	public boolean isAbnormalState(EffectId effectId)
	{
		int state = abnormals & effectId.getEffectId();
		return state > 0 && state <= effectId.getEffectId();
	}

	public int getAbnormals()
	{
		return abnormals;
	}

	/**
	 * 
	 * @return
	 */
	public Iterator<Effect> iterator()
	{
		return abnormalEffectMap.values().iterator();
	}
	
	public Collection<Effect> getNoShowEffects()
	{
		return noshowEffects.values();
	}
	public Collection<Effect> getPassiveEffects()
	{
		return passiveEffectMap.values();
	}
}
