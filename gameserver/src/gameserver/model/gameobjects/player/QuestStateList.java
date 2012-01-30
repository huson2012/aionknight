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

package gameserver.model.gameobjects.player;

import gameserver.quest.model.QuestState;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class QuestStateList
{
	private static final Logger log = Logger.getLogger(QuestStateList.class);
	
	private final SortedMap<Integer, QuestState> _quests;
	
	/**
	 * Creates an empty quests list
	 */
	public QuestStateList()
	{
		_quests = Collections.synchronizedSortedMap(new TreeMap<Integer, QuestState>());
	}

	public boolean addQuest(int questId,  QuestState questState)
	{
		if (_quests.containsKey(questId))
		{
			log.warn("Duplicate quest. ");
			return false;
		}
		_quests.put(questId, questState);
		return true;
	}

	public boolean removeQuest(int questId)
	{
		if (_quests.containsKey(questId))
		{
			_quests.remove(questId);
			return true;
		}
		return false;
	}
	
	public QuestState getQuestState(int questId)
	{
		return _quests.get(questId);
	}

	public Collection <QuestState> getAllQuestState()
	{
		return _quests.values();
	}
}
