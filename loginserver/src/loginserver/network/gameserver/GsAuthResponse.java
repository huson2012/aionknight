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

package loginserver.network.gameserver;

/**
 * This class contains possible response that LoginServer may send to gameserver if authentication fail etc.
 */
public enum GsAuthResponse
{
	/**
	 * Everything is OK
	 */
	AUTHED(0),
	/**
	 * Password/IP etc does not match.
	 */
	NOT_AUTHED(1),
	/**
	 * Requested id is not free
	 */
	ALREADY_REGISTERED(2);

	/**
	 * id of this enum that may be sent to client
	 */
	private byte	responseId;

	/**
	 * Constructor.
	 * 
	 * @param responseId
	 *           id of the message
	 */
	private GsAuthResponse(int responseId)
	{
		this.responseId = (byte) responseId;
	}

	/**
	 * Message Id that may be sent to client.
	 * 
	 * @return message id
	 */
	public byte getResponseId()
	{
		return responseId;
	}
}
