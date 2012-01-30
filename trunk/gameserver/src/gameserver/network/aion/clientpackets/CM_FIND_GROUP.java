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
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_FIND_GROUP;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.LGFService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class CM_FIND_GROUP extends AionClientPacket
{
	private int type;
	private int playerID;
	private String applyString;
	private int groupType;
	@SuppressWarnings("unused")
	private int groupID;
	
	public CM_FIND_GROUP(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readC();//0 recruit group members 4 apply for group

		switch(type)
		{
			//remove recruit group
			case 1:
				playerID = readD();
				groupID = readD();
				applyString = readS();
				groupType = readC();
			break;
			//send recruit group
			case 2:
				playerID = readD();
				applyString = readS();
				groupType = readC();
			break;
			//update recruit group or apply for group
			case 3:
				playerID = readD();
				groupID = readD();
				applyString = readS();
				groupType = readC();			
			break;
			//remove apply for group
			case 5:
				playerID = readD();
			break;
			//send apply for group
			case 6:
				playerID = readD();
				applyString = readS();
				groupType = readC();
			break;
			//update apply for group
			case 7:
				playerID = readD();
				applyString = readS();
				groupType = readC();
			break;
		}
	}

	/**
	 * {@inheritDoc}n
	 */
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		switch(type)
		{
			//recruit group
			case 0:
				PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(type, player));
			break;
			//remove recruit group
			case 1:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeRecruitGroup(playerID);

					World.getInstance().doOnAllPlayers(new Executor<Player>(){
						@Override
						public boolean run(Player p)
						{
							if(p.getCommonData().getRace() == player.getCommonData().getRace())
							{
								PacketSendUtility.sendPacket(p, new SM_FIND_GROUP(type, player));
							}
							return true;
						}
					});
				}
			break;
			//send recruit group
			case 2:
				if(!player.isInGroup())
					return;

				if(LGFService.getInstance().addRecruitGroup(playerID, applyString, groupType, 55, player))
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400392));
				else
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400394));
			break;
			//send update recruit group
			case 3:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeRecruitGroup(playerID);
					LGFService.getInstance().addRecruitGroup(playerID, applyString, groupType, 55, player);
				}
			break;
			//apply for group
			case 4:
				PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(type, player));
			break;
			//remove apply for group
			case 5:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeApplyGroup(playerID);

					World.getInstance().doOnAllPlayers(new Executor<Player>(){
						@Override
						public boolean run(Player p)
						{
							if(p.getCommonData().getRace() == player.getCommonData().getRace())
							{
								PacketSendUtility.sendPacket(p, new SM_FIND_GROUP(type, player));
							}
							return true;
						}
					});
				}
			break;
			//send apply for group
			case 6:
				if(LGFService.getInstance().addApplyGroup(playerID, applyString, groupType, player))
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400393));
				else
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400394));
			break;
			//update apply for group
			case 7:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeApplyGroup(playerID);
					LGFService.getInstance().addApplyGroup(playerID, applyString, groupType, player);
				}
			break;
		}
	}
}
