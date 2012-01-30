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

package gameserver.geo;

import gameserver.configs.main.GSConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.WorldMapTemplate;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import aionjHungary.geoEngine.GeoWorldLoader;
import aionjHungary.geoEngine.models.GeoMap;
import aionjHungary.geoEngine.scene.Spatial;

public class GeoEngine
{
	private static Logger log = Logger.getLogger(GeoEngine.class);

	private Map<Integer, GeoMap> geoMaps = new HashMap<Integer, GeoMap>();
	
	public static final GeoEngine getInstance()
	{
		return SingletonHolder.instance;
	}

	GeoEngine()
	{
		if(GSConfig.GEODATA_ENABLED)
		{
			Map<String, Spatial> models = GeoWorldLoader.loadMeshs();
			for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA)
			{
				GeoMap geoMap = new GeoMap(Integer.toString(map.getMapId()), map.getWorldSize());
				if (GeoWorldLoader.loadWorld(map.getMapId(), models, geoMap))
				{
					geoMaps.put(map.getMapId(), geoMap);
				}
			}
			models.clear();
			models = null;
			log.info("Geodata engine: "+geoMaps.size()+" geoMaps loaded!");
		}
		else
		{
			log.info("Geodata engine disabled.");
		}
	}

	public float getZ(int worldId, float x, float y, float z)
	{
		if(GSConfig.GEODATA_ENABLED && geoMaps.containsKey(worldId))
			return geoMaps.get(worldId).getZ(x, y, z);
		else
			return z;
	}

	public boolean canSee(VisibleObject object, VisibleObject target)
	{
		return this.canSee(object.getWorldId(), object.getX(), object.getY(), object.getZ(), target.getX(), target.getY(), target.getZ());
	}

	public boolean canSee(int worldId, float x, float y, float z, float targetX, float targetY, float targetZ)
	{
		if(GSConfig.GEODATA_ENABLED && geoMaps.containsKey(worldId))
			return geoMaps.get(worldId).canSee(x, y, z, targetX, targetY, targetZ);
		else
			return true;
	}

	public GeoMap getGeoMapByWorldId(int worldId)
	{
		return geoMaps.get(worldId);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GeoEngine	instance	= new GeoEngine();
	}
}