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
					writeH(buf, item.daysLeft());
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
			writeB(buf, new byte[(6 - count)]);
			count = 0;
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeH(buf, ((SimpleModifier) modifier).getValue());
				}
			}
			writeB(buf, new byte[(6 - count) * 2]);
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