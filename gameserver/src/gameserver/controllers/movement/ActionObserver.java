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