/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
 *
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������)
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package quest.event;

import commons.utils.Rnd;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.bonus.CutSceneBonus;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.HandlerResult;
import gameserver.quest.QuestEngine;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

public class _80016EventSockHop extends QuestHandler
{

	private final static int	questId	= 80016;
	
	public _80016EventSockHop()
	{
		super(questId);
	}
	
	@Override
    public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus)
    {
		if(!(bonus instanceof CutSceneBonus))
		    return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			int var1 = qs.getQuestVarById(1);
			int var2 = qs.getQuestVarById(2);
			qs.setQuestVarById(1, var1 + 1);
			// randomize movie
			if ((var1 == 1 && var2 == 0) || var1 == 0 && Rnd.get() * 100 < 50)
			{
				if (qs.getCompleteCount() == 9)
					ItemService.addItems(player, Collections.singletonList(new QuestItems(188051106, 1)));
				qs.setQuestVarById(2, 1);
				return HandlerResult.SUCCESS;
			}
		}
		return HandlerResult.FAILED;
    }
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if ((qs == null || qs.getStatus() == QuestStatus.NONE) && !onLvlUpEvent(env))
			return false;
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE || 
		    qs.getStatus() == QuestStatus.COMPLETE && qs.getCompleteCount() < 10)
		{
			if (env.getTargetId() == 799763)
			{
				switch (env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 1011);
					case 1002:
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(1, questId, QuestStatus.START, 0));
						if(qs == null)
							player.getQuestStateList().addQuest(questId, qs);
						else
						{
							qs.setStatus(QuestStatus.START);
							qs.setQuestVar(0);
						}
						player.getController().updateNearbyQuests();
						return sendQuestDialog(env, 1003);
					}
					default:
						return defaultQuestStartDialog(env);
				}
			}
			return false;
		}

		int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799763)
			{
				switch(env.getDialogId())
				{
					case -1:
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 2375);
					case 34:
						return defaultQuestItemCheck(env, 0, 1, true, 5, 2716);
				}
			}
		}
		
		return defaultQuestRewardDialog(env, 799763, 0);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Calendar now = Calendar.getInstance();
		Calendar cal1 = new GregorianCalendar(now.get(Calendar.YEAR), 11, 15);
		Calendar cal2 = new GregorianCalendar(now.get(Calendar.YEAR) + 1, 0, 5);
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (cal1.before(now) && cal2.after(now))
		{
			if(!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
				return false;
			
			// Start once
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
				return QuestService.startQuest(env, QuestStatus.START);
		} 
		else if (qs != null)
		{
			// Set as expired
			QuestEngine.getInstance().deleteQuest(player, questId);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId));
			player.getController().updateNearbyQuests();
		}
		return false;
	}
	
    @Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799763).addOnQuestStart(questId);
		qe.setNpcQuestData(799763).addOnTalkEvent(questId);
		qe.setQuestBonusType(InventoryBonusType.MOVIE).add(questId);
	}

}
