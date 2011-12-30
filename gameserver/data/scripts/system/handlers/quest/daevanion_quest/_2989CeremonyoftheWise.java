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

package quest.daevanion_quest;

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2989CeremonyoftheWise extends QuestHandler
{
	private final static int	questId	= 2989;
	private final static int[]	npcs	= {204146, 204056, 204057, 204058, 204059};

	public _2989CeremonyoftheWise()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204146).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 204146))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			PlayerClass playerClass = player.getCommonData().getPlayerClass();
			switch(env.getTargetId())
			{
				case 204056:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR)
									return sendQuestDialog(env, 1352);
								else
									return sendQuestDialog(env, 1438);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204057:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER)
									return sendQuestDialog(env, 1693);
								else
									return sendQuestDialog(env, 1779);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204058:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER)
									return sendQuestDialog(env, 2034);
								else
									return sendQuestDialog(env, 2120);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				case 204059:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER)
									return sendQuestDialog(env, 2375);
								else
									return sendQuestDialog(env, 2461);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204146:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 2716);
							else if(var == 2)
								return sendQuestDialog(env, 3057);
							else if(var == 3)
							{
								if(player.getCommonData().getDp() < 4000)
									return sendQuestDialog(env, 3484);	
								else
									return sendQuestDialog(env, 3398);
							}
							else if(var == 4)
							{
								if(player.getCommonData().getDp() < 4000)
									return sendQuestDialog(env, 3825);	
								else
									return sendQuestDialog(env, 3739);
							}
						case 1009:
							if(var == 3 || var == 4)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								defaultQuestMovie(env, 137);
								player.getCommonData().setDp(0);
								return sendQuestDialog(env, 5);
							}
						case 10001:
							if(defaultCloseDialog(env, 1, 2))
								return sendQuestDialog(env, 3057);
						case 10003:
							return defaultCloseDialog(env, 2, 3);
						case 10004:
							return defaultCloseDialog(env, 2, 4);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 204146, 0);
	}
}
