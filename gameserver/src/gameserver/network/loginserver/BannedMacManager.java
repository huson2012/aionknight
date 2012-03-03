package gameserver.network.loginserver;

import java.sql.Timestamp;
import java.util.Map;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.serverpackets.SM_MACBAN_CONTROL;
import gameserver.network.loginserver.serverpackets.SM_REQUEST_MAC;

/**
 * 
 * @author KID
 * @author drsaluml
 * @author Rossdale
 * 
 */
public class BannedMacManager
{
	/**
	 * logger for this class
	 */
	private static final Logger	log	= Logger.getLogger(BannedMacManager.class);
	private static BannedMacManager manager = new BannedMacManager();

	public static BannedMacManager getInstance() {
		return manager;
	}

	private Map<String, BannedMacEntry> bannedList = new FastMap<String, BannedMacEntry>();
	private String	macAddress;

	public final void banAddress(String address,int playerId, long newTime, String details) {
		if(playerId > 0)
		{
			BannedMacEntry entry;
			if (bannedList.containsKey(address)) {
				if (bannedList.get(address).isActiveTill(newTime)) {
					return;
				} else {
					entry = bannedList.get(address);
					entry.updateTime(newTime);
				}
			} else
				entry = new BannedMacEntry(address, newTime);
	
			entry.setDetails(details);
			
			bannedList.put(address, entry);
			
			log.info("banned "+address+" to "+entry.getTime().toString()+" for "+details);
			LoginServer.getInstance().sendBanMacPacket(new SM_MACBAN_CONTROL((byte)1,playerId, address, newTime, details));
		}
	}

	public final boolean unbanAddress(String address,int playerId, String details) {
		if(playerId <= 0)
			return false;
		
		if (bannedList.containsKey(address)) {
			bannedList.remove(address);
			log.info("unbanned "+address+" for "+details);
			LoginServer.getInstance().sendBanMacPacket(new SM_MACBAN_CONTROL((byte)0,playerId, address, 0, details));
			return true;
		}
		else
			return false;
	}

	public final boolean isBanned(String address) {
		if (bannedList.containsKey(address))
			return this.bannedList.get(address).isActive();
		else
			return false;
	}

	public final void dbLoad(String address, long time, String details) {
		this.bannedList.put(address, new BannedMacEntry(address, new Timestamp(time), details));
	}

	public void onEnd() {
		log.info("Loaded "+this.bannedList.size()+" banned mac addesses");
	}
	
	public final String loadMacAddress() {
		return macAddress;
	}
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public final void getMacAddress(int playerId) {
		LoginServer.getInstance().sendBanMacPacket(new SM_REQUEST_MAC(1,playerId));
	}

	public final void reload() {
		LoginServer.getInstance().sendBanMacPacket(new SM_REQUEST_MAC(2,0));
	}
}
