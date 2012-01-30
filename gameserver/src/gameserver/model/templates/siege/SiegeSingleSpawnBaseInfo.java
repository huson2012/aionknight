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

package gameserver.model.templates.siege;

import gameserver.model.siege.SiegeRace;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SingleSpawnBaseInfo")
public class SiegeSingleSpawnBaseInfo
{
	
	@XmlAttribute(name = "npcid_dr")
	protected int		npcid_Drakan;
	@XmlAttribute(name = "npcid_da")
	protected int		npcid_Asmodians;
	@XmlAttribute(name = "npcid_li")
	protected int 		npcid_Elyos;
	
	@XmlAttribute(name = "x")
	protected float x;
	@XmlAttribute(name = "y")
	protected float y;
	@XmlAttribute(name = "z")
	protected float z;
	@XmlAttribute(name = "h")
	protected int	h;
	@XmlAttribute(name = "static_id")
	protected int 	staticId;
	
	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}
	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}
	/**
	 * @return the z
	 */
	public float getZ()
	{
		return z;
	}
	/**
	 * @return the h
	 */
	public int getH()
	{
		return h;
	}
	
	public int getNpcId(SiegeRace race)
	{
		switch(race)
		{
			case ASMODIANS: return npcid_Asmodians;
			case BALAUR: return npcid_Drakan;
			case ELYOS: return npcid_Elyos;
			default: return 0;
		}
	}
	
	public int getStaticId()
	{
		return staticId;
	}
	
}
