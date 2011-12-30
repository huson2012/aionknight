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

package quest.ishalgen;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2001ThinkingAhead extends QuestHandler
{
	private final static int	questId	= 2001;
	private final static int[]	mobs	= {210368, 210369};
	private final static int[]	npcs	= {203518, 700093};

	public _2001ThinkingAhead()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
		for(int mob: mobs)
			qe.setNpcQuestData(mob).addOnKillEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
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
			switch(env.getTargetId())
			{
				case 203518:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 2)
								return sendQuestDialog(env, 1694);
						case 1012:
							return defaultQuestMovie(env, 51);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
						case 34:
							return defaultQuestItemCheck(env, 1, 2, false, 1694, 1693);
					}
					break;
				case 700093:
					if(env.getDialogId() == -1 && player.getInventory().getItemCountByItemId(182203002) < 4)
					return defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
				break;
			}
		}
		return defaultQuestRewardDialog(env, 203518, 2034);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, mobs, 3, 8) || defaultQuestOnKillEvent(env, mobs, 8, true))
			return true;
		else
			return false;
	}
}