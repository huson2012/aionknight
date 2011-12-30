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

package quest.altgard;

import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _2288PutYourMoneyWhereYourMouthIs extends QuestHandler
{

	private final static int	questId	= 2288;
	private final static int [] mob_ids = {210564, 210584, 210581, 201047, 210436, 210437, 210440};

	public _2288PutYourMoneyWhereYourMouthIs()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203621).addOnQuestStart(questId);
		qe.setNpcQuestData(203621).addOnTalkEvent(questId);
		for(int mob_id: mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, mob_ids, 1, 4);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getTargetId() == 203621)
			{
				switch (env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1007:
						return sendQuestDialog(env, 4);
					case 1002:
						return sendQuestDialog(env, 1003);
					case 1003:
						return sendQuestDialog(env, 1004);
					case 10000:
						if(!env.getPlayer().getQuestTimerOn() && QuestService.startQuest(env, QuestStatus.START))
						{
							QuestService.questTimerStart(env, 600);
							return defaultCloseDialog(env, 0, 1);
						}
						else
							return false;
				}
			}
		}	
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203621)
			{
				if(var == 4)
				{
					if(env.getDialogId() == 26)
						return sendQuestDialog(env, 1352);
					if(env.getDialogId() == 1009)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						QuestService.questTimerEnd(env);
						return defaultQuestEndDialog(env);
					}
				}
			}
		}
		return defaultQuestRewardDialog(env, 203621, 0);
	}
}