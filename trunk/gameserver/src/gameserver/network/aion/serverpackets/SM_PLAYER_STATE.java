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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * 
 * So far I've found only one usage for this packet - to stop character blinking ( just after login into game, player's
 * character is blinking )
 *
 * States:
 *
 * 0 - normal char
 * 1- crounched invisible char
 * 64 - standing blinking char
 * 128- char is invisible
 */
public class SM_PLAYER_STATE extends AionServerPacket
{
	private int	playerObjId;
	private int	visualState;
	private int	seeState;

	public SM_PLAYER_STATE(Player player)
	{
		this.playerObjId = player.getObjectId();
		this.visualState = player.getVisualState();
		this.seeState = player.getSeeState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerObjId);
		writeC(buf, visualState);
		writeC(buf, seeState);
		if(visualState == 64)
			writeC(buf, 0x01);
		else
			writeC(buf, 0x00);
	}
}