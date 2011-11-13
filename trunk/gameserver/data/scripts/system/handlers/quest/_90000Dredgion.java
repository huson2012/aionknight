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

package quest;

import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;

public class _90000Dredgion extends QuestHandler
{
	private static int questId = 90000;
	
	public _90000Dredgion()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{

	}
	
	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return false;
	}
}