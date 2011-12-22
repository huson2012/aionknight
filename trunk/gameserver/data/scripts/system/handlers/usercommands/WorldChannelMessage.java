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

package usercommands;

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.CustomChannel;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

public class WorldChannelMessage extends CustomChannel
{
	public WorldChannelMessage ()
	{
		super(LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_WORLD), Player.CHAT_FIXED_ON_WORLD);
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		if (!CustomConfig.CHANNEL_WORLD_ENABLED)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_WORLD_DISABLED, Player.getChanCommand(Player.CHAT_FIXED_ON_WORLD), Player.getChanCommand(Player.CHAT_FIXED_ON_ELYOS), Player.getChanCommand(Player.CHAT_FIXED_ON_ASMOS)));
			return;
		}
		
		super.executeCommand(player, params);
	}
}