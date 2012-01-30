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

package gameserver.model.templates.bonus;

import gameserver.model.Gender;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CutSceneBonus")
public class CutSceneBonus extends AbstractInventoryBonus
{

	static final InventoryBonusType type = InventoryBonusType.MOVIE;
	
	@XmlAttribute
	protected Gender gender;

	@XmlAttribute(name = "movieId")
	protected int movieId;
	
	@XmlAttribute()
	protected int checkItem;

	/** (non-Javadoc)
	 * @see gameserver.itemengine.bonus.AbstractInventoryBonus#canApply(gameserver.model.gameobjects.player.Player, int)
	 */
	@Override
	public boolean canApply(Player player, int itemId, int questId)
	{
		PlayerCommonData data = player.getCommonData();
		boolean itemIdValid = false;
		if(itemId != 0) 
		{
			if(checkItem == 0 || count == 0)
				itemIdValid = true;
			else
				itemIdValid = itemId == checkItem;
			if(itemIdValid)
				return player.getInventory().getItemCountByItemId(itemId) >= count &&
					data.getGender().ordinal() == gender.ordinal();
		}
		else
			return player.getInventory().getItemCountByItemId(checkItem) >= count &&
				data.getGender().ordinal() == gender.ordinal();
		return false;
	}

	/** (non-Javadoc)
	 * @see gameserver.itemengine.bonus.AbstractInventoryBonus#apply(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public boolean apply(Player player, Item item)
	{
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movieId));
		return true;
	}

	/** (non-Javadoc)
	 * @see gameserver.model.templates.bonus.AbstractInventoryBonus#getType()
	 */
	@Override
	public InventoryBonusType getType()
	{
		return type;
	}
}
