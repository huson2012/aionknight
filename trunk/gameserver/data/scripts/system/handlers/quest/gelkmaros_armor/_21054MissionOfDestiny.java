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

package quest.gelkmaros_armor;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _21054MissionOfDestiny extends QuestHandler
{
    private final static int questId = 21054;
	
    public _21054MissionOfDestiny()
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
	        if(targetId == 799318) //Batalrion
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
	        if(targetId == 799318) //Batalrion
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
                case 799318: //Batalrion
                    switch (env.getDialogId())
					{
                        case 26:
                        //
                            return sendQuestDialog(env, 2375);
                         case 2034:
                        //
                            return sendQuestDialog(env, 2034);
                        //
                        case 34:
                        //Collect Hardened Drakscale (30)
                        //Collect Kinah (150000)
                            if (QuestService.collectItemCheck(env, true))
	          {
                                player.getInventory().removeFromBagByItemId(182207842, 30);
                                player.getInventory().removeFromBagByItemId(182400001, 150000);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                //
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
            if(targetId == 799318) //Batalrion
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		if(qs != null || !lvlCheck)
			return false;
		QuestService.startQuest(env, QuestStatus.START);
		return true;
	}
	
    @Override
    public void register()
	{
	    qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(799318).addOnQuestStart(questId); //Batalrion
        qe.setNpcQuestData(799318).addOnTalkEvent(questId); //Batalrion
    }
}