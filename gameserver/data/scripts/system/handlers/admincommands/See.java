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
import gameserver.model.gameobjects.state.CreatureSeeState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class See extends AdminCommand
{
	public See()
	{
		super("see");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_SEE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}
		if (params.length == 1)
		{
			int see;
			try 
			{
				see = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Syntax: //see <0|1|2|20>");
				return;
			}

			Player target = null;
		
			if (admin.getTarget() != null && admin.getTarget() instanceof Player)
				target = (Player)admin.getTarget();
			else
				target = admin;
		
			CreatureSeeState seeState;
				
			switch(see)
			{
				case 1:
					seeState = CreatureSeeState.SEARCH1;
					break;
				case 2:
					seeState = CreatureSeeState.SEARCH2;
					break;
				case 20:
					seeState = CreatureSeeState.SEARCH20;
					break;
				default:
					seeState = CreatureSeeState.NORMAL;
					break;
			}
				
			//reset see state
			for(CreatureSeeState saw : CreatureSeeState.values())
			{
				if (target.isInSeeState(saw))
					target.unsetSeeState(saw);
			}
				
			target.setSeeState(seeState);

			PacketSendUtility.broadcastPacket(target, new SM_PLAYER_STATE(target), true);
			PacketSendUtility.sendMessage(target, "Your SeeState level was changed to "+see);
			
		}
		else
			PacketSendUtility.sendMessage(admin, "Syntax: //see <0|1|2|20>");
	}
}