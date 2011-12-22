/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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
				PacketSendUtility.sendMessage(player, "You went on the road " + road.getName() + ".");
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