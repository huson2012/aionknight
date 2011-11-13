/**
 * This file is part of Aion-Knight Emu.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight Emu. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * author^
 * Fr0st;
 * Mr.Chayka.
*/

package gameserver.controllers.movement;

import java.util.Random;


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

public class RoadObserver extends ActionObserver {
	private Player player;
	private Road road;
	private Point3D oldPosition;
	private Random random;

	public RoadObserver() {
		super(ObserverType.MOVE);
		this.player = null;
		this.road = null;
		this.oldPosition = null;
		this.random = null;
	}

	public RoadObserver(Road road, Player player) {
		super(ObserverType.MOVE);
		this.player = player;
		this.road = road;
		this.oldPosition = new Point3D(player.getX(), player.getY(), player.getZ());
		this.random = new Random();
	}

	@Override
	public void moved() {
		Point3D newPosition = new Point3D(player.getX(), player.getY(), player.getZ());
		boolean passedThrough = false;

		if (road.getPlane().intersect(oldPosition, newPosition)) {
			Point3D intersectionPoint = road.getPlane().intersection(oldPosition, newPosition);
			if (intersectionPoint != null) {
				double distance = Math.abs(road.getPlane().getCenter().distance(intersectionPoint));

				if (distance < road.getTemplate().getRadius()) {
					passedThrough = true;
				}
			} else {
			if (MathUtil.isIn3dRange(road, player, road.getTemplate().getRadius()));
				passedThrough = true;
			}
		}

		if (passedThrough) {
			if (player.getAccessLevel() >= AdminConfig.COMMAND_ROAD) {
				PacketSendUtility.sendMessage(player, "You went on the road " + road.getName() + ".");
			}

			RoadExit exit = road.getTemplate().getRoadExit();

			WorldType type = player.getWorldType();
			if (type == WorldType.ELYSEA) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), 0);
				}
			} else if (type == WorldType.ASMODAE) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), 0);
				}
			}
		}
		oldPosition = newPosition;
	}
}