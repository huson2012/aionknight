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

package gameserver.model.templates.spawn;

import javax.xml.bind.annotation.*;
import java.util.BitSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "object")
public class SpawnTemplate
{
	/**
	 * XML attributes
	 * Order should be reversed to XML attributes order
	 */
	@XmlTransient
	private SpawnGroup spawnGroup;
	@XmlAttribute(name = "rw")
	private int	randomWalk;
	@XmlAttribute(name = "w")
	private int	walkerId;
	@XmlAttribute(name = "h")
	private byte heading;
	@XmlAttribute(name = "z")
	private float z;
	@XmlAttribute(name = "y")
	private float y;
	@XmlAttribute(name = "x")
	private float x;
	@XmlAttribute(name = "staticid")
	private int staticid;
	@XmlAttribute(name = "fly")
	private int npcfly;
	@XmlTransient
	private BitSet spawnState = new BitSet();
	@XmlTransient
	private BitSet noRespawn = new BitSet();
	@XmlTransient
	private BitSet restingState = new BitSet();
	
	private int spawnId = 0;
	
	/**
	 * Constructor used by unmarshaller
	 */
	public SpawnTemplate()
	{
		
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerId
	 * @param randomWalk
	 * @param npcstate
	 */
	public SpawnTemplate(float x, float y, float z, byte heading, int walkerId, int randomWalk, int npcfly)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
		this.walkerId = walkerId;
		this.randomWalk = randomWalk;
		this.npcfly = npcfly;
	}
	
	public int getWorldId()
	{
		return spawnGroup.getMapid();
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

	public byte getHeading()
	{
		return heading;
	}
	
	public int getWalkerId()
	{
		return walkerId;
	}

	/**
	 * Set the randomWalk value only if it is 0.
	 * @param rw
	 */
	protected void setRandomWalkNr(int rw) {
		if(randomWalk == 0)
		{
			randomWalk = rw;
		}
	}

	public int getRandomWalkNr()
	{
		return randomWalk;
	}
	
	public boolean hasRandomWalk()
	{
		return randomWalk > 0;
	}
	
	public int getNpcFlyState()
	{
		return npcfly;
	}

	/**
	 * @return the spawnGroup
	 */
	public SpawnGroup getSpawnGroup()
	{
		return spawnGroup;
	}

	/**
	 * @param spawnGroup the spawnGroup to set
	 */
	public void setSpawnGroup(SpawnGroup spawnGroup)
	{
		this.spawnGroup = spawnGroup;
	}

	/**
	 * @return the isResting
	 */
	public boolean isResting(int instance)
	{
		return restingState.get(instance);
	}

	/**
	 * @param isResting the isResting to set
	 */
	public void setResting(boolean isResting, int instance)
	{
		restingState.set(instance, isResting);
	}

	/**
	 * @return the isSpawned
	 */
	public boolean isSpawned(int instance)
	{
		return spawnState.get(instance);
	}

	/**
	 * @param isSpawned the isSpawned to set
	 */
	public void setSpawned(boolean isSpawned, int instance)
	{
		spawnState.set(instance, isSpawned);
	}

	/**
	 * @param instance
	 * @return true or false
	 */
	public boolean isNoRespawn(int instance)
	{		
		return noRespawn.get(instance);
	}

	/**
	 * @param noRespawn the respawn to set
	 */
	public void setNoRespawn(boolean noRespawn, int instance)
	{
		this.noRespawn.set(instance, noRespawn);
	}

	/**
	 * @return the staticid
	 */
	public int getStaticid()
	{
		return staticid;
	}

	public void setSpawnId(int spawnId)
	{
		this.spawnId = spawnId;
	}
	
	public int getSpawnId()
	{
		return spawnId;
	}
}