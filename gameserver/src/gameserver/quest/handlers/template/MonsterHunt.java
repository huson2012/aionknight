/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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
package gameserver.quest.handlers.template;

import gameserver.dataholders.DataManager;
import gameserver.model.NpcType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.handlers.models.MonsterInfo;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import javolution.util.FastMap;
import org.apache.log4j.Logger;


/**
 * @author MrPoke
 * 
 */
public class MonsterHunt extends QuestHandler
{
	private static final Logger	log		= Logger.getLogger(MonsterHunt.class);
	
	private final int				questId;
	private final int				startNpc;
	private final int				endNpc;
	private final FastMap<Integer, MonsterInfo>	monsterInfo;

	/**
	 * @param questId
	 */
	public MonsterHunt(int questId, int startNpc, int endNpc, FastMap<Integer, MonsterInfo> monsterInfo)
	{
		super(questId);
		this.questId = questId;
		this.startNpc = startNpc;
		if (endNpc != 0)
			this.endNpc = endNpc;
		else
			this.endNpc = startNpc;
		this.monsterInfo = monsterInfo;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(startNpc).addOnQuestStart(questId);
		qe.setNpcQuestData(startNpc).addOnTalkEvent(questId);
		NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(startNpc);
		if (template == null)
			log.warn("Q" + questId + " has invalid start NPC");
		else if (template.getNpcType() == NpcType.USEITEM)
			qe.setNpcQuestData(startNpc).addOnActionItemEvent(questId);
		for(int monsterId : monsterInfo.keySet())
			qe.setNpcQuestData(monsterId).addOnKillEvent(questId);
		if(endNpc != startNpc)
			qe.setNpcQuestData(endNpc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		return (startNpc == env.getTargetId());
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean daily = false;
		
		if(player.getGuild().getCurrentQuest() == env.getQuestId())
			daily = true;
		
		if(defaultQuestStartDaily(env))
			return true;
				
		if(defaultQuestNoneDialog(env, startNpc))
			return true;
		
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			for(MonsterInfo mi : monsterInfo.values())
			{
				if(mi.getMaxKill() > qs.getQuestVarById(mi.getVarId()))
					return false;
			}
			if(env.getTargetId() == endNpc || (env.getTargetId() == startNpc && daily))
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1352);
					case 1009:
						return defaultCloseDialog(env, var, var+1, true, true);
				}
			}
		}
		if(defaultQuestRewardDialog(env, endNpc, 0) || (defaultQuestRewardDialog(env, startNpc, 0) && daily))
			return true;
		else
			return false;
	}
	

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs.getStatus() != QuestStatus.START)
			return false;
		MonsterInfo mi = monsterInfo.get(env.getTargetId());
		if(mi == null)
			return false;
		return defaultQuestOnKillEvent(env, env.getTargetId(), 0, mi.getMaxKill(), mi.getVarId());
	}
}
