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

package gameserver.ai.desires.impl;

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.desires.MoveDesire;
import gameserver.ai.events.Event;
import gameserver.controllers.movement.MovementType;
import gameserver.model.EmotionType;
import gameserver.model.ShoutEventType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.services.NpcShoutsService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class MoveToHomeDesire extends AbstractDesire implements MoveDesire
{
	private Npc owner;
	private float x;
	private float y;
	private float z;
	boolean started = false;
	
	public MoveToHomeDesire(Npc owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		SpawnTemplate template = owner.getSpawn();
		x = template.getX();
		y = template.getY();
		z = template.getZ();
	}

	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;
		
		if(!started)
		{
			if(MathUtil.getDistance(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 50){
				
				// Target too far, teleport to spawn point.
				World.getInstance().updatePosition(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ(), owner.getSpawn().getHeading(), true);
				started = true;
			}
			else {
				owner.getMoveController().stopFollowing();
				owner.getMoveController().setNewDirection(x, y, z);
				owner.getGameStats().setStat(StatEnum.SPEED, (int) owner.getObjectTemplate().getStatsTemplate().getRunSpeed() * 1000);
				owner.getMoveController().setSpeed(owner.getObjectTemplate().getStatsTemplate().getRunSpeed());
				owner.getMoveController().setMovementType(MovementType.NPC_MOVEMENT_TYPE_III);
				PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2));
				started = true;
			}
			NpcShoutsService.getInstance().handleEvent(owner, owner, ShoutEventType.LEAVE);
		}

		double dist = MathUtil.getDistance(owner, x, y, z);
		if(dist < 1)
		{
			// Make sure npc is at right position
			World.getInstance().updatePosition(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ(), owner.getSpawn().getHeading(), true);
			ai.handleEvent(Event.BACK_HOME);
			return false;
		}
		else
		{
			if(!owner.getMoveController().isScheduled())
				owner.getMoveController().schedule();
		}
		return true;
	}

	@Override
	public int getExecutionInterval()
	{
		return 5;
	}

	@Override
	public void onClear()
	{
 		owner.getMoveController().stop();
 		owner.getController().stopMoving();
	}	
}