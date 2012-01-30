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

package gameserver.network.aion.clientpackets;

import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_PLAYERS;
import gameserver.services.AbyssRankingService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.ArrayList;

/**
 * In this packets aion client is asking for player abyss rankings
 */
public class CM_ABYSS_RANKING_PLAYERS extends AionClientPacket
{

	private Race queriedRace;
	private int raceId;
		
	private static final Logger log = Logger.getLogger(CM_ABYSS_RANKING_PLAYERS.class);
	
	public CM_ABYSS_RANKING_PLAYERS(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		raceId = readC();
		switch(raceId)
		{
			case 0: queriedRace = Race.ELYOS; break;
			case 1: queriedRace = Race.ASMODIANS; break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		// calculate rankings and send packet
		if(queriedRace != null)
		{
			ArrayList<AbyssRankingResult> results = AbyssRankingService.getInstance().getInviduals(queriedRace);
			PacketSendUtility.sendPacket(getConnection().getActivePlayer(), new SM_ABYSS_RANKING_PLAYERS(results,queriedRace, getConnection().getActivePlayer()));
		}
		else
		{
			log.warn("Received invalid raceId: " + raceId);
		}
	}
}
