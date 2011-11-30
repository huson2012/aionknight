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

/**
 * @author AE
 */
public class SM_STARTED_QUEST_LIST extends AionServerPacket
{
    private SortedMap<Integer, QuestState>	completeQuestList	= new TreeMap<Integer, QuestState>();
    private List<QuestState>				startedQuestList	= new ArrayList<QuestState>();

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