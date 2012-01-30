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

package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * Client Sends this packet when /Select NAME is typed.
 * I believe it's the same as mouse click on a character.
 * If client want's to select target - d is object id.
 * If client unselects target - d is 0;
 */
public class CM_TARGET_SELECT extends AionClientPacket
{
	/**
	 * Target object id that client wants to select or 0 if wants to unselect
	 */
	private int	targetObjectId;
	private int	type;

	/**
	 * Constructs new client packet instance.
	 * @param opcode
	 */
	public CM_TARGET_SELECT(int opcode)
	{
		super(opcode);
	}

	/**
	 * Read packet.<br>
	 * d - object id;
	 * c - selection type;
	 */
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();
		type = readC();
	}

	/**
	 * Do logging
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;

		AionObject obj = World.getInstance().findAionObject(targetObjectId);
		if(obj != null && obj instanceof VisibleObject)
		{
			//select targets target
			if(type == 1)
			{
				if(((VisibleObject) obj).getTarget() == null)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ASSISTKEY_NO_USER);
					return;
				}
				if (player.canSee(((VisibleObject)obj).getTarget()))
					player.setTarget(((VisibleObject) obj).getTarget());
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ASSISTKEY_INCORRECT_TARGET);
					return;
				}
			}
			else
			{
				player.setTarget(((VisibleObject) obj));
			}
		}
		else
		{
			player.setTarget(null);
		}
		sendPacket(new SM_TARGET_SELECTED(player));
		PacketSendUtility.broadcastPacket(player, new SM_TARGET_UPDATE(player));
	}
}
