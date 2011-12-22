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

package gameserver.controllers;

import gameserver.controllers.movement.RoadObserver;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.road.Road;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

	public class RoadController extends CreatureController<Road> {
		FastMap<Player, RoadObserver> observed = new FastMap<Player, RoadObserver>().shared();

	@Override
	public void see(VisibleObject object) {
		super.see(object);

		if (!(object instanceof Player)) {
			return;
		}

		Player p = (Player)object;
		RoadObserver observer = new RoadObserver((Road)getOwner(), p);
		p.getObserveController().addObserver(observer);
		observed.put(p, observer);
		Logger.getLogger(RoadController.class).debug(getOwner().getName() + " sees " + p.getName());
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if ((isOutOfRange) && ((object instanceof Player))) {
			Player p = (Player)object;
			RoadObserver observer = observed.remove(p);
			observer.moved();
			p.getObserveController().removeObserver(observer);
			Logger.getLogger(RoadController.class).debug(getOwner().getName() + " not sees " + p.getName());
		}
	}
}