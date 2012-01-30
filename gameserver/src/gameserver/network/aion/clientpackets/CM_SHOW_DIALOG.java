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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.Artifact;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_SHOW_DIALOG extends AionClientPacket
{
	private int	targetObjectId;

	/**
	 * Constructs new instance of <tt>CM_SHOW_DIALOG </tt> packet
	 * @param opcode
	 */
	public CM_SHOW_DIALOG(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionObject targetObject = World.getInstance().findAionObject(targetObjectId);
		Player player = getConnection().getActivePlayer();

		if(targetObject == null || player == null || !(targetObject instanceof Npc))
			return;
		if(!MathUtil.isIn3dRange((Npc)targetObject, player, 30))
		{
			Logger.getLogger(this.getClass()).info("[AUDIT]Player "+player.getName()+" sending fake CM_SHOW_DIALOG!");
			return;
		}
		
		if(targetObject instanceof Artifact)
		{
			((Artifact)targetObject).getController().onDialogRequest(player);
		}		
		else if(targetObject instanceof Npc)
		{
			((Npc) targetObject).setTarget(player);

			//TODO this is not needed for all dialog requests
			PacketSendUtility.broadcastPacket((Npc) targetObject,
				new SM_LOOKATOBJECT((Npc) targetObject));
		
			((Npc) targetObject).getController().onDialogRequest(player);
		}
	}
}
