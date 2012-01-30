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

package gameserver.controllers.movement;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.road.Road;
import gameserver.model.templates.road.RoadExit;
import gameserver.model.utils3d.Point3D;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldType;
import java.util.Random;

public class RoadObserver extends ActionObserver 
{
	private Player player;
	private Road road;
	private Point3D oldPosition;
	private Random random;

	public RoadObserver() 
	{
		super(ObserverType.MOVE);
		this.player = null;
		this.road = null;
		this.oldPosition = null;
		this.random = null;
	}

	public RoadObserver(Road road, Player player) 
	{
		super(ObserverType.MOVE);
		this.player = player;
		this.road = road;
		this.oldPosition = new Point3D(player.getX(), player.getY(), player.getZ());
		this.random = new Random();
	}

	@Override
	public void moved() 
	{
		Point3D newPosition = new Point3D(player.getX(), player.getY(), player.getZ());
		boolean passedThrough = false;

		if (road.getPlane().intersect(oldPosition, newPosition)) 
		{
			Point3D intersectionPoint = road.getPlane().intersection(oldPosition, newPosition);
			if (intersectionPoint != null) 
			{
				double distance = Math.abs(road.getPlane().getCenter().distance(intersectionPoint));

				if (distance < road.getTemplate().getRadius()) 
				{
					passedThrough = true;
				}
			} 
			else 
			{
			if (MathUtil.isIn3dRange(road, player, road.getTemplate().getRadius()));
				passedThrough = true;
			}
		}

		if (passedThrough) 
		{
			if (player.getAccessLevel() >= AdminConfig.COMMAND_ROAD) 
			{
				PacketSendUtility.sendMessage(player, "You went on the road " + road.getName() + '.');
			}

			RoadExit exit = road.getTemplate().getRoadExit();

			WorldType type = player.getWorldType();
			if (type == WorldType.ELYSEA) 
			{
				if (player.getCommonData().getRace() == Race.ELYOS) 
				{
					TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), 0);
				}
			} else if (type == WorldType.ASMODAE) 
			{
				if (player.getCommonData().getRace() == Race.ASMODIANS) 
				{
					TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), 0);
				}
			}
		}
		oldPosition = newPosition;
	}
}