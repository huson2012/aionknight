package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.BannedMacManager;
import gameserver.network.loginserver.LsClientPacket;

/**
 * @author drsaluml
 * @author Rossdale
 *
 */
public class CM_MACBAN_REQUEST_MAC extends LsClientPacket {
	
    /**
	 * @param opcode
	 */
	public CM_MACBAN_REQUEST_MAC(int opcode)
	{
		super(opcode);
	}
    
    private String macAddress;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
    	macAddress = readS();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
    	BannedMacManager.getInstance().setMacAddress(macAddress);
	}
}
