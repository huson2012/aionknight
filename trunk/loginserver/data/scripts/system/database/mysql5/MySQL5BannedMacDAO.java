package mysql5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import commons.database.DB;
import loginserver.dao.BannedMacDAO;
import loginserver.model.BannedMacEntry;

/**
 * 
 * @author KID
 * @author Rossdale
 *
 */
public class MySQL5BannedMacDAO extends BannedMacDAO {

    private static final Logger    log = Logger.getLogger(MySQL5BannedMacDAO.class);

	@Override
	public Map<String, BannedMacEntry> load() {
		Map<String, BannedMacEntry> map = new FastMap<String, BannedMacEntry>();
		PreparedStatement ps = DB.prepareStatement("SELECT `address`,`playerId`,`time`,`details` FROM `banned_mac`");
		try {
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				String address = rs.getString("address");
				map.put(address, new BannedMacEntry(address,rs.getInt("playerId"), rs.getTimestamp("time"), rs.getString("details")));
			}
		}
		catch (SQLException e) {
			log.error("Error loading last saved server time", e);
		}
		finally {
			DB.close(ps);
		}
		return map;
	}

	@Override
	public boolean update(BannedMacEntry entry) {
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("REPLACE INTO `banned_mac` (`address`,`playerId`,`time`,`details`) VALUES (?,?,?,?)");
		try {
			ps.setString(1, entry.getMac());
			ps.setInt(2, entry.getPlayerId());
			ps.setTimestamp(3, entry.getTime());
			ps.setString(4, entry.getDetails());
			success = ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			log.error("Error storing BannedMacEntry "+entry.getMac(), e);
		}
		finally {
			DB.close(ps);
		}

		return success;
	}
	
	@Override
	public boolean remove(String address) {
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("DELETE FROM `banned_mac` WHERE address=?");
		try {
			ps.setString(1, address);
			success = ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			log.error("Error removing BannedMacEntry "+address, e);
		}
		finally {
			DB.close(ps);
		}

		return success;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMacAddress(final int playerId) {
        String macAddress = "";
        PreparedStatement st = DB.prepareStatement("SELECT `address` FROM `banned_mac` WHERE `playerId` = ?");

        try {
            st.setInt(1, playerId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                macAddress = rs.getString("address");
            }
        } catch (Exception e) {
            log.error("Can't select last IP of account ID: " + playerId, e);
            return "";
        } finally {
            DB.close(st);
        }

        return macAddress;
    }

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
