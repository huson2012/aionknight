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

package quest.esoterrace;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _28411Esoterrace_Blockade extends QuestHandler
{
    private final static int questId = 28411;
	
    public _28411Esoterrace_Blockade()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		
        if(qs == null || qs.getStatus() == QuestStatus.NONE)
        {
	        if(targetId == 799589) //Loqui.
	        {
		       if(env.getDialogId() == 26)
		       {
			      return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs == null)
            return false;
			
        if(qs == null || qs.getStatus() == QuestStatus.COMPLETE) 
        {
	        if(targetId == 799589) //Loqui.
	        {
		       if(env.getDialogId() == 26)
		       {
			     return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                case 799589: //Loqui.
                    switch (env.getDialogId())
					{
                        case 26:
                            return sendQuestDialog(env, 2375);
                        case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        //Collect Drazma Heart (1)
                            if (QuestService.collectItemCheck(env, true))
	          {                 
			                    player.getInventory().removeFromBagByItemId(182215021, 1); //Drazma Heart.
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else
							{
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                //
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 799589) //Loqui.
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(799589).addOnQuestStart(questId); //Loqui.
        qe.setNpcQuestData(799589).addOnTalkEvent(questId); //Loqui.
    }
}