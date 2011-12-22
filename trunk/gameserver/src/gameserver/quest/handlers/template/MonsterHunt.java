/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
