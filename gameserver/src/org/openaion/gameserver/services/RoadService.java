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

package org.openaion.gameserver.services;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.RoadData;
import org.openaion.gameserver.model.road.Road;
import org.openaion.gameserver.model.templates.road.RoadTemplate;

public class RoadService
{
    private static class SingletonHolder
    {

        protected static final RoadService instance = new RoadService();


        private SingletonHolder()
        {
        }
    }


    public static final RoadService getInstance()
    {
        return SingletonHolder.instance;
    }

    private RoadService()
    {
        log = Logger.getLogger(RoadService.class);
        Road road;
        for(Iterator iterator = DataManager.ROAD_DATA.getRoadTemplates().iterator(); iterator.hasNext(); log.debug((new StringBuilder()).append("Added ").append(road.getName()).append(" at m=").append(road.getWorldId()).append(",x=").append(road.getX()).append(",y=").append(road.getY()).append(",z=").append(road.getZ()).toString()))
        {
            RoadTemplate roadtemplate = (RoadTemplate)iterator.next();
            road = new Road(roadtemplate);
            road.spawn();
        }

        log.info("RoadService: Loaded!");
    }


    Logger log;
}