/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.model.gameobjects;


import ru.aionknight.gameserver.ai.npcai.ServantAi;
import ru.aionknight.gameserver.controllers.NpcController;
import ru.aionknight.gameserver.controllers.NpcWithCreatorController;
import ru.aionknight.gameserver.model.templates.VisibleObjectTemplate;
import ru.aionknight.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ATracer
 *
 */
public class Servant extends NpcWithCreator
{
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Servant(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}

	@Override
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController) super.getController();
	}
	public Servant getOwner()
	{
		return (Servant)this;
	}
	@Override
	public void initializeAi()
	{
		this.ai = new ServantAi();
		ai.setOwner(this);
	}
	
	/**
	 * @return NpcObjectType.TRAP
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.SERVANT;
	}
}
