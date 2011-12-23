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

public class _1122DeliveringPernossRobe extends QuestHandler
{
	private final static int	questId	= 1122;

	public _1122DeliveringPernossRobe()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		int[] npcs = {203060, 790001};
		qe.setNpcQuestData(203060).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 203060, 182200216, 1))
			return true;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 790001)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1352);
					case 10000:
						if(player.getInventory().getItemCountByItemId(182200218) > 0)
						{
							defaultCloseDialog(env, 0, 1, true, false, 0, 0, 182200216, 1);
							defaultQuestRemoveItem(env, 182200218, 1);
							return sendQuestDialog(env, 1523);
						}
						else
							return sendQuestDialog(env, 1608);
					case 10001:
						if (player.getInventory().getItemCountByItemId(182200219) > 0)
						{
							defaultCloseDialog(env, 0, 2, true, false, 0, 0, 182200216, 1);
							defaultQuestRemoveItem(env, 182200219, 1);
							return sendQuestDialog(env, 1438);
						}
						else
							return sendQuestDialog(env, 1608);
					case 10002:
						if (player.getInventory().getItemCountByItemId(182200220) > 0)
						{
							defaultCloseDialog(env, 0, 3, true, false, 0, 0, 182200216, 1);
							defaultQuestRemoveItem(env, 182200220, 1);
							return sendQuestDialog(env, 1353);
						}
						else
							return sendQuestDialog(env, 1608);
				}
			}
		}
		return defaultQuestRewardDialog(env, 790001, 0, var-1);
	}
}