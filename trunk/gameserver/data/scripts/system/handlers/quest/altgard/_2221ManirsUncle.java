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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _2221ManirsUncle extends QuestHandler
{

	private final static int	questId	= 2221;

	public _2221ManirsUncle()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203607).addOnQuestStart(questId);
		qe.setNpcQuestData(203607).addOnTalkEvent(questId);
		qe.setNpcQuestData(203608).addOnTalkEvent(questId);
		qe.setNpcQuestData(700214).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
		{
			if(targetId == 203607)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 203608:
				{
					if (qs.getQuestVarById(0) == 0)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 1352);
						else if(env.getDialogId() == 10000)
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
					if (qs.getQuestVarById(0) == 2)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 2375);
						else if(env.getDialogId() == 1009)
						{
							player.getInventory().removeFromBagByItemId(182203215, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultQuestEndDialog(env);
						}
						else
							return defaultQuestEndDialog(env);
					}
				}
				break;
				case 700214:
					if ((qs.getQuestVarById(0) == 1 || qs.getQuestVarById(0) == 2) && env.getDialogId() == -1)
					{
						qs.setQuestVarById(0, 2);
						updateQuestStatus(env);
					}
						return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203608)
				return defaultQuestEndDialog(env);
		}
		return false;
	}
}