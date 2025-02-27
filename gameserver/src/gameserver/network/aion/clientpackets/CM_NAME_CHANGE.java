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

import commons.database.dao.DAOManager;
import gameserver.dao.LegionDAO;
import gameserver.dao.PlayerAppearanceDAO;
import gameserver.dataholders.DataManager;
import gameserver.itemengine.actions.CosmeticAction;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.templates.item.ItemCategory;
import gameserver.model.templates.preset.PresetTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.LegionService;
import gameserver.services.PlayerService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.Iterator;

public class CM_NAME_CHANGE extends AionClientPacket 
{
	private static final Logger log = Logger.getLogger(CM_NAME_CHANGE.class);

	private int action;
	private int itemObjId;
	private String newName;

	public CM_NAME_CHANGE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		action = readC(); // 0: Change Char Name / 1: Change Legion Name / 2: Change Hair and Skin Color
		readC();
		readH();
		itemObjId = readD();

		if(action != 2)
			newName = readS();
	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		Item ticket = player.getInventory().getItemByObjId(itemObjId);
		if (ticket == null)
			return;

		switch(action)
		{
			// Change Player Name
			case 0:
				if(!PlayerService.isValidName(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400151));
					return;
				}
				if(player.getName().equals(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400153));
					return;
				}
				if(!PlayerService.isFreeName(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400155));
					return;
				}
				if (ticket.getItemTemplate().getItemCategory() != ItemCategory.CHANGE_CHARACTER_NAME)
				{
					log.info("[AUDIT] " + player.getName() + " Trying to change name without ticket.");
					return;
				}
				if(!player.getInventory().removeFromBagByObjectId(itemObjId, 1))
					return;

				player.getCommonData().setName(newName);
				PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
				Iterator<Friend> knownFriends = player.getFriendList().iterator();
				player.getKnownList().doOnAllPlayers(new Executor<Player>(){
					@Override
					public boolean run (Player p)
					{
						PacketSendUtility.sendPacket(p, new SM_PLAYER_INFO(player, player.isEnemyPlayer(p)));
						return true;
					}
					}, true);

				while(knownFriends.hasNext())
				{
					Friend nextObject = knownFriends.next();
					if(nextObject.getPlayer() != null)
					{
						if(nextObject.getPlayer().isOnline())
							PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
					}
				}
				if(player.isLegionMember())
				{
					PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
				}
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400157, newName));

			break;
			// Change Legion Name
			case 1:
				if(!player.isLegionMember())
					return;
				if(!LegionService.getInstance().isValidName(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400152));
					return;
				}
				if(player.getLegion().getLegionName().equals(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400154));
					return;
				}
				if(DAOManager.getDAO(LegionDAO.class).isNameUsed(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400156));
					return;
				}

				if (ticket.getItemTemplate().getItemCategory() != ItemCategory.CHANGE_LEGION_NAME)
				{
					log.info("[AUDIT] " + player.getName() + " Trying to change legion name without ticket.");
					return;
				}
				if(!player.getInventory().removeFromBagByObjectId(itemObjId, 1))
					return;

				LegionService.getInstance().setLegionName(player.getLegion(), newName, true);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400158, newName));

			break;
			// Change Hair, Skin, Voice, Decoration, Tattoo etc
			case 2:
				if (ticket.getItemTemplate().getActions() == null ||
					ticket.getItemTemplate().getActions().getCosmeticActions() == null)
					return;
				
				if(!player.getInventory().removeFromBagByObjectId(itemObjId, 1))
					return;

				CosmeticAction ca = ticket.getItemTemplate().getActions().getCosmeticActions().get(0);

				// first apply preset and then overrides
				if (ca.getPresetName() != null)
				{
					PresetTemplate template = DataManager.CUSTOM_PRESET_DATA.getPresetTemplate(ca.getPresetName());
					if (template == null)
						return;
					if (template.getGender().ordinal() != player.getCommonData().getGender().ordinal() ||
						template.getRace().ordinal() != player.getCommonData().getRace().ordinal())
					{
						log.info("[AUDIT] " + player.getName() + " Trying to change appearance without ticket.");
						return;
					}
					PlayerAppearance.loadDetails(player.getPlayerAppearance(), template.getDetail());
					if (template.getFaceType() > 0)
						player.getPlayerAppearance().setFace(template.getFaceType());
					if (template.getHairType() > 0)
						player.getPlayerAppearance().setHair(template.getHairType());
					if (template.getHairRGB() != null)
						player.getPlayerAppearance().setHairRGB(getDyeColor(template.getHairRGB()));
					if (template.getLipsRGB() != null)
						player.getPlayerAppearance().setLipRGB(getDyeColor(template.getLipsRGB()));
					if (template.getSkinRGB() != null)
						player.getPlayerAppearance().setSkinRGB(getDyeColor(template.getSkinRGB()));
					if (template.getHeight() > 0)
						player.getPlayerAppearance().setHeight(template.getHeight());
				}
				
				if (ca.getEyesColor() != null)
					player.getPlayerAppearance().setEyeRGB(getDyeColor(ca.getEyesColor()));
				if (ca.getFaceColor() != null)
					player.getPlayerAppearance().setSkinRGB(getDyeColor(ca.getFaceColor()));
				if (ca.getHairColor() != null)
					player.getPlayerAppearance().setHairRGB(getDyeColor(ca.getHairColor()));
				if (ca.getLipsColor() != null)
					player.getPlayerAppearance().setLipRGB(getDyeColor(ca.getLipsColor()));
				if (ca.getFaceType() > 0)
					player.getPlayerAppearance().setFace(ca.getFaceType());
				if (ca.getHairType() > 0)
					player.getPlayerAppearance().setHair(ca.getHairType());
				if (ca.getMakeupType() > 0)
					player.getPlayerAppearance().setDecoration(ca.getMakeupType());
				if (ca.getTattooType() > 0)
					player.getPlayerAppearance().setTattoo(ca.getTattooType());
				if (ca.getVoiceType() > 0)
					player.getPlayerAppearance().setVoice(ca.getVoiceType());

				DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);

				player.clearKnownlist();
				PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
				player.updateKnownlist();
			break;
		}
	}

	/**
	 * @param color
	 * @return integer value in BGR
	 */
	private int getDyeColor(String hexRGB)
	{
		int rgb = Integer.parseInt(hexRGB, 16);
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = (rgb >> 0) & 0xFF;

		return (blue << 16) | (green << 8) | (red << 0);
	}
}
