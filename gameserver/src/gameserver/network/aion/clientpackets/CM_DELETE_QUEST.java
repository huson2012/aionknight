/**
 * This file is part of Aion-Knight <Aion-Knight.smfnew.com>.
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
