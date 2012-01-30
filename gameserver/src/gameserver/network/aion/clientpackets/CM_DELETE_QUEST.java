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

package gameserver.network.aion.clientpackets;

import gameserver.dataholders.DataManager;
import gameserver.dataholders.QuestsData;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.QuestEngine;
import gameserver.services.GuildService;

public class CM_DELETE_QUEST extends AionClientPacket 
{

    static QuestsData questsData = DataManager.QUEST_DATA;
    public int questId;

    public CM_DELETE_QUEST(int opcode) 
	{
        super(opcode);
    }

    @Override
    protected void readImpl() 
	{
        questId = readH();
    }

    @Override
    protected void runImpl() 
	{
        Player player = getConnection().getActivePlayer();
        if (questsData.getQuestById(questId).isTimer()) {
            player.getController().cancelTask(TaskId.QUEST_TIMER);
            sendPacket(new SM_QUEST_ACCEPTED(questId, 0));
        }
        if (!QuestEngine.getInstance().deleteQuest(player, questId))
            return;
        sendPacket(new SM_QUEST_ACCEPTED(questId));
		GuildService.getInstance().deleteDaily(player, questId);
        player.getController().updateNearbyQuests();
    }
}
