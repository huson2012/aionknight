package loginserver.dao;

import java.util.Map;

import commons.database.dao.DAO;
import loginserver.model.BannedMacEntry;

/**
 * 
 * @author KID
 *
 */
public abstract class BannedMacDAO implements DAO {
	public abstract boolean update(BannedMacEntry entry);
	
	public abstract boolean remove(String address);
	
	public abstract Map<String, BannedMacEntry> load(); 

	/**
	 * @param playerId
	 */
	public abstract String getMacAddress(int playerId);
	
	@Override
	public final String getClassName() {
		return BannedMacDAO.class.getName();
	}
}
