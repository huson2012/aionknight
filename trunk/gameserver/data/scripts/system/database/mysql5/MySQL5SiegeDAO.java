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
import gameserver.dao.SiegeDAO;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeRace;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySQL5SiegeDAO extends SiegeDAO
{
	public static final String SELECT_QUERY = "SELECT `id`, `race`, `legion_id` FROM `siege_locations`";
	public static final String INSERT_QUERY = "INSERT INTO `siege_locations` (`id`, `race`, `legion_id`) VALUES(?, ?, ?)";
	public static final String UPDATE_QUERY = "UPDATE `siege_locations` SET  `race` = ?, `legion_id` = ? WHERE `id` = ?";

	/** Logger */
	private static final Logger					log					= Logger.getLogger(MySQL5PlayerDAO.class);

	@Override
	public boolean loadSiegeLocations(final Map<Integer, SiegeLocation> locations)
	{
		boolean success = true;
		Connection con = null;
		List<Integer> loaded = new ArrayList<Integer>();
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next())
			{
				SiegeLocation loc = locations.get(resultSet.getInt("id"));
				loc.setRace(SiegeRace.valueOf(resultSet.getString("race")));
				loc.setLegionId(resultSet.getInt("legion_id"));
				loaded.add(loc.getLocationId());
			}
			resultSet.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.warn("Error loading Siege informaiton from database: " + e.getMessage(), e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		// Insert locations that are not entered to DB yet.
		for(SiegeLocation sLoc : locations.values())
		{
			if (!loaded.contains(Integer.valueOf(sLoc.getLocationId())))
			{
				insertSiegeLocation(sLoc);
			}
		}
		
		return success;
	}

	/**
	 * @param siegeLocation
	 * @return success
	 */
	public boolean updateSiegeLocation(final SiegeLocation siegeLocation)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, siegeLocation.getRace().toString());
			stmt.setInt(2, siegeLocation.getLegionId());
			stmt.setInt(3, siegeLocation.getLocationId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error update Siege Location: " + siegeLocation.getLocationId() + " to race: " + siegeLocation.getRace().toString(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	/**
	 * @param siegeLocation
	 * @return success
	 */
	private boolean insertSiegeLocation(final SiegeLocation siegeLocation)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, siegeLocation.getLocationId());
			stmt.setString(2, siegeLocation.getRace().toString());
			stmt.setInt(3, siegeLocation.getLegionId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error insert Siege Location: " + siegeLocation.getLocationId(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
	
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
	
	@Override
	public void insertSiegeLogEntry(String legionName, String action, long tstamp, int locationId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO siege_log(legion_name, action, tstamp, siegeloc_id) VALUES (?,?,?,?)");
			stmt.setString(1, legionName);
			stmt.setString(2, action);
			stmt.setLong(3, tstamp);
			stmt.setInt(4, locationId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error storing Abyss Log entry: ",e);
		}
		finally
		{
			DatabaseFactory.close(con);
	
		}
	}
}