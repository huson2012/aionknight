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

package gameserver.ai.state;

import gameserver.ai.state.handler.*;

public enum StateHandlers
{
	/**
	 * AIState.MOVINGTOHOME
	 */
	MOVINGTOHOME_SH(new MovingToHomeStateHandler()),
	/**
	 * AIState.NONE
	 */
	NONE_MONSTER_SH(new NoneNpcStateHandler()),
	/**
	 * AIState.ATTACKING
	 */
	ATTACKING_SH(new AttackingStateHandler()),
	/**
	 * AIState.THINKING
	 */
	THINKING_SH(new ThinkingStateHandler()),	
	/**
	 * AIState.ACTIVE
	 */
	ACTIVE_NPC_SH(new ActiveNpcStateHandler()),
	ACTIVE_AGGRO_SH(new ActiveAggroStateHandler()),	
	/**
	 * AIState.RESTING
	 */
	RESTING_SH(new RestingStateHandler()),
	/**
	 * AIState.TALKING
	 */
	TALKING_SH(new TalkingStateHandler());
	
	private StateHandler stateHandler;
	
	private StateHandlers(StateHandler stateHandler)
	{
		this.stateHandler = stateHandler;
	}
	
	public StateHandler getHandler()
	{
		return stateHandler;
	}
}