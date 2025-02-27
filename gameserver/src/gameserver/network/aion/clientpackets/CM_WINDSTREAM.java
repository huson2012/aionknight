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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

/**
 * Packet concerning windstreams.
 */
public class CM_WINDSTREAM extends AionClientPacket
{
	int teleportId;
	int distance;
	int state;

	private static final Logger	log	= Logger.getLogger(CM_WINDSTREAM.class);

	/**
	 * @param opcode
	 */
	public CM_WINDSTREAM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		teleportId = readD(); //typical teleport id (ex : 94001 for talloc hallow in inggison)
		distance = readD();	 // 600 for talloc.
		state = readD(); // 0 or 1.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;
		
		switch(state)
		{
		case 0:
		case 4:
		case 8:
			//TODO:	Find in which cases second variable is 0 & 1
			//		Jego's example packets had server refuse with 0 and client kept retrying.
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state,1));
			break;
		case 1:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM, teleportId, distance), true);
			player.setEnterWindstream(1);
			break;
		case 2:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_END, 0, 0), true);			
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state,1));
			log.info("Player entered Windstream with telID: " + teleportId + ", distance: " + distance + " and state: " + state);
			break;
		case 7:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_BOOST, 0, 0), true);
			player.setEnterWindstream(7);			
			break;
		default:
			log.error("Unknown Windstream state #" + state + " was found!" );
		}
	}
}
