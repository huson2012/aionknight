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

package quest.greater_stigma_quest;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _4936SecretOfTheGreaterStigma extends QuestHandler
{
	private final static int	questId	= 4936;
	private final static int[]	npc_ids	= { 204051, 204837};         

    public _4936SecretOfTheGreaterStigma()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
			
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204051)//Vergelmir
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else return defaultQuestStartDialog(env);
        
      }  
      return false; 
    }
	
    
    int var = qs.getQuestVarById(0);
    
    if(qs.getStatus() == QuestStatus.REWARD)
     {
      if(targetId == 204051)//Vergelmir
			{
			  return defaultQuestEndDialog(env);
      }
      return false;
     }
    else if(qs.getStatus() == QuestStatus.START)
		{
		 
     if(targetId == 204051 && var == 1)//Vergelmir
			{
			  switch(env.getDialogId())
        {
         case 26:
          return sendQuestDialog(env, 2375);
         case 34:
          if(QuestService.collectItemCheck(env, true))				
					   {
						  qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
             return sendQuestDialog(env, 5);
            }
					else
						  return sendQuestDialog(env, 2716);
         }
       
      }	
      else if(targetId == 204837 && var == 0)//Hresvelgr
      {
        switch(env.getDialogId())
					{
					case 26:
					 return sendQuestDialog(env, 1352);
          case 10000:
				   PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
           qs.setQuestVarById(0, var + 1);
				   updateQuestStatus(env);
           return true;
          }
      }   
        
       
   return false;
   }
   return false; 
  }

	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204051).addOnQuestStart(questId);	//Vergelmir
    for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
  
}  
