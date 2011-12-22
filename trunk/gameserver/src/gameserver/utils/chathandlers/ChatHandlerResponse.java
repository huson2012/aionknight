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

/**
 * Every {@link ChatHandler} as a result returns object of ChatHandlerResponse type. Objects of this class contains dual
 * information:
 * <ul>
 * <li>(maybe) Transformed message in accessible by {@link #getMessage()}</li>
 * <li>information whether handler blocked this message (it means, that it won't be sent to client(s)</li>
 * </ul>
 */
public class ChatHandlerResponse
{
	/** Single instance of <tt>ChatHandlerResponse</tt> representing response with blocked message */
	public static final ChatHandlerResponse	BLOCKED_MESSAGE	= new ChatHandlerResponse(true, "");

	private boolean							messageBlocked;
	private String							message;

	/**
	 * 
	 * @param messageBlocked
	 * @param message
	 */
	public ChatHandlerResponse(boolean messageBlocked, String message)
	{
		this.messageBlocked = messageBlocked;
		this.message = message;
	}

	/**
	 * A message (maybe) changed by handler.
	 * 
	 * @return a message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * 
	 * @return if true, it means that handler blocked sending this message to client.
	 */
	public boolean isBlocked()
	{
		return messageBlocked;
	}
}
