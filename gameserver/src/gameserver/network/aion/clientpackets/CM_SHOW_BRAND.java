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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.AllianceService;
import gameserver.services.GroupService;

public class CM_SHOW_BRAND extends AionClientPacket
{

	private int	modeId;
	private int	brandId;
	private int	targetObjectId;

	/**
	 * @param opcode
	 */
	public CM_SHOW_BRAND(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		modeId = readD();
		brandId = readD();
		targetObjectId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if (player == null)
			return;
		
		if (player.isInGroup())
			GroupService.getInstance().showBrand(player.getPlayerGroup(), modeId, brandId, targetObjectId);
		if (player.isInAlliance())
			AllianceService.getInstance().showBrand(player.getPlayerAlliance(), modeId, brandId, targetObjectId);
	}
}
