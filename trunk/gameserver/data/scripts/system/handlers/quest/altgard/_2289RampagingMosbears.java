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

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2289RampagingMosbears extends QuestHandler
{

	private final static int	questId	= 2289;
	private final static int[]	mob_ids	= {210564, 210584};
	
	public _2289RampagingMosbears()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203616).addOnQuestStart(questId);
		qe.setNpcQuestData(203616).addOnTalkEvent(questId);
		qe.setNpcQuestData(203618).addOnTalkEvent(questId);
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
	
		if(defaultQuestNoneDialog(env, 203616))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203616:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 1352);
							else if(var == 7)
								return sendQuestDialog(env, 2034);
						case 10001:
							return defaultCloseDialog(env, 5, 6);
						case 34:
							return defaultQuestItemCheck(env, 7, 0, true, 5, 2120);
					}
				}
				break;
				case 203618:
				{
					switch(env.getDialogId())
					{
						case 26:
							if (var == 6)
								return sendQuestDialog(env, 1693);
						case 10002:
							return defaultCloseDialog(env, 6, 7);
					}
				}
				break;
			}
		}
		return defaultQuestRewardDialog(env, 203616, 0);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, mob_ids, 0, 5);
	}
}