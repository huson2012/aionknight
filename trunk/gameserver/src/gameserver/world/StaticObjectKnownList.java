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

package gameserver.world;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import java.util.ArrayList;
import java.util.List;

public class StaticObjectKnownList extends NpcKnownList
{

	public StaticObjectKnownList(VisibleObject owner)
	{
		super(owner);
	}

	@Override
	protected void findVisibleObjects()
	{
		if(owner == null || !owner.isSpawned())
			return;
		
		final List<VisibleObject> objectsToAdd = new ArrayList<VisibleObject>();
		
		for (MapRegion r : owner.getActiveRegion().getNeighbours())
		{
			r.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player newObject)
				{
					if(newObject == owner || newObject == null)
						return true;
					
					if(!checkObjectInRange(owner, newObject))
						return true;
					
					objectsToAdd.add(newObject);

					return true;
				}
			}, true);
		}
		
		for (VisibleObject object : objectsToAdd)
		{
			owner.getKnownList().storeObject(object);
			object.getKnownList().storeObject(owner);
		}
		
		objectsToAdd.clear();
	}
}