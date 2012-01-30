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
        return defaultQuestRewardDialog(env, endNpc, 0) || (defaultQuestRewardDialog(env, startNpc, 0) && daily);
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
