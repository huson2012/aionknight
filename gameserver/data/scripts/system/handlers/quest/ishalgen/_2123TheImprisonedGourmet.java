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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class _2123TheImprisonedGourmet extends QuestHandler
{
   private final static int questId = 2123;

   public _2123TheImprisonedGourmet()
   {
      super(questId);
   }

   @Override
   public void register()
   {
      qe.setNpcQuestData(203550).addOnQuestStart(questId);
      qe.setNpcQuestData(203550).addOnTalkEvent(questId);
	  qe.setNpcQuestData(700128).addOnTalkEvent(questId);
   }

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if(player.getCommonData().getLevel() < 7)
			return false;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId == 203550)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1011);
            else
               return defaultQuestStartDialog(env);
         }
			}
			else if(qs.getStatus() == QuestStatus.START)
			{
				long itemCount;
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 0)
				{
					return sendQuestDialog(env, 1352);
				}
				else if(env.getDialogId() == 10000 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182203121);
					if(itemCount > 0)
					{
						player.getInventory().removeFromBagByItemId(182203121, 1);
						qs.setQuestVar(5);
						updateQuestStatus(env);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else if(env.getDialogId() == 10001 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182203122);
					if(itemCount > 0)
					{	
						player.getInventory().removeFromBagByItemId(182203122, 1);
						qs.setQuestVar(6);
						updateQuestStatus(env);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 6);
					}
					else
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else if(env.getDialogId() == 10002 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182203123);
					if(itemCount > 0)
					{
						player.getInventory().removeFromBagByItemId(182203123, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						qs.setQuestVar(7);
						updateQuestStatus(env);
						return sendQuestDialog(env, 7);
					}
					else
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else
					return defaultQuestEndDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.REWARD)
			{
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 5)
				{
					return sendQuestDialog(env, 5);
				}
				else if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 6)
				{
					return sendQuestDialog(env, 6);
				}
				else if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 7)
				{
					return sendQuestDialog(env, 7);
				}
				else
				{
				return defaultQuestEndDialog(env);
				}
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(targetId == 700128)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						qs.setQuestVar(0);
						updateQuestStatus(env);
					}
				}, 3000);
				return true;
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else
		{
		return defaultQuestEndDialog(env);
		}
	}
}