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

package gameserver.network.aion.serverpackets;

import gameserver.configs.main.CustomConfig;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class SM_QUEST_LIST extends AionServerPacket
{
	private SortedMap<Integer, QuestState>	completeQuestList = new TreeMap<Integer, QuestState>();
	private List<QuestState> startedQuestList = new ArrayList<QuestState>();

	public SM_QUEST_LIST(Player player)
	{
		for(QuestState qs : player.getQuestStateList().getAllQuestState())
		{
			if(qs.getStatus() == QuestStatus.COMPLETE)
				completeQuestList.put(qs.getQuestId(), qs);
			else if(qs.getStatus() != QuestStatus.NONE)
				startedQuestList.add(qs);
		}
		
		if(CustomConfig.ENABLE_SIMPLE_2NDCLASS)
		{
		    if(player.getCommonData().getRace() == Race.ELYOS)
			{
			    completeQuestList.put(1130, new QuestState(1130, QuestStatus.COMPLETE, 0, 1));
				completeQuestList.put(1300, new QuestState(1300, QuestStatus.COMPLETE, 0, 1));
				completeQuestList.put(1006, new QuestState(1006, QuestStatus.COMPLETE, 0, 1));
				completeQuestList.put(1007, new QuestState(1007, QuestStatus.COMPLETE, 0, 1));
			}
			else
			{
			    completeQuestList.put(2200, new QuestState(2200, QuestStatus.COMPLETE, 0, 1));
				completeQuestList.put(2300, new QuestState(2300, QuestStatus.COMPLETE, 0, 1));
				completeQuestList.put(2008, new QuestState(2008, QuestStatus.COMPLETE, 0, 1));
				completeQuestList.put(2009, new QuestState(2009, QuestStatus.COMPLETE, 0, 1));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, 0x01);
		writeH(buf, (-1*completeQuestList.size()) & 0xFFFF);

		for(QuestState qs : completeQuestList.values())
		{
			writeH(buf, qs.getQuestId());
			writeH(buf, 0x00);
			writeC(buf, qs.getCompleteCount());
		}
	}
}