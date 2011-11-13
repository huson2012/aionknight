/**
 * This file is part of Aion-Knight <www.Aion-Knight.com>.
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
package gameserver.controllers.movement;


import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Skill;

/**

 *
 */
public class ActionObserver
{
	public enum ObserverType
	{
		MOVE,
		ATTACK,
		ATTACKED,
		EQUIP,
		SKILLUSE,
		STATECHANGE,
		DEATH,
		JUMP,
		DOT,
		HITTED
	}
	
	private ObserverType observerType;
	
	public ActionObserver(ObserverType observerType)
	{
		this.observerType = observerType;
	}
	
	/**
	 * @return the observerType
	 */
	public ObserverType getObserverType()
	{
		return observerType;
	}

	public void moved(){};
	public void attacked(Creature creature){};
	public void attack(Creature creature){};
	public void equip(Item item, Player owner){};
	public void unequip(Item item, Player owner){};
	public void skilluse(Skill skill){};
	public void stateChanged(CreatureState state, boolean isSet) {};
	public void died(Creature creature) {};
	public void jump(){};
	public void onDot(Creature creature) {};
	public void hitted(Creature creature, DamageType type) {};
}
