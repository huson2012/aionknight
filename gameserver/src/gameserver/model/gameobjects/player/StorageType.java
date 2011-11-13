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

package gameserver.model.gameobjects.player;

public enum StorageType
{
	CUBE(0, 109),
	REGULAR_WAREHOUSE(1, 104),
	ACCOUNT_WAREHOUSE(2, 17),
	LEGION_WAREHOUSE(3, 25),
	PET_BAG_6(32, 6),
	PET_BAG_12(33, 12),
	PET_BAG_18(34, 18),
	PET_BAG_24(35, 24),
	BROKER(126),
	MAILBOX(127);

	private int id;
	private int limit;
	
	private StorageType(int id)
	{
		this.id = id;
	}
	
	private StorageType(int id, int limit)
	{
		this.id = id;
		this.limit = limit;
	}

	public int getId()
	{
		return id;
	}
	
	public int getLimit()
	{
		return limit;
	}
	
	public static StorageType getStorageTypeById(int id)
	{
		for(StorageType st : values())
		{
			if(st.id == id)
				return st;
		}
		return null;
	}
}