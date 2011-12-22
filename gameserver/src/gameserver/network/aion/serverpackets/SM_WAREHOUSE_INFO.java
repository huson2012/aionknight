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
import java.util.Collections;
import java.util.List;

public class SM_WAREHOUSE_INFO extends InventoryPacket 
{
    private int warehouseType;
    private List<Item> itemList;
    private boolean firstPacket;
    private int expandLvl;

    public SM_WAREHOUSE_INFO(List<Item> items, int warehouseType, int expandLvl, boolean firstPacket) 
	{
        this.warehouseType = warehouseType;
        this.expandLvl = expandLvl;
        this.firstPacket = firstPacket;
        if (items == null)
            this.itemList = Collections.emptyList();
        else
            this.itemList = items;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        writeC(buf, warehouseType);
        writeC(buf, firstPacket ? 1 : 0);
        writeC(buf, expandLvl); //warehouse expand (0 - 9)
        writeH(buf, 0);
        writeH(buf, itemList.size());
        for (Item item : itemList) {
            writeGeneralInfo(buf, item);

            ItemTemplate itemTemplate = item.getItemTemplate();

            if (itemTemplate.getTemplateId() == ItemId.KINAH.value())
                writeKinah(buf, item);

            else if (itemTemplate.isWeapon()){
                writeWeaponInfo(buf, item);
                writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
            }
            else if (itemTemplate.isArmor()){
                writeArmorInfo(buf, item);
                writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
            }
            else{
                writeGeneralItemInfo(buf, item);
                writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
            }
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