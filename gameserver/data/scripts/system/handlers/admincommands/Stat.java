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
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Stat extends AdminCommand
{
	public Stat()
	{
		super("status");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_STAT)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (admin.getTarget() == null)
		{
			PacketSendUtility.sendMessage(admin, "You have to select a target");
			return;
		}

		VisibleObject target = admin.getTarget();

		if (!(target instanceof Creature))
		{
			PacketSendUtility.sendMessage(admin, "Your target is not a Creature");
			return;
		}

		Creature cTarget = (Creature)target;

		PacketSendUtility.sendMessage(admin, ">>> Stats information about "+cTarget.getClass().getSimpleName()+" \""+cTarget.getName()+"\"");
		if (cTarget.getGameStats() != null)
		{
			CreatureGameStats<?> cgs = cTarget.getGameStats();
			for (int i=0; i<StatEnum.values().length; i++)
			{
				if (cgs.getCurrentStat(StatEnum.values()[i])!=0)
				{
					PacketSendUtility.sendMessage(admin, StatEnum.values()[i]+": "+cgs.getBaseStat(StatEnum.values()[i])+" ("+cgs.getStatBonus(StatEnum.values()[i])+")");
				}
			}
		}
		PacketSendUtility.sendMessage(admin, ">>> End of stats information");
	}
}