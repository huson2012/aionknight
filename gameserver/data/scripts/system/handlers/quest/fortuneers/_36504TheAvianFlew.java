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

package quest.fortuneers;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _36504TheAvianFlew extends QuestHandler
{
	private final static int	questId	= 36504;

	public _36504TheAvianFlew()
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
			if(env.getTargetId() == 700759)
				return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
		}
		if(defaultQuestRewardDialog(env, 799837, 10002) || defaultQuestRewardDialog(env, 799838, 10002))
			return true;
		else
			return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, 216608, 0, true);
	}

	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		VisibleObject vO = env.getVisibleObject();
		if(vO instanceof Npc)
		{
			Npc fruits = (Npc)vO;
			if(fruits.getNpcId() == 700759)
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 216608, fruits.getX(), fruits.getY(), fruits.getZ(), (byte) 0, true);
		}
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(700759).addOnTalkEvent(questId);
		qe.setNpcQuestData(799837).addOnTalkEvent(questId);
		qe.setNpcQuestData(799838).addOnTalkEvent(questId);
		qe.setNpcQuestData(216608).addOnKillEvent(questId);
	}
}
