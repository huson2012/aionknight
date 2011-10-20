package ru.aionknight.gameserver.controllers;


import ru.aionknight.gameserver.ai.events.Event;
import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.teleport.TelelocationTemplate;
import ru.aionknight.gameserver.model.templates.teleport.TeleportLocation;
import ru.aionknight.gameserver.model.templates.teleport.TeleporterTemplate;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.services.TeleportService;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.exceptionhandlers.exception_enums;

/**
 * @author Dns
 * 
 */

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
			return;
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