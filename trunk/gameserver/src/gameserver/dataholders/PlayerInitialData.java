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