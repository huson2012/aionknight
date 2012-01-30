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

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.world.World;
import java.nio.ByteBuffer;

public class SM_DIALOG_WINDOW extends AionServerPacket
{
	private int	targetObjectId;
	private int dialogID;
	private int	questId = 0;
	
	public SM_DIALOG_WINDOW(int targetObjectId, int dlgID)
	{
		this.targetObjectId = targetObjectId;
		this.dialogID = dlgID;
	}

	public SM_DIALOG_WINDOW(int targetObjectId , int dlgID , int questId)
	{
		this.targetObjectId = targetObjectId;
		this.dialogID = dlgID;
		this.questId = questId;
	}
	/**
	* {@inheritDoc}
	*/
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, targetObjectId);
		writeH(buf, dialogID);
		writeD(buf, questId);
		writeH(buf, 0);
		if(this.dialogID == 18)
		{
			AionObject object = World.getInstance().findAionObject(targetObjectId);
			if(object != null && object instanceof Npc)
			{
				Npc znpc = (Npc)object;
				if(znpc.getNpcId() == 798044 || znpc.getNpcId() == 798101)
				writeH(buf, 2);
			}
		}
	}
}