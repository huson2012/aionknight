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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;

public class CM_QUEST_SHARE extends AionClientPacket
{
	static QuestsData		questsData = DataManager.QUEST_DATA;
	public int questId;

	public CM_QUEST_SHARE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		questId = readD();
	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		//NPE Check - Exploit Check
		if (player == null || questsData.getQuestById(questId).isCannotShare())
			return;

		//Player can only share quests within a group
		if (!player.isInGroup())
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100000));//[there are no group members to share that quest with]
			return;
		}

		//Player cannot share quests he dont have or its already completed
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.COMPLETE)
			return;

		//Player must try to share quests with all his group members
		PlayerGroup playerGroup = player.getPlayerGroup();

		for(Player target : playerGroup.getMembers())
		{
			if( target == player || !MathUtil.isIn3dRange(target, player, 95))
				continue;

			//Cannot share quests if the target player dont meet the level requirements
			if (!QuestService.checkLevelRequirement(questId, target.getCommonData().getLevel()))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, (target.getName())));//[you failed to share the quest with %playername]
				PacketSendUtility.sendPacket(target, new SM_SYSTEM_MESSAGE(1100003, (player.getName())));//[you failed to share the quest with %playername]
				return;
			}

			//Send share quest dialog question to target players and wait for the answer
			PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(questId, ((VisibleObject) player).getObjectId(), true));
		}
	}
}
