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

import java.nio.ByteBuffer;
import org.apache.log4j.Logger;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.TradeListTemplate.TradeTab;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

public class SM_TRADEINTRADELIST extends AionServerPacket
{
	private int targetObjectId;
	private int npcTemplateId;
	private TradeListTemplate tlist;
	private int buyPriceModifier;
	private int finalPriceModifier;

	public SM_TRADEINTRADELIST(Npc npc, TradeListTemplate tlist, int buyPriceModifier)
	{
		if(tlist == null)
			return;
		this.targetObjectId = npc.getObjectId().intValue();
		this.npcTemplateId = npc.getNpcId();
		this.tlist = tlist;
		this.buyPriceModifier = buyPriceModifier;
		finalPriceModifier = Math.round((buyPriceModifier + tlist.getBuyRate() * buyPriceModifier) * tlist.getSellRate());
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if((this.tlist != null) && (this.tlist.getNpcId() != 0) && (this.tlist.getCount() != 0))
		{
			writeD(buf, targetObjectId);
			switch(tlist.getType())
			{
				case ABYSS:
					writeC(buf, 2);
					break;
				case COUPON:
					writeC(buf, 3);
					break;
				case EXTRA:
					writeC(buf, 4);
					break;
				default:
					writeC(buf, 1);
			}
			writeD(buf, finalPriceModifier);
			writeH(buf, tlist.getCount());

			for(TradeTab tradeTabl : tlist.getTradeTablist())
				writeD(buf, tradeTabl.getId());
		}
		else if(tlist == null)
		{
			Logger.getLogger(SM_TRADEINTRADELIST.class).warn("Empty TradeListTemplate for NpcId: " + this.npcTemplateId);
			writeD(buf, targetObjectId);
			writeC(buf, 1);
			writeD(buf, buyPriceModifier);
			writeH(buf, 0);
		}
	}
}