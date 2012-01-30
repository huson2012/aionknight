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
import gameserver.dao.PlayerCmotionListDAO;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_CMOTION;
import gameserver.utils.PacketSendUtility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CmotionService
{
	public static boolean isExpired(long expires_time, long date)
	{
		if(expires_time > 0)
		{
			long timeLeft = (date + (expires_time * 1000L)) - System.currentTimeMillis();
			if(timeLeft < 0)
			{
				return true;
			}
		}
		return false;
	}

	public static void removeCmotion(int playerId, int cmotionId)
	{
		DAOManager.getDAO(PlayerCmotionListDAO.class).removeCmotion(playerId, cmotionId);
	}

	public static void removeExpiredCmotions(Player player)
	{
		CmotionList cmotionList = player.getCmotionList();
		List<Integer> delCmotions = new ArrayList<Integer>();
		boolean removed = false;

		for(Cmotion cmotion : cmotionList.getCmotions())
		{
			if(CmotionService.isExpired(cmotion.getCmotionExpiresTime(), cmotion.getCmotionCreationTime()))
			{
				delCmotions.add(cmotion.getCmotionId());
			}
		}

        for (Integer cmotionId : delCmotions)
        {
            removeCmotion(player.getObjectId(), cmotionId);
            cmotionList.remove(cmotionId);
            removed = true;
        }

		if(removed)
		{
			PacketSendUtility.sendPacket(player, new SM_CMOTION(player));
			PacketSendUtility.sendMessage(player, "The usage time of cmotion has expired.");
		}
	}
}
