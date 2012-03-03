package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.BannedMacManager;
import gameserver.network.loginserver.LsClientPacket;

/**
 * 
 * @author KID
 * @author drsaluml
 * @author Rossdale
 *
 */
public class CM_MACBAN_LIST extends LsClientPacket {

	public CM_MACBAN_LIST(int opCode) {
		super(opCode);
	}

	@Override
	protected void readImpl() {
		BannedMacManager bmm = BannedMacManager.getInstance();
		int cnt = readD();
		for(int a = 0; a < cnt; a++) {
			bmm.dbLoad(readS(), readQ(), readS());
		}
		
		bmm.onEnd();
	}

	@Override
	protected void runImpl() {
		// ?
	}
}
