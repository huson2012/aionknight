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
package gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;


import gameserver.model.NpcType;
import gameserver.model.Race;
import gameserver.model.items.NpcEquippedGear;
import gameserver.model.templates.stats.KiskStatsTemplate;
import gameserver.model.templates.stats.NpcRank;
import gameserver.model.templates.stats.NpcStatsTemplate;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Felas
 * 
 */
public class LocationTemplate
{
	private int mapId;
	private float x = 0;
	private float y = 0;
	private float z = 0;

	public void setLocation(int mapID, float X, float Y, float Z)
	{
		this.mapId = mapID;
		this.x = X;
		this.y = Y;
		this.z = Z;
	}
	
	public int getMapId()
	{
		return this.mapId;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public float getZ()
	{
		return this.z;
	}
}
