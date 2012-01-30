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
