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

package gameserver.ai.events;

import gameserver.ai.events.handler.*;

public enum EventHandlers
{
	ATTACKED_EH(new AttackedEventHandler()),
	TIREDATTACKING_EH(new TiredAttackingEventHandler()),
	MOST_HATED_CHANGED_EH(new TiredAttackingEventHandler()),
	SEEPLAYER_EH(new SeePlayerEventHandler()),
	NOTSEEPLAYER_EH(new NotSeePlayerEventHandler()),
	RESPAWNED_EH(new RespawnedEventHandler()),
	BACKHOME_EH(new BackHomeEventHandler()),
	TALK_EH(new TalkEventHandler()),
	RESTOREDHEALTH_EH(new RestoredHealthEventHandler()),
	NOTHINGTODO_EH(new NothingTodoEventHandler()),
	DESPAWN_EH(new DespawnEventHandler()),
	DIED_EH(new DiedEventHandler());
	
	private EventHandler eventHandler;
	
	private EventHandlers(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}
	
	public EventHandler getHandler()
	{
		return eventHandler;
	}
}