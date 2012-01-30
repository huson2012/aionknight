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

import gameserver.ai.npcai.AggressiveAi;
import gameserver.ai.npcai.MonsterAi;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.FortressGeneralController;
import gameserver.model.alliance.PlayerAllianceGroup;
import gameserver.model.gameobjects.Npc;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import javolution.util.FastList;

public class FortressGeneral extends Npc
{	
	private int linkedFortressId;
	private FastList<PlayerGroup> rewardGroups;
	private FastList<PlayerAllianceGroup> rewardAlliances;

	public FortressGeneral(int objId, FortressGeneralController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId)
	{
		super(objId, controller, spawn, objectTemplate);
		this.linkedFortressId = fortressId;
		this.rewardGroups = new FastList<PlayerGroup>();
		this.rewardAlliances = new FastList<PlayerAllianceGroup>();
	}
	
	public void registerGroup(PlayerGroup group)
	{
		if(!rewardGroups.contains(group))
			rewardGroups.add(group);
	}
	
	public void registerAllianceGroup(PlayerAllianceGroup group)
	{
		if(!rewardAlliances.contains(group))
			rewardAlliances.add(group);
	}
	
	public FastList<PlayerGroup> getRewardGroups()
	{
		return rewardGroups;
	}
	
	public FastList<PlayerAllianceGroup> getRewardAlliances()
	{
		return rewardAlliances;
	}

	public int getFortressId()
	{
		return this.linkedFortressId;
	}

	@Override
	public FortressGeneralController getController()
	{
		return (FortressGeneralController) super.getController();
	}

	@Override
	public void initializeAi()
	{
		if(isAggressive() && !CustomConfig.DISABLE_MOB_AGGRO)
			this.ai = new AggressiveAi();
		else
			this.ai = new MonsterAi();

		ai.setOwner(this);	
	}
}
