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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.skill.effect.EffectId;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Invis extends AdminCommand
{
	public Invis()
	{
		super("invis");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_INVIS)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}
		if (admin.getVisualState() < 20)
		{
			admin.getEffectController().setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
			admin.setVisualState(CreatureVisualState.HIDE20);
			PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
			PacketSendUtility.sendMessage(admin, "You are now invisible.");
		}
		else
		{
			admin.getEffectController().unsetAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
			admin.unsetVisualState(CreatureVisualState.HIDE20);
			PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
			PacketSendUtility.sendMessage(admin, "You are now visible.");
		}
	}
}