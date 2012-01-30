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
import gameserver.network.aion.serverpackets.SM_INSTANCE_COOLDOWN;
import gameserver.services.InstanceService;
import gameserver.utils.PacketSendUtility;
import java.util.HashMap;
import java.util.Map;

public class CM_INSTANCE_CD_REQUEST extends AionClientPacket
{
	
	public CM_INSTANCE_CD_REQUEST(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		readD();
		readC(); // channel
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(true)); //clear everything
		Map<Integer, Integer> infos = new HashMap<Integer, Integer>();
		boolean first = true;
		
		if(activePlayer.getPlayerGroup() != null)
		{
			for(Player member: activePlayer.getPlayerGroup().getMembers())
			{
				if(!activePlayer.equals(member))
				{
					infos = InstanceService.getTimeInfo(member);
					for(Map.Entry<Integer, Integer> entry : infos.entrySet())
					{
						int time = entry.getValue();
						if(time!=0)
						{
							if(first)
							{
								first = false;
								PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(member, entry.getKey(), time, 1, false));
							}
							else
								PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(member, entry.getKey(), time, 2, false));
						}
					}
				}
			}
		}
		
		infos = InstanceService.getTimeInfo(activePlayer);
		
		for(Map.Entry<Integer, Integer> integerIntegerEntry : infos.entrySet())
		{
			int time = integerIntegerEntry.getValue();
			if(time!=0)
				PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(activePlayer, integerIntegerEntry.getKey(), time, 2, true));
		}
	}
}
