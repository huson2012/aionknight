/**
 * This file is part of aion-unique <aion-unique.smfnew.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.network.aion.clientpackets;


import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.dataholders.QuestsData;
import ru.aionknight.gameserver.model.TaskId;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import ru.aionknight.gameserver.quest.QuestEngine;
import ru.aionknight.gameserver.services.GuildService;

public class CM_DELETE_QUEST extends AionClientPacket
{

	static QuestsData		questsData = DataManager.QUEST_DATA;
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
		if(questsData.getQuestById(questId).isTimer())
		{
			player.getController().cancelTask(TaskId.QUEST_TIMER);
			sendPacket(new SM_QUEST_ACCEPTED(4, questId, 0));
		}		
		if (!QuestEngine.getInstance().deleteQuest(player, questId))
			return;
		sendPacket(new SM_QUEST_ACCEPTED(questId));
		GuildService.getInstance().deleteDaily(player, questId);
		player.getController().updateNearbyQuests();
	}
}
