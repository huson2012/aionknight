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

package quest.miragent_holy_templar_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _3935ShoulderTheBurden extends QuestHandler
{
	private final static int	questId	= 3935;
	private final static int[]	npcs = {203701, 203316, 203702, 203329, 203752};

	public _3935ShoulderTheBurden()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 203701, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203316:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				}	break;
				case 203702:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
				} break;
				case 203329:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
							else if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
						case 10003:
							return defaultCloseDialog(env, 3, 0);
						case 34:
							return defaultQuestItemCheck(env, 3, 4, false, 10000, 10001, 182206090, 1);
					}
				} break;
				case 203752:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10255:
							if(player.getInventory().getItemCountByItemId(186000080) >= 1)
								return defaultCloseDialog(env, 4, 0, true, false, 0, 0, 186000080, 1);
							else
								return sendQuestDialog(env, 2461);
					}
				} break;
			}
		}
		return defaultQuestRewardDialog(env, 203701, 10002);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203701).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
}
