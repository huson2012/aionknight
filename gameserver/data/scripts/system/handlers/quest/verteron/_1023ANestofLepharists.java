/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.verteron;


import ru.aionknight.gameserver.configs.main.CustomConfig;
import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.EmotionType;
import ru.aionknight.gameserver.model.PlayerClass;
import ru.aionknight.gameserver.model.gameobjects.Item;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.gameobjects.stats.StatEnum;
import ru.aionknight.gameserver.model.templates.quest.QuestItems;
import ru.aionknight.gameserver.network.aion.SystemMessageId;
import ru.aionknight.gameserver.network.aion.serverpackets.*;
import ru.aionknight.gameserver.quest.HandlerResult;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.services.*;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.ThreadPoolManager;
import ru.aionknight.gameserver.world.WorldMapInstance;
import ru.aionknight.gameserver.world.zone.ZoneName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Mr. Poke
 * re-mode Dune11 www.aiongate.cz
 * re-done kecimis
 * re-done retail by Orpheo
 *
 */
public class _1023ANestofLepharists extends QuestHandler
{
	private final static int	questId	= 1023;
	
	public _1023ANestofLepharists()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setQuestMovieEndIds(23).add(questId);
		qe.setNpcQuestData(203098).addOnTalkEvent(questId);//Spatalos
		qe.setNpcQuestData(203183).addOnTalkEvent(questId);//Khidia
		qe.setQuestEnterZone(ZoneName.MYSTERIOUS_SHIPWRECK).add(questId);
		qe.addQuestLvlUp(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.MYSTERIOUS_SHIPWRECK)
			return false;
		if(qs == null || qs.getQuestVarById(0) != 2)
			return false;
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 23));
		qs.setQuestVarById(0, 3);
		updateQuestStatus(env);
		return true;
	}

    @Override
    public boolean onMovieEndEvent(final QuestCookie env, int movieId) {
        if (movieId != 23)
            return false;
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.START || qs.getQuestVars().getQuestVars() != 3)
		{
			TeleportService.teleportTo(player, 210030000, 1, 2376, 833, 107, (byte) 20, 0);
            return true;
		}
        return false;
    }

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203098)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
				}
			}
			else if(env.getTargetId() == 203183)//Khidia
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1352);
						else if(var == 3) 
							return sendQuestDialog(env, 1693);
						else if(var == 4)
							return sendQuestDialog(env, 2034);
					case 34:
						return defaultQuestItemCheck(env, 4, 0, false, 2120, 2035);
					case 10001:
						PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE, 25, 1));
						PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 39, 1));
						Skill skill = SkillEngine.getInstance().getSkill(player,8197,1,player);
						skill.useSkill();
						return defaultCloseDialog(env, 1, 2);
					case 10002:
						return defaultCloseDialog(env, 3, 4);
					case 10003:
						return defaultCloseDialog(env, 4, 5, true, false);
				}
			}
		}
		return defaultQuestRewardDialog(env, 203098, 2375);
	}	
}
