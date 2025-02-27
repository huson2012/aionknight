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
import gameserver.model.legion.Legion;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LEGION_INFO;
import gameserver.services.LegionService;
import org.apache.log4j.Logger;

public class CM_LEGION extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_LEGION.class);

	/**
	 * exOpcode and the rest
	 */
	private int					exOpcode;
	private int                 deputyPermission1;
	private int                 deputyPermission2;
	private int                 centurionPermission1;
	private int                 centurionPermission2;
	private int                 legionaryPermission1;
	private int                 legionaryPermission2;
	private int                 volunteerPermission1;
	private int					volunteerPermission2;

	private int					rank;
	private String				legionName;
	private String				charName;
	private String				newNickname;
	private String				announcement;
	private String				newSelfIntro;

	/**
	 * Constructs new instance of CM_LEGION packet
	 * 
	 * @param opcode
	 */
	public CM_LEGION(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		exOpcode = readC();

		switch(exOpcode)
		{
			/** Create a legion **/
			case 0x00:
				readD(); // 00 78 19 00 40
				legionName = readS();
				break;
			/** Invite to legion **/
			case 0x01:
				readD(); // empty
				charName = readS();
				break;
			/** Leave legion **/
			case 0x02:
				readD(); // empty
				readH(); // empty
				break;
			/** Kick member from legion **/
			case 0x04:
				readD(); // empty
				charName = readS();
				break;
			/** Appoint a new Brigade General **/
			case 0x05:
				readD();
				charName = readS();
				break;
			/** Appoint Centurion **/
			case 0x06:
				rank = readD();
				charName = readS();
				break;
			/** Demote to Legionary **/
			case 0x07:
				readD(); // char id? 00 78 19 00 40
				charName = readS();
				break;
			/** Refresh legion info **/
			case 0x08:
				break;
			/** Edit announcements **/
			case 0x09:
				readD(); // empty or char id?
				announcement = readS();
				break;
			/** Change self introduction **/
			case 0x0A:
				readD(); // empty char id?
				newSelfIntro = readS();
				break;
			/** Edit permissions **/
			case 0x0D:
	            deputyPermission1 = readC();
	            deputyPermission2 = readC();
				centurionPermission1 = readC();
				centurionPermission2 = readC();
                legionaryPermission1 = readC();
				legionaryPermission2 = readC();
                volunteerPermission1 = readC();
                volunteerPermission2 = readC();
				break;
			/** Level legion up **/
			case 0x0E:
				readD(); // empty
				readH(); // empty
				break;
			case 0x0F:
				charName = readS();
				newNickname = readS();
				break;
			default:
				log.info("Unknown Legion exOpcode? 0x" + Integer.toHexString(exOpcode).toUpperCase());
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer.isLegionMember())
		{
			final Legion legion = activePlayer.getLegion();

			if(charName != null)
			{
				LegionService.getInstance().handleCharNameRequest(exOpcode, activePlayer, charName, newNickname, rank);
			}
			else
			{
				switch(exOpcode)
				{
					/** Refresh legion info **/
					case 0x08:
						sendPacket(new SM_LEGION_INFO(legion));
						break;
					/** Edit announcements **/
					case 0x09:
						LegionService.getInstance().handleLegionRequest(exOpcode, activePlayer, announcement);
						break;
					/** Change self introduction **/
					case 0x0A:
						LegionService.getInstance().handleLegionRequest(exOpcode, activePlayer, newSelfIntro);
						break;
					/** Edit permissions **/
					case 0x0D:
						if(activePlayer.getLegionMember().isBrigadeGeneral())
							LegionService.getInstance().changePermissions(legion, legionaryPermission1, legionaryPermission2, centurionPermission1, centurionPermission2, deputyPermission1, deputyPermission2, volunteerPermission1, volunteerPermission2);
						break;
					/** Misc. **/
					default:
						LegionService.getInstance().handleLegionRequest(exOpcode, activePlayer);
						break;
				}
			}
		}
		else
		{
			switch(exOpcode)
			{
				/** Create a legion **/
				case 0x00:
					LegionService.getInstance().createLegion(activePlayer, legionName);
					break;
			}
		}
	}
}