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