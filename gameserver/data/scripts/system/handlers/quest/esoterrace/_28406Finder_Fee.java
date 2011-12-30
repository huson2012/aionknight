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

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _28406Finder_Fee extends QuestHandler
{
    private final static int questId = 28406;
	
    public _28406Finder_Fee()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        Player player = env.getPlayer();
		
        if (env.getTargetId() == 0)
            return defaultQuestStartItem(env);
			
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
			
        int var = qs.getQuestVarById(0);
		
        if (qs.getStatus() == QuestStatus.START)
		{
            if (env.getTargetId() == 799557)
			{
                switch (env.getDialogId())
				{
                    case 26:
                        if (var == 0)
                        return sendQuestDialog(env, 2375);
                    case 1009:
                        defaultQuestRemoveItem(env, 182215015, 1);
                        return defaultCloseDialog(env, 0, 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799557, 2375);
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(799557).addOnTalkEvent(questId);
    }
}