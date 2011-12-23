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

package loginserver.controller;

import java.sql.Timestamp;
import loginserver.dao.AccountTimeDAO;
import loginserver.model.Account;
import loginserver.model.AccountTime;
import commons.database.dao.DAOManager;

/**
 * This class is for account time controlling.
 * When character logins any server, it should get
 * its day online time and rest time. Some aion ingame
 * feautres also depend on player's online time
 */
public class AccountTimeController
{
	/**
	 * Update account time when character logins.
	 * The following field are being updated:
	 * - LastLoginTime (set to CurrentTime)
	 * - RestTime (set to (RestTime + (CurrentTime-LastLoginTime - SessionDuration))
	 * @param account
	 */
	public static void updateOnLogin(Account account)
	{
		AccountTime	accountTime = account.getAccountTime();

		/**
		 * It seems the account was just created, so new accountTime
		 * should be created too
		 */
		if(accountTime == null)
		{
			accountTime = new AccountTime();
		}

		int	lastLoginDay = getDays(accountTime.getLastLoginTime().getTime());
		int	currentDay   = getDays(System.currentTimeMillis());

		/**
		 * The character from that account was online not today, so it's account timings
		 * should be nulled.
		 */
		if(lastLoginDay < currentDay)
		{
			accountTime.setAccumulatedOnlineTime(0);
			accountTime.setAccumulatedRestTime(0);
		}
		else
		{
			long	restTime = System.currentTimeMillis() - accountTime.getLastLoginTime().getTime() -
							   accountTime.getSessionDuration();

			accountTime.setAccumulatedRestTime(accountTime.getAccumulatedRestTime() + restTime);

		}

		accountTime.setLastLoginTime(new Timestamp(System.currentTimeMillis()));

		DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(account.getId(), accountTime);
		account.setAccountTime(accountTime);
	}

	/**
	 * Update account time when character logouts.
	 * The following field are being updated:
	 * - SessionTime (set to CurrentTime - LastLoginTime)
	 * - AccumulatedOnlineTime (set to AccumulatedOnlineTime + SessionTime)
	 * @param account
	 */
	public static void updateOnLogout(Account account)
	{
		AccountTime	accountTime = account.getAccountTime();

		accountTime.setSessionDuration(System.currentTimeMillis() - accountTime.getLastLoginTime().getTime());
		accountTime.setAccumulatedOnlineTime(accountTime.getAccumulatedOnlineTime() + accountTime.getSessionDuration());
		DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(account.getId(), accountTime);
		account.setAccountTime(accountTime);
	}

	/**
	 * Checks if account is already expired or not
	 * @param account
	 * @return true, if account is expired, false otherwise
	 */
	public static boolean isAccountExpired(Account account)
	{
		AccountTime	accountTime = account.getAccountTime();

		return accountTime != null && accountTime.getExpirationTime() != null &&
			   accountTime.getExpirationTime().getTime() < System.currentTimeMillis();
	}

	/**
	 * Checks if account is restricted by penalty or not
	 * @param account
	 * @return true, is penalty is active, false otherwise
	 */
	public static boolean isAccountPenaltyActive(Account account)
	{
		AccountTime	accountTime = account.getAccountTime();

		// 1000 is 'infinity' value
		return accountTime != null && accountTime.getPenaltyEnd() != null &&
			  (accountTime.getPenaltyEnd().getTime() == 1000 ||
			   accountTime.getPenaltyEnd().getTime() >= System.currentTimeMillis());
	}

	/**
	 * Get days from time presented in milliseconds
	 * @param millis time in ms
	 * @return  days
	 */
	public static int getDays(long millis)
	{
		return (int) (millis / 1000 / 3600 / 24);
	}
}