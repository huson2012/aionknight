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
import gameserver.network.aion.serverpackets.SM_TRANSFORM;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Morph extends AdminCommand
{

	public Morph()
	{
		super("morph");
	}
   
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MORPH)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params == null || params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //morph <npc id | cancel> ");
			return;
		}

		Player target = admin;
		int param = 0;

		if (admin.getTarget() instanceof Player)
			target = (Player)admin.getTarget();

		if (!("cancel").startsWith(params[0].toLowerCase()))
		{
			try
			{
				param = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Parameter must be an integer, or cancel.");
				return;
			}
		}

		if ((param != 0 && param < 200000) || param > 298021)
		{
			PacketSendUtility.sendMessage(admin, "Something wrong with the NPC Id!");
			return;
		}

		target.setTransformedModelId(param);
		PacketSendUtility.broadcastPacketAndReceive(target, new SM_TRANSFORM(target));

		if (param == 0)
		{
			if (target.equals(admin))
			{
				PacketSendUtility.sendMessage(target, "Morph cancelled successfully.");
			}
			else
			{
				PacketSendUtility.sendMessage(target,"Your morph has been cancelled by Admin " + admin.getName() + ".");
				PacketSendUtility.sendMessage(admin, "You have cancelled " + target.getName() + "'s morph.");
			}
		}
		else
		{
			if (target.equals(admin))
			{
				PacketSendUtility.sendMessage(target, "Successfully morphed to npc id " + param + ".");
			}
			else
			{
				PacketSendUtility.sendMessage(target, admin.getName() + " morphs you into an NPC form.");
				PacketSendUtility.sendMessage(admin, "You morph " + target.getName() + " to npc id " + param + ".");
			}
			
		}
	}
}