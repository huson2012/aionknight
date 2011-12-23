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

package loginserver.model;

/**
 * This object is storing Account and corresponding to it reconnectionKey for client that will be reconnecting to
 * LoginServer from GameServer using fast reconnect feature
 */
public class ReconnectingAccount
{
	/**
	 * Account object of account that will be reconnecting.
	 */
	private final Account	account;
	/**
	 * Reconnection Key that will be used for authenticating
	 */
	private final int		reconnectionKey;

	/**
	 * Constructor.
	 * 
	 * @param account
	 * @param reconnectionKey
	 */
	public ReconnectingAccount(Account account, int reconnectionKey)
	{
		this.account = account;
		this.reconnectionKey = reconnectionKey;
	}

	/**
	 * Return Account.
	 * 
	 * @return account
	 */
	public Account getAccount()
	{
		return account;
	}

	/**
	 * Return reconnection key for this account
	 * 
	 * @return reconnectionKey
	 */
	public int getReconnectionKey()
	{
		return reconnectionKey;
	}
}