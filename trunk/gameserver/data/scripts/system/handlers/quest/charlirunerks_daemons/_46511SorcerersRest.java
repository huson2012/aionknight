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

package quest.charlirunerks_daemons;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _46511SorcerersRest extends QuestHandler
{
	private final static int	questId	= 46511;

	public _46511SorcerersRest()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestStartDaily(env))
			return true;
		
		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 700775)
				return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
		}
		if(defaultQuestRewardDialog(env, 799882, 10002) || defaultQuestRewardDialog(env, 799883, 10002))
			return true;
		else
			return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, 216629, 0, true);
	}

	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		VisibleObject vO = env.getVisibleObject();
		if(vO instanceof Npc)
		{
			Npc scroll = (Npc)vO;
			if(scroll.getNpcId() == 700775)
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 216629, scroll.getX(), scroll.getY(), scroll.getZ(), (byte) 0, true);
		}
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(700775).addOnTalkEvent(questId);
		qe.setNpcQuestData(799882).addOnTalkEvent(questId);
		qe.setNpcQuestData(799883).addOnTalkEvent(questId);
		qe.setNpcQuestData(216629).addOnKillEvent(questId);
	}
}
