/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package quest.talocs_hollow;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _11466AHardSeedToCrack extends QuestHandler
{
	private final static int	questId	= 11466;

	public _11466AHardSeedToCrack()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(env.getTargetId() == 0 && env.getQuestId() == questId)
			return defaultQuestStartItem(env);
		
		if(qs == null)
			return false;
		
		if (env.getTargetId() == 279000)
		{
			if (qs.getStatus() == QuestStatus.START)
			{
				if (env.getDialogId() == -1)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}
			else if (qs.getStatus() == QuestStatus.REWARD && env.getDialogId() == 1009)
			{
				// FIXME: don't broadcast to everyone; now thanks to player and everyone
				PacketSendUtility.sendPacket(player, new SM_EMOTION((Creature)env.getVisibleObject(), 
					EmotionType.EMOTE, 15, player.getObjectId()));
				return sendQuestDialog(env, 5);
			}
		}
		return defaultQuestRewardDialog(env, 279000, 2375);
		
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int id = item.getItemTemplate().getTemplateId();

		if (id != 182209524)
			return HandlerResult.UNKNOWN;
		
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		
		if (qs != null || !lvlCheck)
			return HandlerResult.FAILED;
		
		return HandlerResult.SUCCESS; 
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(279000).addOnTalkEvent(questId);
		qe.setQuestItemIds(182209524).add(questId);
	}
}