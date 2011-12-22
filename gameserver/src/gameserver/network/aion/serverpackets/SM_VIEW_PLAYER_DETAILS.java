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
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_VIEW_PLAYER_DETAILS extends InventoryPacket 
{
    private List<Item> items;
    private int size;
    private int targetObjId;

    public SM_VIEW_PLAYER_DETAILS(int targetObjId, List<Item> items) 
	{
        this.items = items;
        this.size = items.size();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        writeD(buf, targetObjId); // Неизвестно
        writeC(buf, 11); // Неизвестно
        writeC(buf, size); // itemCount
        writeC(buf, 0);
        writeD(buf, 0);
        
        for (Item item : items) 
		{
            if((item.getEquipmentSlot()&1) == 0)
                writeD(buf, item.getObjectId());
            
            ItemTemplate itemTemplate = item.getItemTemplate();
            writeD(buf, itemTemplate.getTemplateId());
            writeH(buf, 0x24);
            writeD(buf, itemTemplate.getNameId());
            writeH(buf, 0);
            
            if (item.getItemTemplate().isArmor())
                writeArmorInfo(buf, item);
            else if (item.getItemTemplate().isWeapon())
                writeWeaponInfo(buf, item);
            else
                writeGeneralItemInfo(buf, item);
        }
    }
}