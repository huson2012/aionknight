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

import gameserver.model.PlayerClass;
import gameserver.model.Race;
import gameserver.model.templates.item.ItemTemplate;
import gnu.trove.THashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name="player_initial_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerInitialData
{
	@XmlElement(name="player_data")
	private List<PlayerCreationData> dataList = new ArrayList<PlayerCreationData>();

	@XmlElement(name="elyos_spawn_location", required = true)
	private LocationData elyosSpawnLocation;
	
	@XmlElement(name="asmodian_spawn_location", required = true)
	private LocationData asmodianSpawnLocation;
	private THashMap<PlayerClass, PlayerCreationData> data = new THashMap<PlayerClass, PlayerCreationData>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (PlayerCreationData pt : dataList)
		{
			data.put(pt.getRequiredPlayerClass(), pt);
		}

		dataList.clear();
		dataList = null;
	}

	public PlayerCreationData getPlayerCreationData(PlayerClass cls)
	{
		return data.get(cls);
	}

	public int size()
	{
		return data.size();
	}

	public LocationData getSpawnLocation(Race race)
	{
		switch(race)
		{
			case ASMODIANS:
				return asmodianSpawnLocation;
			case ELYOS:     
				return elyosSpawnLocation;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static class PlayerCreationData
	{
		@XmlAttribute(name="class")
		private PlayerClass requiredPlayerClass;

		@XmlElement(name="items")
		private ItemsType itemsType;

		PlayerClass getRequiredPlayerClass()
		{
			return requiredPlayerClass;
		}

		public List<ItemType> getItems()
		{
			return Collections.unmodifiableList(itemsType.items);
		}

		static class ItemsType
		{
			@XmlElement(name = "item")
			public List<ItemType> items = new ArrayList<ItemType>();
		}

		public static class ItemType
		{
			@XmlAttribute(name = "id")
			@XmlIDREF
			public ItemTemplate template;

			@XmlAttribute(name="count")
			public int count;

			public ItemTemplate getTemplate()
			{
				return template;
			}

			public int getCount()
			{
				return count;
			}

			@Override
			public String toString()
			{
				final StringBuilder sb = new StringBuilder();
				sb.append("ItemType");
				sb.append("{template=").append(template);
				sb.append(", count=").append(count);
				sb.append('}');
				return sb.toString();
			}
		}

	}

	public static class LocationData
	{
		@XmlAttribute(name="map_id")
		private int mapId;
		
		@XmlAttribute(name="x")
		private float x;
		
		@XmlAttribute(name="y")
		private float y;
		
		@XmlAttribute(name="z")
		private float z;
		
		@XmlAttribute(name="heading")
		private byte heading;

		LocationData() {
			//
		}

		public int getMapId()
		{
			return mapId;
		}

		public float getX()
		{
			return x;
		}

		public float getY()
		{
			return y;
		}

		public float getZ()
		{
			return z;
		}

		public byte getHeading()
		{
			return heading;
		}
	}
}