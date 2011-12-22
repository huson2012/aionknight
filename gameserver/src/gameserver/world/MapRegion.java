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

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.ObjectContainer;
import java.util.ArrayList;
import java.util.List;

public class MapRegion extends ObjectContainer
{

	private Integer	regionId;
	private WorldMapInstance parent;
	private ArrayList<MapRegion> neighbours	= new ArrayList<MapRegion>(9);
	private boolean regionActive = false;

	MapRegion(Integer id, WorldMapInstance parent)
	{
		this.regionId = id;
		this.parent = parent;
		this.neighbours.add(this);
	}

	public Integer getMapId()
	{
		return getParent().getMapId();
	}

	public World getWorld()
	{
		return getParent().getWorld();
	}

	public Integer getRegionId()
	{
		return regionId;
	}

	public WorldMapInstance getParent()
	{
		return parent;
	}

	public List<MapRegion> getNeighbours()
	{
		return neighbours;
	}

	void addNeighbourRegion(MapRegion neighbour)
	{
		neighbours.add(neighbour);
	}

	@Override
	public void storeObject (AionObject object)
	{
		super.storeObject(object);
		
		if (!regionActive && object instanceof Player)
			regionActive = true;
	}

	@Override
	public void removeObject(AionObject object)
	{
		super.removeObject(object);
		
		if (getPlayersCount() == 0)
			regionActive = false;
	}
	
	public boolean isMapRegionActive()
	{
		for (MapRegion r : neighbours)
		{
			if (r.regionActive)
				return true;
		}
		return false;
	}
}