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

package quest.carving_out_a_fortune_mission;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _2099ToFacetheFuture extends QuestHandler
{
	private final static int	questId	= 2099;
	private final static int[]	mob_ids	= { 798342, 798343, 798344, 798345, 798346 }; // Legionary, Brigade General Hellion 
	
	public _2099ToFacetheFuture()
	{
		super(questId);
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
		return false;
		
		if(targetId == 203550) //Munin
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					qs.setQuestVar(1);
               		updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
					.getObjectId(), 10));
						return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		else if(targetId == 205020) //Hagen
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					qs.setQuestVar(2); 
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 3001, 0));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}	
			else if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) > 1)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 3001, 0));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}				

		}
		
		else if(targetId == 205118) //Lephar
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 53)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 10004)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				
				else if(env.getDialogId() == 10005)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}					

		}
		
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204052)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 2716);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		
		return false;
	}
		
		@Override
		public boolean onKillEvent(QuestCookie env)
		{
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if(qs == null || qs.getStatus() != QuestStatus.START)
				return false;

			int var = qs.getQuestVarById(0);
			int targetId = 0;
			Npc npc = null;
			if(env.getVisibleObject() instanceof Npc)
			{
				npc = (Npc) env.getVisibleObject();
				targetId = npc.getNpcId();
			}
			switch(targetId)
			{
				case 798345:
				case 798344:
				case 798343:
				case 798342:
					if(var >= 2 && var < 51)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					}
					
					else if(var >= 51)
					{
						QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 798346, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true);
						qs.setQuestVar(52);
						updateQuestStatus(env);
						return true;
					}
			}
			
			switch(targetId)
			{
				case 798346:
					if(var == 52) //Brigade General Hellion 
					{
						QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 205118, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true);	
						qs.setQuestVar(53);
						updateQuestStatus(env);
						return true;
					}
			}
			return false;
		}
		
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null)
		{
			if(player.getCommonData().getLevel() < 50)
				return false;
		
			env.setQuestId(questId);
			QuestService.startQuest(env, QuestStatus.START);
			return true;
		}
		return false;		
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(203550).addOnTalkEvent(questId); // Munin
		qe.setNpcQuestData(205020).addOnTalkEvent(questId); // Hagen
		qe.setNpcQuestData(204052).addOnTalkEvent(questId); // Vidar
		qe.setNpcQuestData(205118).addOnTalkEvent(questId); // Lephar
		qe.addQuestLvlUp(questId);
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
}