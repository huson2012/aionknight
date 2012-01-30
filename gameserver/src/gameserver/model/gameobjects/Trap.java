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

import gameserver.ai.npcai.TrapAi;
import gameserver.controllers.NpcController;
import gameserver.controllers.NpcWithCreatorController;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

public class Trap extends NpcWithCreator
{
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Trap(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}

	
	@Override
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController) super.getController();
	}
	public Trap getOwner()
	{
		return (Trap)this;
	}
	@Override
	public byte getLevel()
	{
		return (this.creator == null ? 1 : this.creator.getLevel());
	}

	@Override
	public void initializeAi()
	{
		this.ai = new TrapAi();
		ai.setOwner(this);
	}
	/**
	 * @return NpcObjectType.TRAP
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.TRAP;
	}
}
