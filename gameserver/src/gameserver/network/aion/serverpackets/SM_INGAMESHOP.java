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
import gameserver.services.CashShopManager.ShopCategory;
import java.nio.ByteBuffer;

public class SM_INGAMESHOP extends AionServerPacket
{
	private final int type;
	private final ShopCategory[] categories;

	public SM_INGAMESHOP(int type, ShopCategory[] categories)
	{
		this.type = type;
		this.categories = categories;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, type);
		switch(type)
		{
			// TODO : Fix tabs values to something more dynamic
			
			// Top Tabs
			case 0:
				writeH(buf, 1);//Count
				writeD(buf, 25);//Tab Id
				writeS(buf, "Something");//name
				break;
			// Categories
			case 1:
				writeH(buf, categories.length);//Count
				for(ShopCategory category : categories)
				{
					writeD(buf, category.id);//id tab
					writeS(buf, category.name);//name
				}
				break;
		}
	}
}
