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

package gameserver.model.templates.quest;

import java.util.ArrayList;
import java.util.List;

public class NpcQuestData
{
	private final List<Integer> onQuestStart;
	private final List<Integer> onKillEvent;
	private final List<Integer> onTalkEvent;
	private final List<Integer> onAttackEvent;
	private final List<Integer> onActionItemEvent;
	
	public NpcQuestData()
	{
		onQuestStart = new ArrayList<Integer>();
		onKillEvent = new ArrayList<Integer>();
		onTalkEvent = new ArrayList<Integer>();
		onAttackEvent = new ArrayList<Integer>();
		onActionItemEvent = new ArrayList<Integer>();
	}

	public void addOnQuestStart(int questId)
	{
		if (!onQuestStart.contains(questId))
		{
			onQuestStart.add(questId);
		}
	}
	public List<Integer> getOnQuestStart()
	{
		return onQuestStart;
	}

	public void addOnAttackEvent(int questId)
	{
		if (!onAttackEvent.contains(questId))
		{
			onAttackEvent.add(questId);
		}
	}
	public List<Integer> getOnAttackEvent()
	{
		return onAttackEvent;
	}

	public void addOnKillEvent(int questId)
	{
		if (!onKillEvent.contains(questId))
		{
			onKillEvent.add(questId);
		}
	}
	public List<Integer> getOnKillEvent()
	{
		return onKillEvent;
	}

	public void addOnTalkEvent(int questId)
	{
		if (!onTalkEvent.contains(questId))
		{
			onTalkEvent.add(questId);
		}
	}
	public List<Integer> getOnTalkEvent()
	{
		return onTalkEvent;
	}
	
	public void addOnActionItemEvent(int questId)
	{
		if (!onActionItemEvent.contains(questId))
		{
			onActionItemEvent.add(questId);
		}
	}
	public List<Integer> getOnActionItemEvent()
	{
		return onActionItemEvent;
	}
}
