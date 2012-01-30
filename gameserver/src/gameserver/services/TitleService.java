/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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

        for (Integer titleId : delTitles)
        {
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
