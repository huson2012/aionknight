/*
 * This file is part of Aion-Knight Emu.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight Emu.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * author^
 * Fr0st;
 * Mr.Chayka.
*/
 
 package org.openaion.gameserver.services;
     
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.road.Road;
import org.openaion.gameserver.model.templates.road.RoadTemplate;
import org.apache.log4j.Logger;
     
public class RoadService {
	Logger log = Logger.getLogger(RoadService.class);
     
    private static class SingletonHolder {
		protected static final RoadService instance = new RoadService();
    }

	public static final RoadService getInstance()
       {
		return SingletonHolder.instance;
	}
     
	private RoadService()
       {
		for (RoadTemplate rt : DataManager.ROAD_DATA.getRoadTemplates()) {
			Road r = new Road(rt);
			r.spawn();
			log.debug("Added " + r.getName() + " at m=" + r.getWorldId() + ",x=" + r.getX() + ",y=" + r.getY() + ",z=" + r.getZ());
		}
	}
}