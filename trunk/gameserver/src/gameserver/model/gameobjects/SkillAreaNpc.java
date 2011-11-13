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

package gameserver.model.gameobjects;

import gameserver.ai.npcai.SkillAreaNpcAi;
import gameserver.controllers.NpcWithCreatorController;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

public class SkillAreaNpc extends NpcWithCreator
{
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public SkillAreaNpc(int objId, NpcWithCreatorController controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
		// TODO Auto-generated constructor stub
	}
	
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController)super.getController();
	}
	public SkillAreaNpc getOwner()
	{
		return (SkillAreaNpc)this;
	}
	@Override
	public void initializeAi()
	{
		this.ai = new SkillAreaNpcAi();
		ai.setOwner(this);
	}
	
	/**
	 * @return NpcObjectType.SKILLAREANPC
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.SKILLAREANPC;
	}
}