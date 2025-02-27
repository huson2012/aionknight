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

package quest.fenris_fangs_quest;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _4943LuckandPersistence extends QuestHandler
{
	private final static int	questId	= 4943;
	private final static int[]	npcs = {204053, 700538, 204096, 204097, 204075};

	public _4943LuckandPersistence()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 204053, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204096:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
						case 34:
							return defaultQuestItemCheck(env, 2, 3, false, 10000, 10001, 182207125, 1);
					}
				}	break;
				case 204097:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 1354:
							if(player.getInventory().decreaseKinah(3400000))
								return defaultCloseDialog(env, 1, 2, 122001275, 1, 0, 0);
							else
								return sendQuestDialog(env, 1438);
					}
				} break;
				case 700538:
					if(env.getDialogId() == -1 && player.getEquipment().getEquippedItemsByItemId(122001275).size() > 0)
						return defaultQuestUseNpc(env, 2, 3, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
					break;
				case 204075:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10255:
							if(player.getInventory().getItemCountByItemId(186000084) >= 1)
								return defaultCloseDialog(env, 3, 0, true, false, 0, 0, 186000084, 1);
							else
								return sendQuestDialog(env, 2120);
					}
				} break;
			}
		}
		return defaultQuestRewardDialog(env, 204053, 10002);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204053).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
}
