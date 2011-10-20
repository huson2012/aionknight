package ru.aionknight.gameserver.controllers.instances;


import ru.aionknight.gameserver.ai.events.Event;
import ru.aionknight.gameserver.controllers.NpcController;
import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.EmotionType;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.NpcTemplate;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_EMOTION;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import ru.aionknight.gameserver.services.TeleportService;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.ThreadPoolManager;
import ru.aionknight.gameserver.utils.exceptionhandlers.exception_enums;

/**
 * @author Ritsu, Orpheo
 * 
 */
public class KromedesTrialController extends NpcController
{
	Npc	npc	= getOwner();

	@Override
	public void onDialogRequest(final Player player)
	{
		getOwner().getAi().handleEvent(Event.TALK);

		switch (getOwner().getNpcId()) {
			//Kromede's Library
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