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

import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.PlayerInfo;
import java.nio.ByteBuffer;

public class SM_CREATE_CHARACTER extends PlayerInfo
{	

	public static final int	RESPONSE_OK	= 0x00;
	public static final int	FAILED_TO_CREATE_THE_CHARACTER = 1;
	public static final int RESPONSE_DB_ERROR = 2;
	public static final int RESPONSE_SERVER_LIMIT_EXCEEDED = 4;
	public static final int	RESPONSE_INVALID_NAME = 5;
	public static final int RESPONSE_FORBIDDEN_CHAR_NAME= 9;
	public static final int	RESPONSE_NAME_ALREADY_USED = 10;
	public static final int RESPONSE_NAME_RESERVED = 11;
	public static final int RESPONSE_OTHER_RACE = 12;
	private final int responseCode;
	private final PlayerAccountData	player;

	public SM_CREATE_CHARACTER(PlayerAccountData accPlData, int responseCode)
	{
		this.player = accPlData;
		this.responseCode = responseCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, responseCode);

		if(responseCode == RESPONSE_OK)
		{
			writePlayerInfo(buf, player);
		}
		else
		{
			writeB(buf, new byte[512]);
		}
	}
}