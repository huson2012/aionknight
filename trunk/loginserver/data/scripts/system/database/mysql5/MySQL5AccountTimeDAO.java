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

package mysql5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import loginserver.dao.AccountTimeDAO;
import loginserver.model.AccountTime;
import org.apache.log4j.Logger;
import commons.database.DB;
import commons.database.IUStH;

/**
 * MySQL5 AccountTimeDAO implementation
 */
public class MySQL5AccountTimeDAO extends AccountTimeDAO
{
	/**
	 * Logger
	 */
	private static final Logger	log = Logger.getLogger(MySQL5AccountTimeDAO.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateAccountTime(final int accountId, final AccountTime accountTime)
	{
		return DB.insertUpdate("REPLACE INTO account_time (account_id, last_active, expiration_time, " +
							   "session_duration, accumulated_online, accumulated_rest, penalty_end) values " +
							   "(?,?,?,?,?,?,?)",
							   new IUStH()
		{
			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setLong(1, accountId);
				preparedStatement.setTimestamp(2, accountTime.getLastLoginTime());
				preparedStatement.setTimestamp(3, accountTime.getExpirationTime());
				preparedStatement.setLong(4, accountTime.getSessionDuration());
				preparedStatement.setLong(5, accountTime.getAccumulatedOnlineTime());
				preparedStatement.setLong(6, accountTime.getAccumulatedRestTime());
				preparedStatement.setTimestamp(7, accountTime.getPenaltyEnd());
				preparedStatement.execute();
			}
		});
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public AccountTime getAccountTime(int accountId)
	{
		AccountTime			accountTime = null;
		PreparedStatement	st          = DB.prepareStatement("SELECT * FROM account_time WHERE account_id = ?");

		try
		{
			st.setLong(1, accountId);

			ResultSet	rs = st.executeQuery();

			if(rs.next())
			{
				accountTime = new AccountTime();

				accountTime.setLastLoginTime(rs.getTimestamp("last_active"));
				accountTime.setSessionDuration(rs.getLong("session_duration"));
				accountTime.setAccumulatedOnlineTime(rs.getLong("accumulated_online"));
				accountTime.setAccumulatedRestTime(rs.getLong("accumulated_rest"));
				accountTime.setPenaltyEnd(rs.getTimestamp("penalty_end"));
				accountTime.setExpirationTime(rs.getTimestamp("expiration_time"));
			}
		}
		catch (Exception e)
		{
			log.error("Can't get account time for account with id: " + accountId, e);
		}
		finally
		{
			DB.close(st);
		}

		return accountTime;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String database, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
	}
}
