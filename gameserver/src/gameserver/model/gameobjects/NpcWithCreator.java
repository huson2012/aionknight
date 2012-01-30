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

import gameserver.controllers.NpcController;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

public class NpcWithCreator extends Npc
{
	/**
	 * this class is extended by
	 * GroupGate
	 * Servant
	 * Homing
	 * SkillAreaNpc
	 * Totem
	 * Trap
	 * 
	 * for easier handling
	 */
	
	protected Creature creator;
	
	protected int skillId;
	
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public NpcWithCreator(int objId, NpcController controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}
	
	public NpcWithCreator getOwner()
	{
		return this;
	}
	
	@Override
	public Creature getActingCreature()
	{
		return this.creator;
	}

	@Override
	public Creature getMaster()
	{
		return this.creator;
	}
	
	/**
	 * @return the creator
	 */
	public Creature getCreator()
	{
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Creature creator)
	{
		this.creator = creator;
	}
	
	/**
	 * @return the skillId
	 */
	public int getSkillId()
	{
		return skillId;
	}

	/**
	 * @param skillId the skillId to set
	 */
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}
	
	@Override
	public boolean isEnemy(VisibleObject visibleObject)
	{
		if (creator == null)
		{
			getOwner().getLifeStats().reduceHp(10000, getOwner());
			return false;
		}
		return super.isEnemy(visibleObject);
	}
	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		return this.creator.isEnemyNpc(visibleObject);
	}

	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		return this.creator.isEnemyPlayer(visibleObject);
	}
	
	@Override
	protected boolean isEnemySummon(Summon summon)
	{
		return this.creator.isEnemySummon(summon);
	}
}
