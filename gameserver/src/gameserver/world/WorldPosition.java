/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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
			+ x + ", y=" + y + ", z=" + z + "]";
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