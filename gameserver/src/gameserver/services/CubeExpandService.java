/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
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

package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.templates.CubeExpandTemplate;
import gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

public class CubeExpandService
{
	private static final Logger	log = Logger.getLogger(CubeExpandService.class);
	private static final int MIN_EXPAND	= 0;
	private static final int MAX_EXPAND	= 9;
	
	/**
	 * ���������� ��� � ��������� ��� ��� ������������� ����������
	 * 
	 * @param player
	 * @param npc
	 */
	public static void expandCube(final Player player, Npc npc)
	{
		final CubeExpandTemplate expandTemplate = DataManager.CUBEEXPANDER_DATA.getCubeExpandListTemplate(npc.getNpcId());

		if(expandTemplate == null)
		{
			log.error("Cube Expand Template could not be found for Npc ID: " + npc.getObjectId());
			return;
		}
		
		if(npcCanExpandLevel(expandTemplate, cubeLevel(player) + 1)
			&& validateNewSize(cubeLevel(player) + 1))
		{
			/**
			 * ���������, ����� �� ����� ��������� ����������� ����� ��� ���������� ����
			 */
			final int price = getPriceByLevel(expandTemplate, cubeLevel(player) + 1);

			RequestResponseHandler responseHandler = new RequestResponseHandler(npc){
				@Override
				public void acceptRequest(Creature requester, Player responder)
				{
					if(price > player.getInventory().getKinahItem().getItemCount())
					{
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.CUBEEXPAND_NOT_ENOUGH_KINAH);
						return;
					}
					if(player.getInventory().decreaseKinah(price))
						expand(responder);
					
				}

				@Override
				public void denyRequest(Creature requester, Player responder)
				{
				}
			};

			boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING,
				responseHandler);
			if(result)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
					SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING, 0, String.valueOf(price)));
			}
		}
		else
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300430));
	}
	/**
	 * ��������� ���
	 * @param player
	 */
	public static void expand(Player player)
	{
		if (!validateNewSize(cubeLevel(player) + 1))
			return;
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300431, "9")); // 9 ������ ���������
		player.setCubesize(player.getCubeSize() + 1);
		PacketSendUtility.sendPacket(player, new SM_CUBE_UPDATE(player, 0));
	}
	
	private static int cubeLevel(Player player)
	{
		int cubeLevel = player.getCubeSize();
		if(player.getCommonData().getRace() == Race.ASMODIANS)
		{
			QuestState qs = player.getQuestStateList().getQuestState(2937);
			if(qs != null && qs.getStatus() == QuestStatus.COMPLETE)
				cubeLevel -= 1;
			qs = player.getQuestStateList().getQuestState(2833);
			if(qs != null && qs.getStatus() == QuestStatus.COMPLETE)
				cubeLevel -= 1;
		}
		if(player.getCommonData().getRace() == Race.ELYOS)
		{
			QuestState qs = player.getQuestStateList().getQuestState(1947);
			if(qs != null && qs.getStatus() == QuestStatus.COMPLETE)
				cubeLevel -= 1;
			qs = player.getQuestStateList().getQuestState(1797);
			if(qs != null && qs.getStatus() == QuestStatus.COMPLETE)
				cubeLevel -= 1;
			qs = player.getQuestStateList().getQuestState(1800);
			if(qs != null && qs.getStatus() == QuestStatus.COMPLETE)
				cubeLevel -= 1;
		}
		return cubeLevel;
	}
	/**
	 * ��������� �� ������� ��� �������� ��� 
	 * 
	 * @param level
	 * @return true or false
	 */
	private static boolean validateNewSize(int level)
	{
		if(level < MIN_EXPAND || level > MAX_EXPAND)
			return false;
		return true;
	}
	/**
	 * ��������� ������������� �� ������� ������ ���������� ��� ���������� ����
	 * 
	 * @param clist
	 * @param level
	 * @return true or false
	 */
	private static boolean npcCanExpandLevel(CubeExpandTemplate clist, int level)
	{
		if(!clist.contains(level))
			return false;
		return true;
	}
	/**
	 * ��� ��� ������ ������ ��� ���� - ��������� ������ ))) �����-���� �������� ��� ����������.
	 * 
	 * @param clist
	 * @param level
	 * @return
	 */
	private static int getPriceByLevel(CubeExpandTemplate clist, int level)
	{
		return clist.get(level).getPrice();
	}
}