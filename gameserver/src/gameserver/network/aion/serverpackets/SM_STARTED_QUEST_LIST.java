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

public class SM_STARTED_QUEST_LIST extends AionServerPacket
{
    private SortedMap<Integer, QuestState>	completeQuestList = new TreeMap<Integer, QuestState>();
    private List<QuestState> startedQuestList = new ArrayList<QuestState>();

    public SM_STARTED_QUEST_LIST(Player player)
    {
        for (QuestState qs : player.getQuestStateList().getAllQuestState())
        {
            if (qs.getStatus() == QuestStatus.COMPLETE)
                completeQuestList.put(qs.getQuestId(), qs);
            else if (qs.getStatus() != QuestStatus.NONE)
                startedQuestList.add(qs);
        }
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeH(buf, 0x01);
        writeH(buf, (-1*startedQuestList.size()) & 0xFFFF);
        
		for (QuestState qs : startedQuestList) // quest list size ( retail max is 30 )
        {
            writeH(buf, qs.getQuestId());
            writeH(buf, 0);
            writeC(buf, qs.getStatus().value());
            writeD(buf, qs.getQuestVars().getQuestVars());
            writeC(buf, 0);
        }
    }
}