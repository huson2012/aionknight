/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */
package gameserver.dataholders;

import gameserver.model.items.WrapperItem;
import gameserver.model.templates.item.ItemRace;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "wrapped_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedItemData
{
	
	@XmlElement(required = true, name = "wrapper_item")
	protected List<WrapperItem> list;

	private TIntObjectHashMap<WrapperItem> wrappedItemData;

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		wrappedItemData = new TIntObjectHashMap<WrapperItem>();
		for(WrapperItem it : list)
		{
			wrappedItemData.put(it.getItemId(), it);
		}
		list = null;
	}

	public int size()
	{
		return wrappedItemData.size();
	}
	
	public WrapperItem getItemWrapper(int itemId)
	{
		return wrappedItemData.get(itemId);
	}
	
	public TIntIntHashMap rollItems(int wrapperItemId, int playerLevel, ItemRace race)
	{
		TIntIntHashMap itemCountMap = new TIntIntHashMap();
		
		final WrapperItem wrapperItem = wrappedItemData.get(wrapperItemId);
		if (wrapperItem == null)
			return itemCountMap;
		
		return wrapperItem.rollItems(playerLevel, race);
	}	
}