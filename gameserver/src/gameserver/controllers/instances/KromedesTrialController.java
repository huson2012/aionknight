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

package gameserver.controllers.instances;

import gameserver.ai.events.Event;
import gameserver.controllers.NpcController;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.TeleportService;
import gameserver.skill.SkillEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.exceptionhandlers.exception_enums;

public class KromedesTrialController extends NpcController
{
	Npc	npc	= getOwner();

	@Override
	public void onDialogRequest(final Player player)
	{
		getOwner().getAi().handleEvent(Event.TALK);

		switch (getOwner().getNpcId()) {

			case 700924:
				TeleportService.teleportTo(player, 300230000, player.getInstanceId(), 593, 774, 215, 0);
			return;
		}
		NpcTemplate npctemplate = DataManager.NPC_DATA.getNpcTemplate(getOwner().getNpcId());
		if(npctemplate.getNameId() == 371634 || npctemplate.getNameId() == 371688 || npctemplate.getNameId() == 371630
			|| npctemplate.getNameId() == 371648 || npctemplate.getNameId() == 371646 || npctemplate.getNameId() == 371632
			|| npctemplate.getNameId() == 371644 || npctemplate.getNameId() == 371642)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
			return;
		}

		switch(getOwner().getNpcId())
		{
			case exception_enums.NPC_INSTANCE_KR_DOOR_I:
			{
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						final int defaultUseTime = 3000;
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner()
							.getObjectId(), defaultUseTime, 1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT,
							0, getOwner().getObjectId()), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,
									EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner()
									.getObjectId(), defaultUseTime, 0));
								TeleportService.teleportTo(player, 300230000, 593.04126f, 774.2241f, 215.58362f,
									(byte) 118);
							}
						}, defaultUseTime);
					}
				}, 0);

				return;
			}
		}
	}

	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId)
	{
		Npc npc = getOwner();

		if(dialogId == 1012
			&& (npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_DOOR_II
				|| npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_DOOR_III
				|| npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_DOOR_IV))
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
			npc.getController().onDelete();
			return;
		}
		if(dialogId == 1012 && (npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_BUFF_I))// Prophet Tower
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
			SkillEngine.getInstance().getSkill(player, 19219, 1, player).useSkill();
			return;
		}
		if(dialogId == 1012 && (npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_BUFF_II))// Garden Fountain
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
			SkillEngine.getInstance().getSkill(player, 19216, 1, player).useSkill();
			return;
		}
		if(dialogId == 1012 && (npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_BUFF_III))// Porgus Barbecue
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
			SkillEngine.getInstance().getSkill(player, 19218, 1, player).useSkill();
			npc.getController().onDelete();
			return;
		}
		if(dialogId == 1012 && (npc.getNpcId() == exception_enums.NPC_INSTANCE_KR_BUFF_IV))// Fruit Basket
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
			SkillEngine.getInstance().getSkill(player, 19217, 1, player).useSkill();
			npc.getController().onDelete();
			return;
		}
	}
}