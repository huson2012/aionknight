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

package mysql5;

import commons.database.DatabaseFactory;
import gameserver.dao.PurchaseLimitDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PurchaseLimit;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class MySQL5PurchaseLimitDAO extends PurchaseLimitDAO
{
	public static final String SELECT_QUERY = "SELECT itemId, itemCount FROM purchase_limit WHERE player_id = ?";
	public static final String INSERT_QUERY	= "INSERT INTO purchase_limit (player_id, itemId, itemCount) VALUES (?, ?, ?)";
	public static final String DELETE_QUERY = "DELETE FROM purchase_limit WHERE player_id = ? ";
	public static final String DELETE_ALL_QUERY = "DELETE FROM purchase_limit";
	public static final String SELECT_COUNT_ITEM = "SELECT sum(itemCount) as count FROM purchase_limit WHERE itemId = ?";
	/**
	 * Logger for this class.
	 */
	private static final Logger	log				= Logger.getLogger(MySQL5AbyssRankDAO.class);

	
	@Override
	public void loadPurchaseLimit(final Player player)
	{
		LinkedHashMap<Integer, Integer> purchaseLimitList = new LinkedHashMap<Integer, Integer>();
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
					
			stmt.setInt(1, player.getObjectId());
					
			ResultSet rs = stmt.executeQuery();
					
			while(rs.next())
		 	{
				int itemId = rs.getInt("itemId");
				int itemCount = rs.getInt("itemCount");

				purchaseLimitList.put(itemId, itemCount);
		 	}

			PurchaseLimit purchaseLimit = new PurchaseLimit();
			purchaseLimit.setItems(purchaseLimitList);

			player.setPurchaseLimit(purchaseLimit);

			rs.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public int loadCountItem(int itemId)
	{
		int count = 0;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_COUNT_ITEM);

			stmt.setInt(1, itemId);

			ResultSet rs = stmt.executeQuery();

			if(rs.next())
		 	{
				count = rs.getInt("count");
		 	}

			rs.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
			return 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return count;
	}

	@Override
	public void savePurchaseLimit(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			LinkedHashMap<Integer, Integer> items = player.getPurchaseLimit().getItems();

			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.execute();
			stmt.close();


			for(int itemId : items.keySet())
			{
				stmt = con.prepareStatement(INSERT_QUERY);
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, itemId);
				stmt.setLong(3, items.get(itemId));
				stmt.execute();
				stmt.close();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void deleteAllPurchaseLimit()
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_ALL_QUERY);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}