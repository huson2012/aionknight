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

import gameserver.ai.events.Event;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.NpcWithCreator;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.utils.PacketSendUtility;

public class NpcWithCreatorController extends NpcController
{
	@Override
    public void onDie(Creature lastAttacker)
    {
		super.onCreatureDie(lastAttacker);
		
		PacketSendUtility.broadcastPacket(this.getOwner(),
			new SM_EMOTION(this.getOwner(), EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));
		
		this.getOwner().getAi().handleEvent(Event.DIED);

		// Deselect target at the end
		this.getOwner().setTarget(null);
		PacketSendUtility.broadcastPacket(this.getOwner(), new SM_LOOKATOBJECT(this.getOwner()));
		
		onDelete();
    }

	@Override
	public void onDialogRequest(Player player)
	{
    }

	@Override
	public NpcWithCreator getOwner()
	{
		return  (NpcWithCreator)super.getOwner();
	}
	@Override
	public void onDelete()
	{
		getOwner().setCreator(null);
		super.onDelete();
	}
}