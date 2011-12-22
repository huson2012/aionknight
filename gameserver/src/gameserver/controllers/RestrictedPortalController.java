/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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