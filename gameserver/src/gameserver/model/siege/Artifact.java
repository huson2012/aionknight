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

package gameserver.model.siege;

import gameserver.controllers.ArtifactController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.siege.ArtifactTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import javolution.util.FastList;

public class Artifact extends Npc
{
	private int spawnStaticId; 
	private int artifactId;
	private ArtifactProtector protector;
	
	private FastList<Integer> relatedSpawnedObjectsIds;
	private ArtifactTemplate template;

	public Artifact(int objId, ArtifactController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int artifactId, int staticId)
	{
		super(objId, controller, spawn, objectTemplate);
		this.artifactId = artifactId;
		this.spawnStaticId = staticId; 
		this.relatedSpawnedObjectsIds = new FastList<Integer>();
	}
	
	public void setProtector(ArtifactProtector protector)
	{
		this.protector = protector;
		protector.setArtifact(this);
	}
	
	public int getLocationId()
	{
		return artifactId;
	}
	
	public int getStaticId()
	{
		return spawnStaticId;
	}
	
	public ArtifactProtector getProtector()
	{
		return protector;
	}
	
	public void registerRelatedSpawn(int objectId)
	{
		if(!relatedSpawnedObjectsIds.contains(objectId))
			relatedSpawnedObjectsIds.add(objectId);
	}
	
	public FastList<Integer> getRelatedSpawnIds()
	{
		return relatedSpawnedObjectsIds;
	}

	public ArtifactTemplate getTemplate()
	{
		return template;
	}

	public void setTemplate(ArtifactTemplate template)
	{
		this.template = template;
	}
	
	public ArtifactController getController()
	{
		return (ArtifactController)super.getController();
	}
}
