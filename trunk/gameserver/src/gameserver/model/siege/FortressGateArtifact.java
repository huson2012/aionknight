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

import gameserver.controllers.FortressGateArtifactController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

public class FortressGateArtifact extends Npc
{
	private int fortressId;
	private FortressGate relatedGate;
	private int healRatio;
	private int spawnStaticId;
	
	public FortressGateArtifact(int objId, FortressGateArtifactController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId, int staticId,int healRatio)
	{
		super(objId, controller, spawn, objectTemplate);
		this.fortressId = fortressId;
		this.spawnStaticId = staticId;
		this.healRatio = healRatio;
	}
	
	public FortressGate getRelatedGate()
	{
		return relatedGate;
	}
	
	public void setRelatedGate(FortressGate gate)
	{
		relatedGate = gate;
	}
	
	public void healGate()
	{
		int hpToAdd = relatedGate.getLifeStats().getMaxHp() * (healRatio / 100);
		relatedGate.getLifeStats().increaseHp(TYPE.NATURAL_HP, hpToAdd);
	}
	
	public int getFortressId()
	{
		return fortressId;
	}
	
	public int getStaticId()
	{
		return spawnStaticId;
	}	
}
