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

public class _18405MemoriesintheCornerofHisMind extends QuestHandler 
{
    private final static int questId = 18405;

    public _18405MemoriesintheCornerofHisMind() 
	{
        super(questId);
    }

    @Override
    public void register() 
	{
        qe.setNpcQuestData(799553).addOnTalkEvent(questId);
		qe.setNpcQuestData(799552).addOnTalkEvent(questId);
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
            if (env.getTargetId() == 799553) 
			{
                switch (env.getDialogId()) 
				{
                    case 26:
                        if (var == 1)
                    return sendQuestDialog(env, 1011);

					case 1009:
                        defaultQuestRemoveItem(env, 188051351, 1);
                    return defaultCloseDialog(env, 0, 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799552, 2375);
    }
}