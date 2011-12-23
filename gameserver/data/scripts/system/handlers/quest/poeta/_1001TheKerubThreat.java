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

package quest.poeta;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _1001TheKerubThreat extends QuestHandler
{
	private final static int	questId	= 1001;

	public _1001TheKerubThreat()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(210670).addOnKillEvent(questId);
		int[] npcs = {203071, 203067};
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
		qe.addQuestLvlUp(questId);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, 210670, 1, 6);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, 1100, false);
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
			if(env.getTargetId() == 203071)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						else if(var == 6)
							return sendQuestDialog(env, 1352);
						else if(var == 7)
							return sendQuestDialog(env, 1693);
						break;
					case 1012:
						return defaultQuestMovie(env, 15);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
					case 10001:
						return defaultCloseDialog(env, 6, 7);
					case 34:
						return defaultQuestItemCheck(env, 7, 8, true, 1694, 1779);
				}
			}
		}
		else
		{
			if(env.getDialogId() == 10002)
				return defaultCloseDialog(env, 8, 0);
		}
		return defaultQuestRewardDialog(env, 203067, 0);
	}
}