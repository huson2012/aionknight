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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import loginserver.dao.BannedIpDAO;
import loginserver.model.BannedIP;
import commons.database.DB;
import commons.database.IUStH;
import commons.database.ParamReadStH;
import commons.database.ReadStH;

/**
 * BannedIP DAO implementation for MySQL5
 */
public class MySQL5BannedIpDAO extends BannedIpDAO
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BannedIP insert(String mask)
	{
		return insert(mask, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BannedIP insert(final String mask, final Timestamp expireTime)
	{
		BannedIP result = new BannedIP();
		result.setMask(mask);
		result.setTimeEnd(expireTime);

		if (insert(result))
			return result;
		else
			return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean insert(final BannedIP bannedIP)
	{
		boolean insert = DB.insertUpdate("INSERT INTO banned_ip(mask, time_end) VALUES (?, ?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, bannedIP.getMask());
				if (bannedIP.getTimeEnd() == null)
					preparedStatement.setNull(2, Types.TIMESTAMP);
				else
					preparedStatement.setTimestamp(2, bannedIP.getTimeEnd());
				preparedStatement.execute();
			}
		});

		if (!insert)
			return false;

		final BannedIP result = new BannedIP();
		DB.select("SELECT * FROM banned_ip WHERE mask = ?", new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, bannedIP.getMask());
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException
			{
				resultSet.next(); // mask is unique, only one result allowed
				result.setId(resultSet.getInt("id"));
				result.setMask(resultSet.getString("mask"));
				result.setTimeEnd(resultSet.getTimestamp("time_end"));
			}
		});
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(final BannedIP bannedIP)
	{
		return DB.insertUpdate("UPDATE banned_ip SET mask = ?, time_end = ? WHERE id = ?", new IUStH() {
			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, bannedIP.getMask());
				if (bannedIP.getTimeEnd() == null)
					preparedStatement.setNull(2, Types.TIMESTAMP);
				else
					preparedStatement.setTimestamp(2, bannedIP.getTimeEnd());
				preparedStatement.setInt(3, bannedIP.getId());
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final String mask)
	{
		return DB.insertUpdate("DELETE FROM banned_ip WHERE mask = ?", new IUStH() {
			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, mask);
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final BannedIP bannedIP)
	{
		return DB.insertUpdate("DELETE FROM banned_ip WHERE mask = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				// Changed from id to mask because we don't get id of last inserted ban
				preparedStatement.setString(1, bannedIP.getMask());
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<BannedIP> getAllBans()
	{

		final Set<BannedIP> result = new HashSet<BannedIP>();
		DB.select("SELECT * FROM banned_ip", new ReadStH() {
			@Override
			public void handleRead(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					BannedIP ip = new BannedIP();
					ip.setId(resultSet.getInt("id"));
					ip.setMask(resultSet.getString("mask"));
					ip.setTimeEnd(resultSet.getTimestamp("time_end"));
					result.add(ip);
				}
			}
		});
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}