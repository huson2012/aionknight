/**
 * This file is part of Aion-Knight
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.fortuneers;


import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;


public class _36517KillingPride extends QuestHandler
{
	private final static int	questId	= 36517;

	public _36517KillingPride()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestStartDaily(env))
			return true;
		
		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 700764)
				return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
		}
		if(defaultQuestRewardDialog(env, 799837, 10002) || defaultQuestRewardDialog(env, 799838, 10002))
			return true;
		else
			return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, 216621, 0, true);
	}

	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		VisibleObject vO = env.getVisibleObject();
		if(vO instanceof Npc)
		{
			Npc seed = (Npc)vO;
			if(seed.getNpcId() == 700764)
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 216621, seed.getX(), seed.getY(), seed.getZ(), (byte) 0, true);
		}
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(700764).addOnTalkEvent(questId);
		qe.setNpcQuestData(799837).addOnTalkEvent(questId);
		qe.setNpcQuestData(799838).addOnTalkEvent(questId);
		qe.setNpcQuestData(216621).addOnKillEvent(questId);
	}
}
