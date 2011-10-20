/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;

import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.teleport.TelelocationTemplate;
import ru.aionknight.gameserver.model.templates.teleport.TeleporterTemplate;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.services.TeleportService;
import ru.aionknight.gameserver.utils.MathUtil;
import ru.aionknight.gameserver.world.World;


/**
 * @author ATracer, orz
 *
 */
public class CM_TELEPORT_SELECT extends AionClientPacket
{
	/** NPC ID */
	public  int					targetObjectId;

	/** Destination of teleport */
	public  int					locId;

	public  TelelocationTemplate _tele;

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
				//TODO
		}
	}
}
