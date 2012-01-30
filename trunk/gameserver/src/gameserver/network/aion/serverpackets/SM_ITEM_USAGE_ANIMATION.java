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
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.Artifact;
import java.nio.ByteBuffer;

public class SM_ITEM_USAGE_ANIMATION extends AionServerPacket 
{
    private int playerObjId;
    private int targetObjId;
    private int itemObjId;
    private int itemId;
    private int time;
    private int end;
    private int unk;

    public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId) 
	{
        this.playerObjId = playerObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = 0;
        this.end = 1;
        this.unk = 1;
    }

    public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId, int time, int end, int unk) 
	{
        this.playerObjId = playerObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = time;
        this.end = end;
        this.unk = unk;
    }

    public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId, int time, int end) 
	{
        this.playerObjId = playerObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = time;
        this.end = end;
    }
    
    public SM_ITEM_USAGE_ANIMATION(Artifact artifact, Player player, Item stone, int end) 
	{
		this.playerObjId = player.getObjectId();
		this.targetObjId = artifact.getObjectId();
		this.itemObjId = stone.getObjectId();
		this.itemId = stone.getItemId();
		this.time = (end == 0) ? 5000 : 0;
		this.end = end;
		this.unk = 1;
	}

    public SM_ITEM_USAGE_ANIMATION(int playerObjId,int targetObjId, int itemObjId, int itemId, int time, int end, int unk) 
	{
        this.playerObjId = playerObjId;
        this.targetObjId = targetObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = time;
        this.end = end;
        this.unk = unk;
    }
    
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf) {
		writeD(buf, playerObjId); // player obj id
		writeD(buf, targetObjId); // target obj id

		writeD(buf, itemObjId); // itemObjId
		writeD(buf, itemId); 	// item id

		writeD(buf, time); 	// time of casting bar of an item
		writeC(buf, end); 	// 1-casting bar hitted end, 3- interrupted by moving
		writeC(buf, 0); 	// always 1
		writeC(buf, 1);
		writeD(buf, unk);
	}
}