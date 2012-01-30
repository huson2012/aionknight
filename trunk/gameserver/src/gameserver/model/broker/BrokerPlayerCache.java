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

package gameserver.model.broker;

import gameserver.model.gameobjects.BrokerItem;
import java.util.ArrayList;
import java.util.List;

public class BrokerPlayerCache
{
	private BrokerItem[] brokerListCache = new BrokerItem[0];
	private int brokerMaskCache;
	private int brokerSoftTypeCache;
	private int brokerStartPageCache;
	private List<Integer> searchItemsId = new ArrayList<Integer>();

	public BrokerItem[] getBrokerListCache()
	{
		return brokerListCache;
	}

	public void setBrokerListCache(BrokerItem[] brokerListCache)
	{
		this.brokerListCache = brokerListCache;
	}

	public int getBrokerMaskCache()
	{
		return brokerMaskCache;
	}

	public void setBrokerMaskCache(int brokerMaskCache)
	{
		this.brokerMaskCache = brokerMaskCache;
	}

	public int getBrokerSortTypeCache()
	{
		return brokerSoftTypeCache;
	}

	public void setBrokerSortTypeCache(int brokerSoftTypeCache)
	{
		this.brokerSoftTypeCache = brokerSoftTypeCache;
	}

	public int getBrokerStartPageCache()
	{
		return brokerStartPageCache;
	}

	public void setBrokerStartPageCache(int brokerStartPageCache)
	{
		this.brokerStartPageCache = brokerStartPageCache;
	}

	public void setSearchItemsId(List<Integer> searchItemsId)
	{
		this.searchItemsId = searchItemsId;
	}

	public List<Integer> getSearchItemsId()
	{
		if(this.searchItemsId.isEmpty())
			return null;
		return this.searchItemsId;
	}
}
