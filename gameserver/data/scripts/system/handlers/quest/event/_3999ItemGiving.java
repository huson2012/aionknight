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

package quest.event;

import gameserver.configs.main.EventConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.utils.PacketSendUtility;

public class _3999ItemGiving extends QuestHandler
{
	private final static int	questId	= 3999;
	
	public _3999ItemGiving()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int itemId = 0;
		Player player = env.getPlayer();
		
		switch (env.getPlayer().getCommonData().getRace())
		{
			case ASMODIANS:
				if (env.getTargetId() == 799703)
					itemId = EventConfig.EVENT_GIVEJUICE_ASMOS;
				else if (env.getTargetId() == 798416)
					itemId = EventConfig.EVENT_GIVECAKE_ASMOS;
				break;
			case ELYOS:
				if (env.getTargetId() == 799702)
					itemId = EventConfig.EVENT_GIVEJUICE_ELYOS;
				else if (env.getTargetId() == 798414)
					itemId = EventConfig.EVENT_GIVECAKE_ELYOS;
				break;
		}
		
		if (itemId == 0)
			return false;
		
		int targetId = env.getVisibleObject().getObjectId();
		switch(env.getDialogId())
		{
			case -1:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1011, 0));
				return true;
			case 1012:
			{
				Storage inventory = player.getInventory();
				if (inventory.getItemCountByItemId(itemId) > 0)
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1097, 0));
					return true;
				}
				else
				{
					if (defaultQuestGiveItem(env, itemId, 1))
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1012, 0));
					return true;
				}
			}
		}
		return false;
		
	}
	
	@Override
	public void register()
	{
		// Juice
		qe.setNpcQuestData(799702).addOnTalkEvent(questId); // Laylin (elyos)
		qe.setNpcQuestData(799703).addOnTalkEvent(questId); // Ronya (asmodian)
		// Cakes
		qe.setNpcQuestData(798414).addOnTalkEvent(questId); // Brios (elyos)
		qe.setNpcQuestData(798416).addOnTalkEvent(questId); // Bothen (asmodian)
	}
}