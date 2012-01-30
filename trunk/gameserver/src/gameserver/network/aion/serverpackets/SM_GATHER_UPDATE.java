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

import gameserver.model.templates.GatherableTemplate;
import gameserver.model.templates.gather.Material;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_GATHER_UPDATE extends AionServerPacket
{
	private GatherableTemplate template;
	private int action;
	private int itemId;
	private int success;
	private int failure;
	private int nameId;

	public SM_GATHER_UPDATE(GatherableTemplate template, Material material, int success, int failure, int action)
	{
		this.action = action;
		this.template = template;
		this.itemId = material.getItemid();
		this.success = success;
		this.failure = failure;
		this.nameId = material.getNameid();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, template.getSkillLevel());
		writeC(buf, action);
		writeD(buf, itemId);

		switch(action)
		{
			case 0:
			{
				writeD(buf, 100); //success value
				writeD(buf, 100); //failure value
				writeD(buf, 0);
				writeD(buf, 1200);	//delay after which bar will start moving (ms)
				writeD(buf, 1330011); //start gathering system message
				writeH(buf, 0x24);
				writeD(buf, nameId); //item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			}
			case  1:
			{
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 700);	//time of moving execution (ms)
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 2:
			{
				writeD(buf, 100);
				writeD(buf, failure);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 5: // you have stopped gathering
			{
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 1330080);
				writeH(buf, 0);
				break;
			}
			case 6:
			{
				writeD(buf, 100);
				writeD(buf, failure);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 7:
			{
				writeD(buf, success);
				writeD(buf, 100);
				writeD(buf, 0);
				writeD(buf, 1200);
				writeD(buf, 1330079);
				writeH(buf, 0x24);
				writeD(buf, nameId);
				writeH(buf, 0);
				break;
			}
		}
	}

}
