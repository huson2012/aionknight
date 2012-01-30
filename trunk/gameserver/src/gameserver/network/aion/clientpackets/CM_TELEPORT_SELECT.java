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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.teleport.TelelocationTemplate;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_TELEPORT_SELECT extends AionClientPacket
{
	/** NPC ID */
	public int targetObjectId;

	/** Destination of teleport */
	public int locId;

	public TelelocationTemplate _tele;

	private TeleporterTemplate teleport;

	public CM_TELEPORT_SELECT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
		targetObjectId = readD();
		locId = readD(); //locationId
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();

		Npc npc = (Npc)World.getInstance().findAionObject(targetObjectId);

		if(activePlayer == null || activePlayer.getLifeStats().isAlreadyDead())
			return;
		
		if(!MathUtil.isIn3dRange(npc, activePlayer, 10))
		{
			Logger.getLogger(this.getClass()).info("[AUDIT]Player "+activePlayer.getName()+" sending fake CM_TELEPORT_SELECT!");
			return;
		}
		
		teleport = DataManager.TELEPORTER_DATA.getTeleporterTemplate(npc.getNpcId());

		switch(teleport.getType())
		{
			case FLIGHT:
				TeleportService.flightTeleport(teleport, locId, activePlayer);
			break;
			case REGULAR:
				TeleportService.regularTeleport(teleport, locId, activePlayer);
			break;
			default:
		}
	}
}
