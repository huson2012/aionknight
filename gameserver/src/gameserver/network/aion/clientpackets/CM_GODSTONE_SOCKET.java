/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemCategory;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.ItemService;
import gameserver.utils.MathUtil;
import gameserver.world.World;


/**

 *
 */
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
