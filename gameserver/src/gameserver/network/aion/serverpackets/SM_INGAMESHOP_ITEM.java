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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.CashShopManager.ShopItem;
import java.nio.ByteBuffer;

public class SM_INGAMESHOP_ITEM extends AionServerPacket
{
	private final ShopItem item;
	
	public SM_INGAMESHOP_ITEM(ShopItem item)
	{
		this.item = item;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, item.id);
		writeQ(buf, item.price);
		writeH(buf, 0); //unk
		writeD(buf, item.itemId);
		writeD(buf, item.count);
		writeD(buf, 0);
		writeD(buf, 0);//if greater than 0, shows a stack of boxes
		writeD(buf, 0);//if greater than 0, shows a stack of boxes
		writeC(buf, item.eyecatch);//0- regular, 1-new, 2-hot
		writeD(buf, 0);
		writeD(buf, 0);
		writeH(buf, 0);
		writeS(buf, item.name);
		writeS(buf, item.desc);
	}
}