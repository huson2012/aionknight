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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_DREDGION_INSTANCE extends AionServerPacket
{
	private byte	dredgiontype;
	private int		players;
	private int		instanceid;
	private int		allowed;
	private int		timer = 0;
	private boolean		close = false;

	public SM_DREDGION_INSTANCE(byte dredgiontype, int players, int allowed, int timer)
	{
		this.dredgiontype = dredgiontype;
		this.players = players;
		this.allowed = allowed;
		this.timer = timer;
	}

	public SM_DREDGION_INSTANCE(int instanceid)
	{
		this.players = 5;
		this.allowed = 0;
		this.timer = 0;
		this.close = true;

		if(instanceid == 300110000)
			this.dredgiontype = 1;
		else
			this.dredgiontype = 2;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		/**
		 * Use : on retail two packets are sent. One for chantra, one for regular dredgion. If player lvl is 55, then
		 * chantra dredgion is automaticaly selected, but both packets are still sent.
		 */
		if(dredgiontype == 1)
			instanceid = 300110000;
		else
			instanceid = 300210000;

		writeD(buf, dredgiontype); // 1 if regular dredgion, 2 if chantra
		writeC(buf, players); // players ?
		writeD(buf, instanceid); // dredgion or chantra dredgion

		if(timer < 1 && dredgiontype == 1 && !close){
			writeD(buf, 401193);
			writeD(buf, 401197);
		}else if(timer < 1 && dredgiontype == 2 && !close){
			writeD(buf, 401675);
			writeD(buf, 401677);
		}
		else{
			writeD(buf, 0);
			writeD(buf, 0);
		}
		writeD(buf, allowed); // 1 if player is allowed to join
		writeH(buf, timer); // 21248 = retail timer value (when registrating on dredgion, you get a max time)
		writeC(buf, 0);// unk

	}
}