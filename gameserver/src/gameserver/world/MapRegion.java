/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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