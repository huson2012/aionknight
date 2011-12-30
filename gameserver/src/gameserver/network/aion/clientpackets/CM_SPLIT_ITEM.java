/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

public class CM_SPLIT_ITEM extends AionClientPacket
{

	int sourceItemObjId;
	int sourceStorageType;
	long itemAmount;
	int destinationItemObjId;
	int destinationStorageType;
	int slotNum; // destination slot.

	public CM_SPLIT_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		sourceItemObjId = readD();       // drag item unique ID. If merging and itemCount becoming null, this item must be deleted.
		itemAmount = readD();            // Items count to be moved.
		@SuppressWarnings("unused")
		byte[] zeros = readB(4);         // Nothing
		sourceStorageType = readC();     // Source storage
		destinationItemObjId = readD();  // Destination item unique ID if merging. Null if spliting.
		destinationStorageType = readC();// Destination storage
		slotNum = readH();               // Destination slot. Not needed right now, Items adding only to next available slot. Not needed at all when merge.
	}


	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		// Temp Fix To Avoid Legion Warehouse Dupe Exploit
		/**
		 * if(sourceStorageType == 3)
		 * return;
		 */
		
		if (player.isTrading())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INVENTORY_SPLIT_DURING_TRADE);
			return;
		}

		if(destinationItemObjId == 0)
			ItemService.splitItem(player, sourceItemObjId, itemAmount, slotNum, sourceStorageType, destinationStorageType);
		else
			ItemService.mergeItems(player, sourceItemObjId, itemAmount, destinationItemObjId, sourceStorageType, destinationStorageType);
	}
}