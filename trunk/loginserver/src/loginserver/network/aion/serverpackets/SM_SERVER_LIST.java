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

package loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;

public class SM_SERVER_LIST extends AionServerPacket
{
	/**
	 * Logger for this class.
	 */
	protected static Logger	log	= Logger.getLogger(SM_SERVER_LIST.class);

	/**
	 * Constructs new instance of <tt>SM_SERVER_LIST</tt> packet.
	 */
	public SM_SERVER_LIST()
	{
		super(0x04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		Collection<GameServerInfo> servers = GameServerTable.getGameServers();
		Map<Integer, Integer> charactersCountOnServer = null;
		
		int accountId = con.getAccount().getId();
		int maxId = 0;
		int accessLvl;
		
		charactersCountOnServer = AccountController.getCharacterCountsFor(accountId);
		
		writeC(buf, getOpcode());
		writeC(buf, servers.size());// servers
		writeC(buf, con.getAccount().getLastServer());// last server
		for (GameServerInfo gsi : servers)
		{
			accessLvl = (int)(con.getAccount().getAccessLevel());
			if (gsi.getId()>maxId)
			{
				maxId = gsi.getId();
			}
			writeC(buf, gsi.getId());// server id
			writeB(buf, gsi.getIPAddressForPlayer(con.getIP())); // server IP
			writeD(buf, gsi.getPort());// port
			writeC(buf, 0x00); // age limit
			writeC(buf, 0x01);// pvp=1
			writeH(buf, gsi.getCurrentPlayers());// currentPlayers
			writeH(buf, gsi.canAccess(accessLvl) ? gsi.getMaxPlayers() : 0);// maxPlayers
			writeC(buf, gsi.canAccess(accessLvl) ? (gsi.isOnline() ? 1 : 0) : 0);// ServerStatus, up=1
			writeD(buf, gsi.canAccess(accessLvl) ? 1 : 0);// bits);
			writeC(buf, 1);// server.brackets ? 0x01 : 0x00);
		}
		
		writeH(buf, maxId+1);
		writeC(buf, 0x01); // 0x01 for autoconnect
		
		for (int i = 1; i <= maxId; i++)
		{
			if (charactersCountOnServer.containsKey(i))
			{
				writeC(buf, charactersCountOnServer.get(i));
			}
			else
			{
				writeC(buf, 0);
			}
		}
	}
}
