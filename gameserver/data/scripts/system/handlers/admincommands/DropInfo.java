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

package admincommands;

import gameserver.model.drop.DropItem;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import java.util.Set;

public class DropInfo extends AdminCommand 
{

	private DropList dropList;
	public DropInfo() 
	{
		super("dropinfo");
	}

	@Override
	public void executeCommand(Player player, String[] params) 
	{

		VisibleObject visibleObject = player.getTarget();

		if (visibleObject == null) 
		{
			PacketSendUtility.sendMessage(player, "You should select target npc first.");
			return;
		}

		if (visibleObject instanceof Npc) 
		{
			dropList = DropService.getInstance().getDropList();

			Set<DropTemplate> templates = dropList.getDropsFor(((Npc) visibleObject).getNpcId());

			if (templates != null) {
				PacketSendUtility.sendMessage(player, "[Drop Info about NPC]\n");
				for (DropTemplate dropTemplate : templates) 
				{
					DropItem dropItem = new DropItem(dropTemplate);
					PacketSendUtility.sendMessage(player, "[item:" + dropItem.getDropTemplate().getItemId() + "]" + "	Rate: "
						+ dropItem.getDropTemplate().getChance());
				}
			}
		}
	}
}