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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.legion.Legion;
import gameserver.services.LegionService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Spy extends AdminCommand
{
	public Spy()
	{
		super("spy");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < 2)
		{
			PacketSendUtility.sendMessage(admin, "Not authorized");
			return;
		}

		if(params.length != 2)
		{
			syntax(admin);
			return;
		}

		if(params[1].startsWith("L"))
		{
			try
			{
				int legionId = Integer.parseInt(params[1].replace("L", ""));
				Legion legion = LegionService.getInstance().getLegion(legionId);
				if(legion == null)
					throw new Exception("no such legion.");
				
				if(params[0].equals("start"))
				{
					if(!admin.spyedLegions.contains(legionId))
						admin.spyedLegions.add(legionId);
				}
				else if(params[0].equals("stop"))
				{
					if(admin.spyedLegions.contains(legionId))
						admin.spyedLegions.remove(new Integer(legionId));
				}
				else
					syntax(admin);
			}
			catch(Exception e)
			{
				PacketSendUtility.sendMessage(admin, "no such legion.");
				return;
			}
		}
		else if(params[1].startsWith("G"))
		{
			Player target = World.getInstance().findPlayer(params[1].substring(1));
			if(target == null)
			{
				PacketSendUtility.sendMessage(admin, "target player not found.");
				return;
			}
			if(target.getPlayerGroup() == null)
			{
				PacketSendUtility.sendMessage(admin, "target player not in group.");
				return;
			}
			
			if(params[0].equals("start"))
			{
				if(!admin.spyedGroups.contains(target.getPlayerGroup().getGroupId()))
					admin.spyedGroups.add(target.getPlayerGroup().getGroupId());
			}
			else if(params[0].equals("stop"))
			{
				if(admin.spyedGroups.contains(target.getPlayerGroup().getGroupId()))
					admin.spyedGroups.remove(new Integer(target.getPlayerGroup().getGroupId()));
			}
			else
				syntax(admin);
			
		}

	}

	private void syntax(Player admin)
	{
		PacketSendUtility.sendMessage(admin, "Syntax: //spy <start|stop> <legionName>");
	}
}