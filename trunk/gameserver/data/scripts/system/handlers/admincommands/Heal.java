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
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Heal extends AdminCommand
{
	public Heal()
	{
		super("heal");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_HEAL)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		String syntax = "//heal : Full HP and MP\n" + "//heal dp : Full DP, must be used on a players only !" + "//heal fp";

		VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "No target selected");
			return;
		}

		Creature creature = (Creature) target;

		if (params == null || params.length < 1)
		{
			if (target instanceof Creature)
			{
				creature.getLifeStats().increaseHp(TYPE.HP, creature.getLifeStats().getMaxHp()+1); 
				creature.getLifeStats().increaseMp(TYPE.MP, creature.getLifeStats().getMaxMp()+1);
				PacketSendUtility.sendMessage(admin, creature.getName() + " HP and MP has been fully refreshed !");
			}
		}
		else if (params[0].equals("dp") && target instanceof Player)
		{
			((Player) creature).getCommonData().setDp(creature.getGameStats().getCurrentStat(StatEnum.MAXDP));
			
			PacketSendUtility.sendMessage(admin, creature.getName() + " DP has been fully refreshed !");
		}
		else if (params[0].equals("fp") && target instanceof Player)
		{
			((Player) creature).getLifeStats().setCurrentFp(((Player) creature).getLifeStats().getMaxFp());
			
			PacketSendUtility.sendMessage(admin, creature.getName() + " FP has been fully refreshed !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}
	}
}