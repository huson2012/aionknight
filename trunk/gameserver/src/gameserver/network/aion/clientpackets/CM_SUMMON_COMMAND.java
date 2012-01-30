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

import gameserver.controllers.SummonController.UnsummonType;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;

public class CM_SUMMON_COMMAND extends AionClientPacket
{
	private int mode;
	private int targetObjId;
	
	public CM_SUMMON_COMMAND(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		mode = readC();
		readD();
		readD();
		targetObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		Summon summon = activePlayer.getSummon();
		if(summon != null)
		{
			switch(mode)
			{
				case 0:
					AionObject target = World.getInstance().findAionObject(targetObjId);
					if(target != null && target instanceof Creature)
					{
						summon.getController().attackMode();
					}
				break;
				case 1:
					summon.getController().guardMode();
				break;
				case 2:
					summon.getController().restMode();
				break;
				case 3:
					summon.getController().release(UnsummonType.COMMAND);
				break;
			}
		}
	}
}
