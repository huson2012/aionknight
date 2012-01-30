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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_DREDGION_INSTANCE;
import gameserver.services.DredgionInstanceService;
import gameserver.utils.PacketSendUtility;

public class CM_DREDGION_REQUEST extends AionClientPacket
{

	private byte type;
	private int state;
	
	public CM_DREDGION_REQUEST(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		type = (byte) readD(); // 1 for dredgion regular - 2 for chantra
		state = readH(); // 68 00 when clicking on little icon, 64 00 when registrating as individual(private) 64 01 (quick) 64 02 (group) and opened wait window, 65 when canceling registration.
		
	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		if(player != null)
		{
			switch(state)
			{
				//private entry
				case 100:
					if(DredgionInstanceService.getInstance().privateEntry(player))
					{
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 1, 1, 21248));
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
					}
				break;
				//cancel
				case 101:
					DredgionInstanceService.getInstance().cancelGroup(player.getPlayerGroup());
					PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
				break;
				//open request entry window
				case 104:
					PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 0, 0, 0));
				break;
				//quick entry
				case 356:
					if(DredgionInstanceService.getInstance().quickEntry(player))
					{
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 1, 1, 21248));
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
					}
				break;
				//group entry
				case 612:
					if(DredgionInstanceService.getInstance().groupEntry(player))
					{

						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 1, 1, 21248));
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
					}
				break;

			}
		}
	}	
}
