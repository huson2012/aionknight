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
package gameserver.ai.events;

import gameserver.ai.events.handler.AttackedEventHandler;
import gameserver.ai.events.handler.BackHomeEventHandler;
import gameserver.ai.events.handler.DespawnEventHandler;
import gameserver.ai.events.handler.DiedEventHandler;
import gameserver.ai.events.handler.EventHandler;
import gameserver.ai.events.handler.NotSeePlayerEventHandler;
import gameserver.ai.events.handler.NothingTodoEventHandler;
import gameserver.ai.events.handler.RespawnedEventHandler;
import gameserver.ai.events.handler.RestoredHealthEventHandler;
import gameserver.ai.events.handler.SeePlayerEventHandler;
import gameserver.ai.events.handler.TalkEventHandler;
import gameserver.ai.events.handler.TiredAttackingEventHandler;

/**

 *
 */
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
