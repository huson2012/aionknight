/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.BrokerItem;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.ItemStone;
import gameserver.model.items.ManaStone;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.util.Set;

public class SM_BROKER_ITEMS extends AionServerPacket
{
	private BrokerItem[] brokerItems;
	private Item buyItem;
	private int itemsCount;
	private int startPage;
	private int brokerFunction;
	private int id;
	private long totalKinah;
		
	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, int itemsCount, int startPage, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.itemsCount = itemsCount;
		this.startPage = startPage;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.brokerFunction = brokerFunction;
	}
	
	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, int id, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.id = id;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(int id, int brokerFunction)
	{
		this.id = id;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(int brokerFunction)
	{
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(Item buyItem, int brokerFunction)
	{
		this.buyItem = buyItem;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, long totalKinah, int id, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.totalKinah = totalKinah;
		this.id = id;
		this.brokerFunction = brokerFunction;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{	
		writeC(buf, brokerFunction);
		switch(brokerFunction)
		{
			case 0:
				writeD(buf, itemsCount);
				writeC(buf, 0);
				writeH(buf, startPage);
				writeH(buf, brokerItems.length);

				for(BrokerItem item : brokerItems)
				{
					if(item.getItem().getItemTemplate().isArmor() || item.getItem().getItemTemplate().isWeapon())
						writeArmorWeaponInfo(buf, item);
					else
						writeCommonInfo(buf, item);
				}
				break;
				
			case 1:
				writeD(buf, 0x00);
				writeH(buf, brokerItems.length);
			case 3:
				for(BrokerItem item : brokerItems)
				{
					if(brokerFunction == 3)
					{
						writeC(buf, 0x00);
						writeC(buf, id);
					}
					writeD(buf, item.getItem().getObjectId());
					writeD(buf, item.getItem().getItemTemplate().getTemplateId());
					writeQ(buf, item.getPrice());
					writeQ(buf, item.getItem().getItemCount());
					writeQ(buf, item.getItem().getItemCount());
					writeH(buf, brokerFunction == 3 ? 0x08 : item.daysLeft());
					writeC(buf, item.getItem().getEnchantLevel());
					writeD(buf, item.getItem().getItemSkinTemplate().getTemplateId());
					writeC(buf, item.getItem().hasOptionalSocket() ? item.getItem().getOptionalSocket() : 0);
					writeItemStones(buf, item.getItem());
					ItemStone god = item.getItem().getGodStone();
					writeD(buf, god == null ? 0 : god.getItemId());
					writeD(buf, 0x00);
					writeD(buf, 0x00);
                    writeD(buf, 0x00);
                    writeC(buf, 0x00);
					writeS(buf, item.getItem().getCrafterName());
				}
				break;
				
			case 2:
				writeC(buf, 0x00);
				writeD(buf, buyItem.getObjectId());
				writeQ(buf, buyItem.getItemCount());				
				break;
				
			case 4:
				writeC(buf, 0x00);
				writeD(buf, id);
				break;
				
			case 5:
				writeQ(buf, totalKinah);
				writeH(buf, (brokerItems == null || brokerItems.length < 0) ? 0x00 : brokerItems.length);
				writeD(buf, 0x00);
				writeC(buf, id);
				writeH(buf, (brokerItems == null || brokerItems.length < 0) ? 0x00 : brokerItems.length);
				if(brokerItems != null && brokerItems.length > 0)
				{
					for(BrokerItem item : brokerItems)
					{
						writeD(buf, item.getItemId());
						writeQ(buf, item.getPrice());
						writeQ(buf, item.getItemCount());
						writeQ(buf, item.getItemCount());
						writeD(buf, item.getItemUniqueId());
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeD(buf, item.getItemId());
						writeC(buf, 0x00);
						writeB(buf, new byte[24]);				
						writeD(buf, 0x00);
						writeD(buf, 0);
						writeD(buf, 0);
                        writeD(buf, 0);
						writeC(buf, 0x00);
						writeS(buf, "");
					}
				}
				break;
			case 6:
				writeC(buf, 0x00);
				break;
				
			default:
				return;
		}
	}
	
	private void writeArmorWeaponInfo(ByteBuffer buf, BrokerItem item)
	{
		writeD(buf, item.getItem().getObjectId());
		writeD(buf, item.getItem().getItemTemplate().getTemplateId());
		writeQ(buf, item.getPrice());
		writeQ(buf, item.getItem().getItemCount());
		writeC(buf, 0);
		writeC(buf, item.getItem().getEnchantLevel());
		writeD(buf, item.getItem().getItemSkinTemplate().getTemplateId());
		writeC(buf, item.getItem().hasOptionalSocket() ? item.getItem().getOptionalSocket() : 0);
		writeItemStones(buf, item.getItem());
		ItemStone god = item.getItem().getGodStone();
		writeD(buf, god == null ? 0 : god.getItemId());
		writeC(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
        writeD(buf, 0);
		writeS(buf, item.getSeller());
		writeS(buf, item.getItem().getCrafterName());
	}	
	private void writeItemStones(ByteBuffer buf, Item item)
	{
		int count = 0;
		
		if(item.hasManaStones())
		{
			Set<ManaStone> itemStones = item.getItemStones();
			
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeC(buf, modifier.getStat().getItemStoneMask());
				}
			}
			writeB(buf, new byte[(6-count)]);
			count = 0;
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeH(buf, ((SimpleModifier)modifier).getValue());
				}
			}
			writeB(buf, new byte[(6-count)*2]);
		}
		else
		{
			writeB(buf, new byte[24]);
		}
	}	
	private void writeCommonInfo(ByteBuffer buf, BrokerItem item)
	{
		writeD(buf, item.getItem().getObjectId());
		writeD(buf, item.getItem().getItemTemplate().getTemplateId());
		writeQ(buf, item.getPrice());
		writeQ(buf, item.getItem().getItemCount());
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeH(buf, 0);
        writeH(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
		writeS(buf, item.getSeller());
		writeS(buf, item.getItem().getCrafterName());
	}
}