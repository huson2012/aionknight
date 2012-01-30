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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_SUMMON_ATTACK extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_SUMMON_ATTACK.class);

	@SuppressWarnings("unused")
	private int summonObjId;
	private int targetObjId;
	@SuppressWarnings("unused")
	private int unk1;
	@SuppressWarnings("unused")
	private int unk2;
	@SuppressWarnings("unused")
	private int unk3;
	
	public CM_SUMMON_ATTACK(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		summonObjId = readD();
		targetObjId = readD();
		unk1 = readC();
		unk2 = readH();
		unk3 = readC();
	}

	@Override
	protected void runImpl()
	{
		// TODO: Use summonObjId to get summon, instead of activePlayer?
		Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null)
		{
			log.error("CM_SUMMON_ATTACK packet received but cannot get master player.");
			return;
		}
		
		Summon summon = activePlayer.getSummon();
		
		if(summon == null)
		{
			log.error("CM_SUMMON_ATTACK packet received but cannot get summon.");
			return;
		}
		
		Creature creature = (Creature) World.getInstance().findAionObject(targetObjId);
		if (creature == null)
			return;
		summon.getController().attackTarget(creature);
	}
}
