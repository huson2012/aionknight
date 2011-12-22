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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.items.ItemId;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;

public class SM_WAREHOUSE_UPDATE extends InventoryPacket 
{
    private int warehouseType;
    private Item item;


    public SM_WAREHOUSE_UPDATE(Item item, int warehouseType) 
	{
        this.warehouseType = warehouseType;
        this.item = item;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        writeC(buf, warehouseType);
        writeH(buf, 13);
        writeH(buf, 1);

        writeGeneralInfo(buf, item);

        ItemTemplate itemTemplate = item.getItemTemplate();

        if (itemTemplate.getTemplateId() == ItemId.KINAH.value()) 
		{
            writeKinah(buf, item);
        } else if (itemTemplate.isWeapon()) 
		{
            writeWeaponInfo(buf, item);
            writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
        } else if (itemTemplate.isArmor()) 
		{
            writeArmorInfo(buf, item);
            writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
        } else {
            writeGeneralItemInfo(buf, item);
        	writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
        }

    }

    @Override
    protected void writeGeneralInfo(ByteBuffer buf, Item item) 
	{
        writeD(buf, item.getObjectId());
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeD(buf, itemTemplate.getTemplateId());
        writeC(buf, 0); //some item info (4 - weapon, 7 - armor, 8 - rings, 17 - bottles)
        writeH(buf, 0x24);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }
}