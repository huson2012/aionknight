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

package org.openaion.gameserver.controllers.movement;

import java.util.Random;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.model.road.Road;
import org.openaion.gameserver.model.templates.road.RoadExit;
import org.openaion.gameserver.model.templates.road.RoadTemplate;
import org.openaion.gameserver.model.utils3d.Plane3D;
import org.openaion.gameserver.model.utils3d.Point3D;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.WorldType;

// Referenced classes of package org.openaion.gameserver.controllers.movement:
//            ActionObserver

public class RoadObserver extends ActionObserver
{

    public RoadObserver()
    {
        super(ActionObserver.ObserverType.MOVE);
        player = null;
        road = null;
        oldPosition = null;
        random = null;
    }

    public RoadObserver(Road road1, Player player1)
    {
        super(ActionObserver.ObserverType.MOVE);
        player = player1;
        road = road1;
        oldPosition = new Point3D(player1.getX(), player1.getY(), player1.getZ());
        random = new Random();
    }

    public void moved()
    {
        Point3D point3d = new Point3D(player.getX(), player.getY(), player.getZ());
        boolean flag = false;
        if(road.getPlane().intersect(oldPosition, point3d))
        {
            Point3D point3d1 = road.getPlane().intersection(oldPosition, point3d);
            if(point3d1 != null)
            {
                double d = Math.abs(road.getPlane().getCenter().distance(point3d1));
                if(d < (double)road.getTemplate().getRadius())
                    flag = true;
            } else
            {
                if(!MathUtil.isIn3dRange(road, player, road.getTemplate().getRadius()));
                flag = true;
            }
        }
        if(flag)
        {
            if(player.getAccessLevel() >= AdminConfig.COMMAND_ROAD)
                PacketSendUtility.sendMessage(player, (new StringBuilder()).append("You passed through ").append(road.getName()).append(" Road .").toString());
            RoadExit roadexit = road.getTemplate().getRoadExit();
            WorldType worldtype = player.getWorldType();
            if(worldtype == WorldType.ELYSEA)
            {
                if(player.getCommonData().getRace() == Race.ELYOS)
                    TeleportService.teleportTo(player, roadexit.getMap(), roadexit.getX(), roadexit.getY(), roadexit.getZ(), 0);
            } else
            if(worldtype == WorldType.ASMODAE && player.getCommonData().getRace() == Race.ASMODIANS)
                TeleportService.teleportTo(player, roadexit.getMap(), roadexit.getX(), roadexit.getY(), roadexit.getZ(), 0);
        }
        oldPosition = point3d;
    }

    private Player player;
    private Road road;
    private Point3D oldPosition;
    private Random random;
}