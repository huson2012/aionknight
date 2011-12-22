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

package gameserver.ai.events.handler;

import gameserver.ai.AI;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.Npc;
import gameserver.utils.ThreadPoolManager;

public class TalkEventHandler implements EventHandler
{
	@Override
	public Event getEvent()
	{
		return Event.TALK;
	}

	@Override
	public void handleEvent(Event event, AI<?> ai)
	{
		final Npc owner = (Npc) ai.getOwner();

		if(owner.hasWalkRoutes())
		{
			owner.getMoveController().setCanWalk(false);
			owner.getController().stopMoving();

			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					owner.getMoveController().setCanWalk(true);
				}
			}, 60000);
		}
		ai.setAiState(AIState.TALKING);
	}
}
