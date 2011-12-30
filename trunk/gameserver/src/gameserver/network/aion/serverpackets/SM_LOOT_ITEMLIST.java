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

import gameserver.model.drop.DropItem;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class SM_LOOT_ITEMLIST extends AionServerPacket
{
	private int	targetObjectId;
	private DropItem[] dropItems;
	private int size;

	public SM_LOOT_ITEMLIST(int targetObjectId, Set<DropItem> dropItems, Player player)
	{
		this.targetObjectId = targetObjectId;
		Set<DropItem> tmp = new HashSet<DropItem>();
		for (DropItem item : dropItems)
		{
			if(item.hasQuestPlayerObjId(player.getObjectId()))
				tmp.add(item);
		}
		this.dropItems = tmp.toArray(new DropItem[tmp.size()]);
		size = this.dropItems.length;
	}

	/**
	 * {@inheritDoc} dc
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
		writeD(buf, targetObjectId);
		writeC(buf, size);

		for(DropItem dropItem : dropItems)
		{
			writeC(buf, dropItem.getIndex()); // index in droplist
			writeD(buf, dropItem.getDropTemplate().getItemId());
			writeH(buf, (int) dropItem.getCount());
			writeD(buf, 0);
		}
	}
}
