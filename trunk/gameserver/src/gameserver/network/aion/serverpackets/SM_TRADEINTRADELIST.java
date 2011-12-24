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
