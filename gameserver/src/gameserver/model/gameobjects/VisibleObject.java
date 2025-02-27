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

package gameserver.model.gameobjects;

import gameserver.controllers.VisibleObjectController;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.world.*;

public abstract class VisibleObject extends AionObject
{
	protected VisibleObjectTemplate objectTemplate;
	
	/**
	 * Constructor.
	 * @param objId
	 * @param objectTemplate 
	 */
	public VisibleObject(int objId, VisibleObjectController<? extends VisibleObject> controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate, WorldPosition position)
	{
		super(objId);
		this.controller = controller;
		this.position = position;
		this.spawn = spawnTemplate;
		this.objectTemplate = objectTemplate;
	}

	/**
	 * Position of object in the world.
	 */
	private WorldPosition											position;

	/**
	 * KnownList of this VisibleObject.
	 */
	private KnownList												knownlist;

	/**
	 * Controller of this VisibleObject
	 */
	private final VisibleObjectController<? extends VisibleObject>	controller;
	
	/**
	 * Visible object's target
	 */
	private VisibleObject	target;
	
	/**
	 * Spawn template of this visibleObject. .
	 */
	private SpawnTemplate	spawn;

	private boolean	custom = false;

	/**
	 * Returns current WorldRegion AionObject is in.
	 * 
	 * @return mapRegion
	 */
	public MapRegion getActiveRegion()
	{
		return position.getMapRegion();
	}
	
	public int getInstanceId()
	{
		return position.getInstanceId();
	}

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public int getWorldId()
	{
		return position.getMapId();
	}

	/**
	 * Return WorldType of current location
	 * 
	 * @return WorldType of current location
	 */
	public WorldType getWorldType()
	{
		return World.getInstance().getWorldMap(getWorldId()).getWorldType();
	}
	
	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public float getX()
	{
		return position.getX();
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public float getY()
	{
		return position.getY();
	}

	/**
	 * Return World position z
	 * 
	 * @return z
	 */
	public float getZ()
	{
		return position.getZ();
	}

	/**
	 * Heading of the object. Values from <0,120)
	 * 
	 * @return heading
	 */
	public byte getHeading()
	{
		return position.getHeading();
	}

	/**
	 * Return object position
	 * 
	 * @return position.
	 */
	public WorldPosition getPosition()
	{
		return position;
	}
	
	public void setPosition(WorldPosition wp)
	{
		position = wp;
	}

	/**
	 * Check if object is spawned.
	 * 
	 * @return true if object is spawned.
	 */
	public boolean isSpawned()
	{
		return position.isSpawned();
	}
	
	/**
	 * 
	 * @return 
	 */
	public boolean isInWorld()
	{
		return World.getInstance().findAionObject(getObjectId()) != null;
	}
	
	/**
	 * Check if map is instance
	 * 
	 * @return true if object in one of the instance maps
	 */
	public boolean isInInstance()
	{
		return position.isInstanceMap();
	}
	public boolean isInEmpyrean()
	{
		return position.isInEmpyreanMap();
	}
	
	public void clearKnownlist()
	{
		getKnownList().clear();
	}
	
	public void updateKnownlist()
	{
		getKnownList().doUpdate();
	}

	/**
	 * Set KnownList to this VisibleObject
	 * 
	 * @param knownlist
	 */
	public void setKnownlist(KnownList knownlist)
	{
		this.knownlist = knownlist;
	}

	/**
	 * Returns KnownList of this VisibleObject.
	 * 
	 * @return knownList.
	 */
	public KnownList getKnownList()
	{
		return knownlist;
	}

	/**
	 * Return VisibleObjectController of this VisibleObject
	 * 
	 * @return VisibleObjectController.
	 */
	public VisibleObjectController<? extends VisibleObject> getController()
	{
		return controller;
	}
	
	/**
	 * 
	 * @return VisibleObject
	 */
	public VisibleObject getTarget()
	{
		return target;
	}
	
	/**
	 * 
	 * @param creature
	 */
	public void setTarget(VisibleObject creature)
	{
		target = creature;
	}
	
	/**
	 * 
	 * @param objectId
	 * @return target is object with id equal to objectId
	 */
	public boolean isTargeting(int objectId)
	{
		return target != null && target.getObjectId() == objectId; 
	}
	
	/**
	 * Return spawn template of this VisibleObject
	 * 
	 * @return SpawnTemplate
	 */
	public SpawnTemplate getSpawn()
	{
		return spawn;
	}

	/**
	 * @param spawn the spawn to set
	 */
	public void setSpawn(SpawnTemplate spawn)
	{
		this.spawn = spawn;
	}

	/**
	 * @return the objectTemplate
	 */
	public VisibleObjectTemplate getObjectTemplate()
	{
		return objectTemplate;
	}

	/**
	 * @param objectTemplate the objectTemplate to set
	 */
	public void setObjectTemplate(VisibleObjectTemplate objectTemplate)
	{
		this.objectTemplate = objectTemplate;
	}

	/**
	 * @param custom
	 */
	public void setCustom(boolean custom)
	{
		this.custom  = custom;
	}
	
	public boolean isCustom ()
	{
		return custom;
	}
}
