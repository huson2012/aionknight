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

package gameserver.model.account;

import java.sql.Timestamp;
import java.util.List;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.legion.Legion;
import gameserver.model.legion.LegionMember;

public class PlayerAccountData
{
	private PlayerCommonData playerCommonData;
	private PlayerAppearance appereance;
	private List<Item> equipment;
	private Timestamp creationDate;
	private Timestamp deletionDate;
	private LegionMember legionMember;
	
	public PlayerAccountData(PlayerCommonData playerCommonData, PlayerAppearance appereance, List<Item> equipment, 
	LegionMember legionMember)
	{
		this.playerCommonData = playerCommonData;
		this.appereance = appereance;
		this.equipment = equipment;
		this.legionMember = legionMember;
	}

	public Timestamp getCreationDate()
	{
		return creationDate;
	}

	public void setDeletionDate(Timestamp deletionDate)
	{
		this.deletionDate = deletionDate;
	}

	public Timestamp getDeletionDate()
	{
		return deletionDate;
	}

	public int getDeletionTimeInSeconds()
	{
		return deletionDate == null ? 0 : (int) (deletionDate.getTime() / 1000);
	}

	public PlayerCommonData getPlayerCommonData()
	{
		return playerCommonData;
	}

	public void setPlayerCommonData(PlayerCommonData playerCommonData)
	{
		this.playerCommonData = playerCommonData;
	}

	public PlayerAppearance getAppereance()
	{
		return appereance;
	}

	public void setCreationDate(Timestamp creationDate)
	{
		this.creationDate = creationDate;
	}

	public Legion getLegion()
	{
		return legionMember.getLegion();
	}

	public boolean isLegionMember()
	{
		return legionMember != null;
	}

	public List<Item> getEquipment()
	{
		return equipment;
	}

	public void setEquipment(List<Item> equipment)
	{
		this.equipment = equipment;
	}
}