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

package quest.daevanion_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _1990ASagesGift extends QuestHandler
{
	private final static int	questId	= 1990;
	private final static int[]	mobs	= {256617, 253720, 254513};
	
	public _1990ASagesGift()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203771).addOnQuestStart(questId);
		qe.setNpcQuestData(203771).addOnTalkEvent(questId);
		for(int mob: mobs)
			qe.setNpcQuestData(mob).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		int plate = player.getEquipment().itemSetPartsEquipped(9);
		int chain = player.getEquipment().itemSetPartsEquipped(8);
		int leather = player.getEquipment().itemSetPartsEquipped(7);
		int cloth = player.getEquipment().itemSetPartsEquipped(6);
		int startDialog = 4762;

		if(plate != 5 && chain != 5 && leather != 5 && cloth != 5)
			startDialog = 4848;
		
		if(defaultQuestNoneDialog(env, 203771, startDialog))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0); // ignore 7 bits
		int var1 = (qs.getQuestVars().getQuestVars() >> 7) & 0x7F;

		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203771)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						if(var == 1)
							return sendQuestDialog(env, 1352);
						if(var == 2 && var1 == 30)
							return sendQuestDialog(env, 1693);
						if(var == 3)
							return sendQuestDialog(env, 2034);
					case 34:
						return defaultQuestItemCheck(env, 0, 1, false, 10000, 10001);
					case 2035:
						if(var == 3)
						{
							if(player.getCommonData().getDp() < 4000 || player.getInventory().getItemCountByItemId(186000040) == 0)
								return sendQuestDialog(env, 2120);
							else if(defaultCloseDialog(env, 3, 0, true, false, 0, 0, 186000040, 1))
							{
								player.getCommonData().setDp(0);
								return sendQuestDialog(env, 5);
							}
						}
						break;
					case 10001:
						return defaultCloseDialog(env, 1, 2);
					case 10002:
						return defaultCloseDialog(env, 2, 3);
				}
			}
			return false;
		}
		return defaultQuestRewardDialog(env, 203771, 0);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int var0 = qs.getQuestVarById(0);
		if (var0 != 2)
			return false;
		
		int var = qs.getQuestVars().getQuestVars();
		int var1 = (var >> 7) & 0x7F;
		int var2 = (var >> 14) & 0x7F;
		int var3 = (var >> 21) & 0x7F;
		boolean killed = false;
		
		int targetId = env.getTargetId();

		if((targetId == 256617 || targetId == 253720 || targetId == 254513) && qs.getQuestVarById(0) == 2)
		{
			switch(targetId)
			{
				case 256617:
					if(var1 >= 0 && var1 < 30)
					{
						++var1;
						killed = true;
					}
					break;
				case 253720:
					if(var2 >= 0 && var2 < 30)
					{
						++var2;
						killed = true;
					}
					break;
				case 254513:
					if(var3 >= 0 && var3 < 30)
					{
						++var3;
						killed = true;
					}
					break;
			}
		}
		if(killed)
		{
			var = var3;
			var = ((var << 7) | var2);
			var = ((var << 7) | var1);
			var = ((var << 7) | var0);
			qs.setQuestVar(var);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
