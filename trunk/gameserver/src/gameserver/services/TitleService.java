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

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerTitleListDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.player.TitleList;
import gameserver.network.aion.serverpackets.SM_TITLE_LIST;
import gameserver.utils.PacketSendUtility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TitleService
{
	public static boolean isExpired(long title_expires_time, long title_date)
	{
		if(title_expires_time > 0)
		{
			long timeLeft = (title_date + (title_expires_time * 1000L)) - System.currentTimeMillis();
			if(timeLeft < 0)
			{
				return true;
			}
		}
		return false;
	}

	public static void removeTitle(int playerId, int titleId)
	{
		DAOManager.getDAO(PlayerTitleListDAO.class).removeTitle(playerId, titleId);
	}

	public static void removeExpiredTitles(Player player)
	{
		TitleList titleList = player.getTitleList();
		List<Integer> delTitles = new ArrayList<Integer>();

		for(Title title : titleList.getTitles())
		{
			if(TitleService.isExpired(title.getTitleExpiresTime(), title.getTitleDate()))
			{
				delTitles.add(title.getTitleId());
			}
		}

		Iterator<Integer> iterator = delTitles.iterator();
		while(iterator.hasNext())
		{
			int titleId = iterator.next();
			removeTitle(player.getObjectId(), titleId);
			titleList.delTitle(titleId);
		}
	}

	public static void checkPlayerTitles(Player player)
	{
		TitleList titleList = player.getTitleList();

		removeExpiredTitles(player);

		if(player.getCommonData().getTitleId() > 0)
		{
			if(titleList.canAddTitle(player.getCommonData().getTitleId()))
			{
				int titleId = -1;
				player.getCommonData().setTitleId(titleId);
				PacketSendUtility.sendPacket(player, new SM_TITLE_LIST(titleId));
				PacketSendUtility.broadcastPacket(player, (new SM_TITLE_LIST(player.getObjectId(), titleId)));
				PacketSendUtility.sendMessage(player, "The usage time of title has expired.");
			}
		}
	}
}