/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

import commons.database.DB;
import commons.database.IUStH;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Команда для добавления необходимого дропа в БД.
 */
 
public class AddDrop extends AdminCommand
{
	public AddDrop()
	{
		super("adddrop");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDDROP)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if (params.length != 5)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDDROP_SYNTAX));
			return;
		}

		try
		{
			final int mobId = Integer.parseInt(params[0]);
			final int itemId = Integer.parseInt(params[1]);
			final int min = Integer.parseInt(params[2]);
			final int max = Integer.parseInt(params[3]);
			final int chance = Integer.parseInt(params[4]);

			DropList dropList = DropService.getInstance().getDropList();

			DropTemplate dropTemplate = new DropTemplate(mobId, itemId, min, max, chance);
			dropList.addDropTemplate(mobId, dropTemplate);

			DB.insertUpdate("INSERT INTO droplist ("
				+ "`mobId`, `itemId`, `min`, `max`, `chance`)" + " VALUES "
				+ "(?, ?, ?, ?, ?)", new IUStH() 
				{
					@Override
					public void handleInsertUpdate(PreparedStatement ps) throws SQLException
					{
						ps.setInt(1, mobId);
						ps.setInt(2, itemId);
						ps.setInt(3, min);
						ps.setInt(4, max);
						ps.setInt(5, chance);
						ps.execute();
					}
				});
		}
		catch(Exception ex)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETERS_ONLY));
			return;
		}
	}
}