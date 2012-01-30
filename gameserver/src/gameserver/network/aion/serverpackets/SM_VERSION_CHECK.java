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

import gameserver.configs.main.CustomConfig;
import gameserver.configs.main.GSConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.model.siege.Influence;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.ChatService;
import java.nio.ByteBuffer;

public class SM_VERSION_CHECK extends AionServerPacket
{
	/**
	 * @param chatService
	 */
	public SM_VERSION_CHECK()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, 0x00);
		writeC(buf, NetworkConfig.GAMESERVER_ID);
		writeD(buf, 0x000188AD);// unk
		writeD(buf, 0x000188A6);// unk
		writeD(buf, 0x00000000);// unk
		writeD(buf, 0x00018898);// unk
		writeD(buf, 0x4C346D9D);// unk
		writeC(buf, 0x00);// unk
		writeC(buf, GSConfig.SERVER_COUNTRY_CODE);// country code;
		writeC(buf, 0x00);// unk
		if (GSConfig.FACTIONS_RATIO_LIMITED)
		{
			Influence inf = Influence.getInstance();

			int elyosRatio = Math.round(inf.getElyos() * 100);
			int asmosRatio = Math.round(inf.getAsmos() * 100);

			if (elyosRatio > GSConfig.FACTIONS_RATIO_VALUE)
			{
				writeC(buf, GSConfig.SERVER_MODE | 0x04); // limit elyos creation
			} 
			else if (asmosRatio > GSConfig.FACTIONS_RATIO_VALUE)
			{
				writeC(buf, GSConfig.SERVER_MODE | 0x08); // limit asmos creation
			}
			else
			{
				writeC(buf, GSConfig.SERVER_MODE);
			}
		}
		else
		{
			writeC(buf, GSConfig.SERVER_MODE); // Server mode : 0x80 = one race / 0x01 = free race / 0x22 = Character
		}
		writeD(buf, (int) (System.currentTimeMillis() / 1000));
		writeH(buf, 0x015E);
		writeH(buf, 0x0A01);
		writeH(buf, 0x0A01);
		writeH(buf, 0x370A);
        writeC(buf, 0x02);
		writeC(buf, 0x00);
        writeC(buf, 0x14);

		if(CustomConfig.ENABLE_DECOR_CHRISTMAS)
		{
		    writeC(buf, 0x01);
		}
        else
        {
            writeC(buf, 0x00);
        }

		writeH(buf, 0x00);
		writeH(buf, 0x00);
		writeC(buf, 0x01);
        writeH(buf, 0x00);
		writeB(buf, ChatService.getIp());
		writeH(buf, ChatService.getPort());
	}
}