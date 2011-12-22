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

package chatserver.service;

import chatserver.configs.Config;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.netty.handler.GameChannelHandler;

public class GameServerService
{
	public static byte GAMESERVER_ID;
	/**
	 * @param gameChannelHandler
	 * @param gameServerId
	 * @param defaultAddress
	 * @param password
	 * @return
	 */
	public static GsAuthResponse registerGameServer(GameChannelHandler gameChannelHandler, byte gameServerId,
		byte[] defaultAddress, String password)
	{
		GAMESERVER_ID = gameServerId;
		return passwordConfigAuth(password);
	}

	/**
	 * @return
	 */
	private static GsAuthResponse passwordConfigAuth(String password)
	{
		if (password.equals(Config.GAME_SERVER_PASSWORD))
			return GsAuthResponse.AUTHED;

		return GsAuthResponse.NOT_AUTHED;
	}
}