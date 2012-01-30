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

import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import gameserver.network.aion.serverpackets.SM_WAREHOUSE_UPDATE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.Collections;

public class CM_MOVE_ITEM extends AionClientPacket
{
	/**
	 * Target object id that client wants to TALK WITH or 0 if wants to unselect
	 */
	private int					targetObjectId;
	private int					source;
	private int					destination;
	private int					slot;
	private static final Logger	log	= Logger.getLogger(CM_MOVE_ITEM.class);
	/**
	 * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
	 * @param opcode
	 */
	public CM_MOVE_ITEM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();// empty
		source = readC();        //FROM (0 - player inventory, 1 - regular warehouse, 2 - account warehouse, 3 - legion warehouse)
		destination = readC();   //TO
		slot = readH();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if (player != null)
		{
			if(player.isTrading() && source != destination)
			{
				log.warn("[AUDIT] Trying to use trade exploit: " + player.getName());

				Item item = player.getStorage(source).getItemByObjId(targetObjectId);

				if(source == StorageType.CUBE.getId())
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(Collections.singletonList(item)));
				else
					PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_UPDATE(item, source));

				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,EmotionType.END_LOOT,0,0));

				return;
			}
			
			//prevent from using items endless  amount of times with packets.
			if (player.getController().hasTask(TaskId.ITEM_USE))
			{
				log.info("[AUDIT] "+player.getName()+" sending fake CM_MOVE_ITEM packet. Trying to dupe item.");
				return;
			}

			ItemService.moveItem(player, targetObjectId, source, destination, slot);
			
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,EmotionType.END_LOOT,0,0));
		}
	}
}