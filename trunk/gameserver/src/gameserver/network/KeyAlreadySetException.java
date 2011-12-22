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

package gameserver.network;

@SuppressWarnings("serial")
public class KeyAlreadySetException extends RuntimeException
{
	/**
	 * Constructs an <code>KeyAlreadySetException</code> with no detail message.
	 */
	public KeyAlreadySetException()
	{
		super();
	}

	/**
	 * Constructs an <code>KeyAlreadySetException</code> with the specified detail message.
	 * 
	 * @param s
	 *           the detail message.
	 */
	public KeyAlreadySetException(String s)
	{
		super(s);
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *           exception description
	 * @param cause
	 *           reason of this exception
	 */
	public KeyAlreadySetException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates new error
	 * 
	 * @param cause
	 *           reason of this exception
	 */
	public KeyAlreadySetException(Throwable cause)
	{
		super(cause);
	}
}