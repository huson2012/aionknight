/**
*  This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
*
* Aion-Knight is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Aion-Knight is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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