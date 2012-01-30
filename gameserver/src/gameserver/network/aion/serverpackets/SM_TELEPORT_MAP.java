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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;

public class SM_TELEPORT_MAP extends AionServerPacket
{
	private int	targetObjectId;
	private Player	player;
	private TeleporterTemplate teleport;
	public Npc npc;

	private static final Logger	log	= Logger.getLogger(SM_TELEPORT_MAP.class);

	public SM_TELEPORT_MAP(Player player, int targetObjectId, TeleporterTemplate teleport)
	{
		this.player = player;
		this.targetObjectId = targetObjectId;
		this.npc = (Npc)World.getInstance().findAionObject(targetObjectId);
		this.teleport = teleport;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if ((teleport != null) && (teleport.getNpcId() != 0) && (teleport.getTeleportId() != 0))
		{
			writeD(buf, targetObjectId);
			writeH(buf, teleport.getTeleportId());
		}
		
		else
		{
			PacketSendUtility.sendMessage(player, "Missing info at npc_teleporter.xml with npcid: "+ npc.getNpcId());
			log.info(String.format("Missing teleport info with npcid: %d", npc.getNpcId()));
		}
	}
}