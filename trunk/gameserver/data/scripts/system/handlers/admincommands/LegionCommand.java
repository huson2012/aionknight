/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.legion.Legion;
import gameserver.services.LegionService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class LegionCommand extends AdminCommand
{

	/**
	 * The constructor of Legion Command
	 */
	public LegionCommand()
	{
		super("legion");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_LEGION)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin,
				"syntax //legion <disband | setlevel | setpoints | setname <legion name> <value>");
			return;
		}
		LegionService legionService = LegionService.getInstance();
		
		int legionId = 0;

		try
		{
			legionId = Integer.valueOf(params[1]);
		}
		catch(NumberFormatException e){
			legionId = 0;
		}

		Legion legion;

		if(legionId == 0)
			legion = legionService.getLegion(params[1].toLowerCase());
		else
			legion = legionService.getLegion(legionId);

		if (legion == null)
		{
			PacketSendUtility.sendMessage(admin, "The " + params[1].toLowerCase() + " legion does not exist.");
			return;
		}

		if (params[0].toLowerCase().equals("disband"))
		{
			legionService.disbandLegion(legion);
			PacketSendUtility.sendMessage(admin, "The following legion has been disbanded: " + legion.getLegionName());
		}
		else if (params[0].toLowerCase().equals("setlevel"))
		{
			int newLevel = Integer.parseInt(params[2]);

			if (newLevel < 1 || newLevel > 5)
			{
				PacketSendUtility.sendMessage(admin, "Please use a valid legion level. (1 - 5)");
				return;
			}
			else if (legion.getLegionLevel() == newLevel)
			{
				PacketSendUtility.sendMessage(admin, "Level of legion already is " + newLevel);
				return;
			}
			legionService.changeLevel(legion, newLevel, true);
			PacketSendUtility.sendMessage(admin, "The " + legion.getLegionName() + " legion has been leveled up to level "
				+ newLevel);
		}
		else if (params[0].toLowerCase().equals("setpoints"))
		{
			int newPoints = Integer.parseInt(params[2]);

			if (newPoints <= 0 || newPoints > 2000000000)
			{
				PacketSendUtility.sendMessage(admin, "Please use valid points amount. (0 - 2.000.000.000)");
				return;
			}
			else if (legion.getContributionPoints() == newPoints)
			{
				PacketSendUtility.sendMessage(admin, "Contribution Points of legion already is " + newPoints);
				return;
			}
			legionService.setContributionPoints(legion, newPoints, true);
			PacketSendUtility.sendMessage(admin, "The " + legion.getLegionName()
				+ " legion points have been changed to " + newPoints);
		}
		else if (params[0].toLowerCase().equals("setname"))
		{
			String newLegionName = params[2];

			if (!legionService.isValidName(newLegionName))
			{
				PacketSendUtility.sendMessage(admin, "Please use a valid legion name!");
				return;
			}
			else if (legion.getLegionName().toLowerCase() == newLegionName.toLowerCase())
			{
				PacketSendUtility.sendMessage(admin, "Name of legion already is " + newLegionName);
				return;
			}
			legionService.setLegionName(legion, newLegionName, true);
			PacketSendUtility.sendMessage(admin, "The " + legion.getLegionName()
				+ " legion's name has been changed to " + newLegionName);
		}
	}
}