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

package gameserver.model.drop;

import commons.utils.Rnd;
import gameserver.dataholders.DataManager;
import gameserver.model.NpcType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class DropItem
{
	private int index = 0;
	private long count = 0;
	private DropTemplate dropTemplate;
	private List<Integer> questPlayerObjId = null;
	private boolean isFreeForAll = false;
	private long highestValue = 0;
	private Player winningPlayer = null;
	private boolean isItemWonNotCollected = false;
	private boolean	registeredSpecial = false;
	private List<Player> playerStatus = null;
	private boolean processed = false;
	private ScheduledFuture<?> specialDropTimeout = null;
	private boolean questDropForEachMemeber	= false;
	private int distributionType = 0;
	private double lootChance = 0;

	public DropItem(DropTemplate dropTemplate)
	{
		this.dropTemplate = dropTemplate;
	}

	public void calculateCount(float rate)
	{
		if(Rnd.get() * 100 < dropTemplate.getChance() * rate)
		{
			count = Rnd.get(dropTemplate.getMin(), dropTemplate.getMax());
		}
	}

	public void calculateCount(Player player, int npcId, float rate)
	{
		NpcDropStat stats = player.getNpcDropStats(npcId);
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(npcId);
		
		if(npcTemplate.getNpcType() == NpcType.CHEST)
			rate = player.getRates().getChestDropRate();
		
		double chance = stats.getItemLootChance(dropTemplate.getItemId()) * rate;
		if (chance == 0)
		{
			stats.setItemLootChance(dropTemplate.getItemId(), dropTemplate.getChance());
			chance = Math.min(dropTemplate.getChance() * rate, 100);
		}
		
		lootChance = chance;
		boolean looted = false;
		if (Rnd.get() * 100F < chance)
		{
			int min = dropTemplate.getMin();
			if (min != 0)
			{
				if (min >= dropTemplate.getMax())
					count = dropTemplate.getMax();
				else
					count = Rnd.get(min, dropTemplate.getMax());
			}
			else
				count = Rnd.get(1, dropTemplate.getMax());

			looted = count > 0;

		}
		if(npcTemplate.getNpcType() != NpcType.CHEST)
			stats.updateStat(dropTemplate.getItemId(), looted);
		else
			stats.updateStat(dropTemplate.getItemId(), true);
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public long getCount()
	{
		return count;
	}

	public double getLootChance()
	{
		return lootChance;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	public DropTemplate getDropTemplate()
	{
		return dropTemplate;
	}

	public boolean hasQuestPlayerObjId(int playerObjId)
	{
		if(questPlayerObjId == null || questPlayerObjId.size() == 0)
			return true;

		return questPlayerObjId.contains(playerObjId);
	}

	public void addQuestPlayerObjId(int playerObjId)
	{
		if(questPlayerObjId == null)
			questPlayerObjId = new ArrayList<Integer>();

		questPlayerObjId.add(playerObjId);
	}

	public void setFreeForAll(boolean isFreeForAll)
	{
		this.isFreeForAll = isFreeForAll;
	}

	public boolean isFreeForAll()
	{
		return isFreeForAll;
	}

	public long getHighestValue()
	{
		return highestValue;
	}

	public void setHighestValue(long highestValue)
	{
		this.highestValue  = highestValue;
	}

	public void setWinningPlayer(Player winningPlayer)
	{
		this.winningPlayer  = winningPlayer;
		
	}

	public Player getWinningPlayer()
	{
		return winningPlayer;
	}
	
	public void setItemWonNotCollected(boolean isItemWonNotCollected)
	{
		this.isItemWonNotCollected = isItemWonNotCollected;
	}

	public boolean isItemWonNotCollected()
	{
		return isItemWonNotCollected;
	}

	public void setRegisteredSpecial()
	{
		this.registeredSpecial = true;
	}

	public boolean isRegisteredSpecial()
	{
		return registeredSpecial;
	}

	public void addSpecialPlayer(Player player)
	{
		if(playerStatus == null)
		{
			playerStatus = new ArrayList<Player>();
		}
		playerStatus.add(player);
	}

	public void delSpecialPlayer(Player player)
	{
		if(playerStatus == null)
		{
			return;
		}
		playerStatus.remove(player);
	}

	public int getSpecialPlayerSize()
	{
		if(playerStatus == null)
		{
			return 0;
		}
		return playerStatus.size();
	}

	public boolean containsSpecialPlayer(Player player)
	{
		if(playerStatus == null)
		{
			return false;
		}
		return playerStatus.contains(player);
	}

	public boolean isProcessed()
	{
		return processed;
	}

	public void setProcessed()
	{
		this.processed = true;
	}

	public boolean isQuestDropForEachMemeber()
	{
		return questDropForEachMemeber;
	}

	public void setQuestDropForEachMemeber()
	{
		this.questDropForEachMemeber = true;
	}

	public void setDistributionType(int distributionType)
	{
		this.distributionType = distributionType;
	}

	public int getDistributionType()
	{
		return distributionType;
	}

	@Override 
	public boolean equals(Object other) 
	{
		if(other == null || !(other instanceof DropItem))
			return false;

		DropItem otherDrop = (DropItem) other;
		if(otherDrop.dropTemplate.getItemId() != this.dropTemplate.getItemId())
			return false;
		if(otherDrop.questPlayerObjId == this.questPlayerObjId)
			return true;
		if(otherDrop.questPlayerObjId.size() != this.questPlayerObjId.size())
			return false;

		for(int playerId : this.questPlayerObjId)
		{
			if(!otherDrop.questPlayerObjId.contains(playerId))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int playerIds = 0;
		if(this.questPlayerObjId != null)
		{
			for(int playerId : questPlayerObjId)
			{
				playerIds += playerId;
			}
		}
		int hash = 1000000007 * this.dropTemplate.getItemId();
		hash += 1000000009 * playerIds;
		return hash;
	}

	public void setSpecialDropTimeout(ScheduledFuture<?> specialDropTimeout)
	{
		this.specialDropTimeout = specialDropTimeout;
	}

	public void cancelTimeoutTask()
	{
		if (specialDropTimeout == null)
			return;

		specialDropTimeout.cancel(true);
		specialDropTimeout = null;
	}
}