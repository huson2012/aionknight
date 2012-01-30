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

package gameserver.controllers;

import gameserver.ai.events.Event;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.teleport.TelelocationTemplate;
import gameserver.model.templates.teleport.TeleportLocation;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.exceptionhandlers.exception_enums;

public class RestrictedPortalController extends NpcController
{
	@Override
	public void onDialogRequest(final Player player)
	{
		getOwner().getAi().handleEvent(Event.TALK);

		// Inggison & Gelkmaros teleporters
		Npc npc = getOwner();
		if(npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ASMO
			|| npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ELYOS)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
        }
	}

	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId)
	{
		Npc npc = getOwner();
		int targetObjectId = npc.getObjectId();

		// 1st case : Gelkmaros & Inggison
		if(dialogId == 10000
			&& (npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ASMO || npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ELYOS))
		{
			int completedquestid = 0;
			switch(player.getCommonData().getRace())
			{
				case ASMODIANS:
					completedquestid = 20001;
					break;
				case ELYOS:
					completedquestid = 10001;
					break;
			}
			QuestState qstel = player.getQuestStateList().getQuestState(completedquestid);
			if(qstel == null || qstel.getStatus() != QuestStatus.COMPLETE)
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
				return;
			}

			TeleporterTemplate template = DataManager.TELEPORTER_DATA.getTeleporterTemplate(npc.getNpcId());
			if(template != null)
			{
				TeleportLocation loc = template.getTeleLocIdData().getTelelocations().get(0);
				if(loc != null)
				{
					if(!player.getInventory().decreaseKinah(loc.getPrice()))
						return;
					
					TelelocationTemplate tlt = DataManager.TELELOCATION_DATA.getTelelocationTemplate(loc.getLocId());
					TeleportService.teleportTo(player, tlt.getMapId(), tlt.getX(), tlt.getY(), tlt.getZ(), 1000);
				}
			}
		}
	}
}