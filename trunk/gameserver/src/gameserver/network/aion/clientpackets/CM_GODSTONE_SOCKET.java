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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemCategory;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.ItemService;
import gameserver.utils.MathUtil;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_GODSTONE_SOCKET extends AionClientPacket
{
	
	private int npcId;
	private int weaponId;
	private int stoneId;
	
	public CM_GODSTONE_SOCKET(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		this.npcId = readD();
		this.weaponId = readD();
		this.stoneId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		Npc npc = (Npc) World.getInstance().findAionObject(npcId);
		if(npc == null)
			return;
		
		if(!MathUtil.isInRange(activePlayer, npc, 10))
			return;
		
		Item stone = activePlayer.getInventory().getItemByObjId(stoneId);
		Item weapon = activePlayer.getInventory().getItemByObjId(weaponId);

		if (stone == null || weapon == null)
		{
			return;
		}
		boolean isWeapon = weapon.getItemTemplate().isWeapon();
		if(stone.getItemTemplate().getItemCategory() != ItemCategory.HOLYSTONE)
		{
			Logger.getLogger(this.getClass()).info("[AUDIT]Player: "+activePlayer.getName()+" is trying to socket non godstone => Hacking!");
			return;
		}
		if (!isWeapon)
		{
			Logger.getLogger(this.getClass()).info("[AUDIT]Player: "+activePlayer.getName()+" is trying to socket godstone to non weapon => Hacking!");
			return;
		}
		
		ItemService.socketGodstone(activePlayer, weaponId, stoneId);
	}
}