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

public class _2012Encroachers extends QuestHandler
{
	private final static int	questId	= 2012;
	private final static int[]	mob_ids	= { 210715 };	//Brute lvl 10
	
	public _2012Encroachers()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203559).addOnTalkEvent(questId);
		qe.addQuestLvlUp(questId);
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
	
	/** Disponible des le level 10 **/
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		/** Initialisation de l'event **/
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		/**Si on start la quete **/
		if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 203559)
			{

				switch(env.getDialogId())
				{
					case 26:			
						if(var == 0)		//Initialisation du dialogue
							return sendQuestDialog(env, 1011);
						else if(var <= 5)	//Rendu de la quete
						{
							return sendQuestDialog(env, 1352);
						}
						else if(var >= 5)
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
						}
					case 10000:
					case 10001:
						if(var == 0 || var == 5)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
				}
				
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203559)
			{
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 210715, 1, 4) || defaultQuestOnKillEvent(env, 210715, 4, true))
			return true;
		else
			return false;
	}
}