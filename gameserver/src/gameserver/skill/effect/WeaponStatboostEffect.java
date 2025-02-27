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
package gameserver.skill.effect;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.id.SkillEffectId;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.templates.item.WeaponType;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.TreeSet;


/**

 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeaponStatboostEffect")
public class WeaponStatboostEffect extends BufEffect
{
	@XmlAttribute(name = "weapon")
	private WeaponType weaponType;
	
	@Override
	public void startEffect(final Effect effect)
	{
		final Player effected  = (Player) effect.getEffected();
	
		final SkillEffectId skillEffectId = getSkillEffectId(effect);
		final TreeSet<StatModifier> stats = getModifiers(effect);
		
		if(effected.getEquipment().isWeaponEquipped(weaponType))
			effected.getGameStats().addModifiers(skillEffectId, stats);
		
		/**
		 * Since weapon stat boost is only for bows in templates - checking only
		 * one weapon is enough for final result.
		 */
		ActionObserver aObserver = new ActionObserver(ObserverType.EQUIP){

			@Override
			public void equip(Item item, Player owner)
			{
				if(item.getItemTemplate().getWeaponType() == weaponType)
					effected.getGameStats().addModifiers(skillEffectId, stats);
			}

			@Override
			public void unequip(Item item, Player owner)
			{
				if(item.getItemTemplate().getWeaponType() == weaponType)
					effected.getGameStats().endEffect(skillEffectId);
			}
			
		};
		
		effected.getObserveController().addEquipObserver(aObserver);
		effect.setActionObserver(aObserver, position);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		ActionObserver observer = effect.getActionObserver(position);
		if(observer != null)
			effect.getEffected().getObserveController().removeEquipObserver(observer);
		
		final SkillEffectId skillEffectId = getSkillEffectId(effect);
		
		if(effect.getEffected().getGameStats().effectAlreadyAdded(skillEffectId))
			effect.getEffected().getGameStats().endEffect(skillEffectId);
	}
}
