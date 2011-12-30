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

package quest.esoterrace;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _28409Make_The_Blade_Complete extends QuestHandler
{
	private final static int questId = 28409;
	
	public _28409Make_The_Blade_Complete()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(env.getTargetId() == 0)
			return defaultQuestStartItem(env);
		
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 799557:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 205237:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
							return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2, true, false);
					}
					break;
			 }
		}
		return defaultQuestRewardDialog(env, 799557, 2375);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 215795, 0, 1))
		   
			return true;
		else
			return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(799552).addOnQuestStart(questId);
		qe.setNpcQuestData(205232).addOnTalkEvent(questId);
		qe.setNpcQuestData(215795).addOnKillEvent(questId);
	}
}