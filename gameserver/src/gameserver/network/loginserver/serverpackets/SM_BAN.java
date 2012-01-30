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

package gameserver.network.loginserver.serverpackets;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;

/**
 * The universal packet for account/IP bans
 */
public class SM_BAN extends LsServerPacket
{
	/**
	 * Ban type
	 * 1 = account
	 * 2 = IP
	 * 3 = Full ban (account and IP)
	 */
	private	final	byte		type;
	
	/**
	 * Account to ban
	 */
	private	final	int			accountId;

	/**
	 * IP or mask to ban
	 */
	private	final	String		ip;

	/**
	 * Time in minutes. 0 = infinity;
	 * If time < 0 then it's unban command
	 */
	private	final	int			time;
	
	/**
	 * Object ID of Admin, who request the ban
	 */
	private	final	int			adminObjId;

	public SM_BAN(byte type, int accountId, String ip, int time, int adminObjId)
	{
		super(0x06);

		this.type = type;
		this.accountId = accountId;
		this.ip = ip;
		this.time = time;
		this.adminObjId = adminObjId;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		
		writeC(buf, type);
		writeD(buf, accountId);
		writeS(buf, ip);
		writeD(buf, time);
		writeD(buf, adminObjId);
	}
}
