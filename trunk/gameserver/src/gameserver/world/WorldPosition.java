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

package gameserver.world;

import org.apache.log4j.Logger;

public class WorldPosition
{

	private static final Logger	log	= Logger.getLogger(WorldPosition.class);
	private int mapId;
	private MapRegion mapRegion;
	private float x;
	private float y;
	private float z;
	private byte heading;
	private boolean isSpawned = false;
	public int getMapId()
	{
		return mapId;
	}

	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}

	public MapRegion getMapRegion()
	{
		return isSpawned ? mapRegion : null;
	}

	public int getInstanceId()
	{
		return mapRegion.getParent().getInstanceId();
	}

	public int getInstanceCount()
	{
		return mapRegion.getParent().getParent().getInstanceCount();
	}

	public boolean isInstanceMap()
	{
		return mapRegion.getParent().getParent().isInstanceType();
	}
	public boolean isInEmpyreanMap()
	{
		return mapRegion.getMapId() == 300300000;
	}

	public byte getHeading()
	{
		return heading;
	}

	public World getWorld()
	{
		return mapRegion.getWorld();
	}

	public boolean isSpawned()
	{
		return isSpawned;
	}

	void setIsSpawned(boolean val)
	{
		isSpawned = val;
	}

	void setMapRegion(MapRegion r)
	{
		mapRegion = r;
	}

	void setXYZH(float newX, float newY, float newZ, byte newHeading)
	{
		x = newX;
		y = newY;
		z = newZ;
		heading = newHeading;
	}

	@Override
	public String toString()
	{
		return "WorldPosition [heading=" + heading + ", isSpawned=" + isSpawned + ", mapRegion=" + mapRegion + ", x="
			+ x + ", y=" + y + ", z=" + z + ']';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof WorldPosition))
			return false;

		WorldPosition pos = (WorldPosition) o;
		return this.x == pos.x && this.y == pos.y && this.z == pos.z && this.isSpawned == pos.isSpawned
			&& this.heading == pos.heading && this.mapRegion == pos.mapRegion;
	}

	@Override
	public WorldPosition clone()
	{
		WorldPosition pos = new WorldPosition();
		pos.heading = this.heading;
		pos.isSpawned = this.isSpawned;
		pos.mapRegion = this.mapRegion;
		pos.mapId = this.mapId;
		pos.x = this.x;
		pos.y = this.y;
		pos.z = this.z;
		return pos;
	}	
}
