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

package gameserver.controllers;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.ArtifactProtector;
import gameserver.services.SiegeService;

public class ArtifactProtectorController extends NpcController
{
	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}
	
	@Override
	public void onStartMove()
	{
		super.onStartMove();
	}
	
	@Override
	public void onMove()
	{
		super.onMove();
	}
	
	@Override
	public void onStopMove()
	{
		super.onStopMove();
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		if(lastAttacker instanceof Player || lastAttacker instanceof Summon || lastAttacker instanceof Trap)
		{
			Player taker;
			if(lastAttacker instanceof Player)
				taker = (Player)lastAttacker;
			else if(lastAttacker instanceof Summon)
				taker = ((Summon)lastAttacker).getMaster();
			else if(lastAttacker instanceof Trap)
				taker = (Player)((Trap)lastAttacker).getCreator();
			else
				taker = null;
			
			if(taker != null)			
				SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact(), taker);
		}
		else
		{
			// Taken by Balaur
			if(lastAttacker != null)
				SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact());
		}
		super.onDie(lastAttacker);
	}

	@Override
	public ArtifactProtector getOwner()
	{
		return (ArtifactProtector) super.getOwner();
	}
}