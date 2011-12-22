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

package gameserver.utils.chathandlers;

import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.Player;

/**
 * ChatHandler is called every time when player is trying to send a message using chat. ChatHandler can decide whether
 * message should be send later to players (i.e. admin command handler will block it) and can also change the content of
 * the message ( for example censor may put *** in place of vulgar words)
 */
public interface ChatHandler
{
	/**
	 * This method may check content of message and take proper actions based on it. The message can be changed and also
	 * blocked to forwarding to players.
	 * 
	 * @param chatType
	 * @param message
	 * @param sender
	 * @return response {@link ChatHandlerResponse}
	 */
	public ChatHandlerResponse handleChatMessage(ChatType chatType, String message, Player sender);
}
