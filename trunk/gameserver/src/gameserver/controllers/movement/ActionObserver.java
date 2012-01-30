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

package gameserver.controllers.movement;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Skill;

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
	
	private final ObserverType observerType;
	
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

	public void moved(){}
    public void attacked(Creature creature){}
    public void attack(Creature creature){}
    public void equip(Item item, Player owner){}
    public void unequip(Item item, Player owner){}
    public void skilluse(Skill skill){}
    public void stateChanged(CreatureState state, boolean isSet) {}
    public void died(Creature creature) {}
    public void jump(){}
    public void onDot(Creature creature) {}
    public void hitted(Creature creature, DamageType type) {}
}