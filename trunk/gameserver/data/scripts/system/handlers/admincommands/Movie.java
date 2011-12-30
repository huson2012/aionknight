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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Movie extends AdminCommand
{
	public Movie()
	{
		super("movie");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVIE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		int movieId = 0;
		int type = 0;
		VisibleObject target = admin.getTarget();

		if (target == null || !(target instanceof Player))
		{
			target = admin;
		}
		if (params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			return;
		}
		if (params.length == 1)
		{
			try
			{
				movieId = Integer.valueOf(params[0]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(0, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}
		else if (params.length == 2)
		{
			try
			{
				type = Integer.valueOf(params[0]);
				movieId = Integer.valueOf(params[1]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(type, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}

	}
}