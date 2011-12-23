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

package loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import loginserver.GameServerTable;
import loginserver.dao.GameServersDAO;
import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsConnection.State;
import loginserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import commons.database.dao.DAOManager;
import commons.network.IPRange;

/**
 * This is authentication packet that gs will send to login server for registration.
 */
public class CM_GS_AUTH extends GsClientPacket
{
	/**
	 * Password for authentication
	 */
	private String		password;

	/**
	 * Id of GameServer
	 */
	private byte			gameServerId;

	/**
	 * Maximum number of players that this Gameserver can accept.
	 */
	private int			maxPlayers;
	
	/**
	 * Required access level to login
	 */
	private int			requiredAccess;
	
	/**
	 * Port of this Gameserver.
	 */
	private int			port;

	/**
	 * Default address for server
	 */
	private byte[]		defaultAddress;

	/**
	 * List of IPRanges for this gameServer
	 */
	private List<IPRange>	ipRanges;

	/**
	 * Constructor.
	 * 
	 * @param buf
	 * @param client
	 */
	public CM_GS_AUTH(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x00);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		gameServerId = (byte) readC();

		defaultAddress = readB(readC());
		int size = readD();
		ipRanges = new ArrayList<IPRange>(size);
		for (int i = 0; i < size; i++)
		{
			ipRanges.add(new IPRange(readB(readC()), readB(readC()), readB(readC())));
		}

		port = readH();
		maxPlayers = readD();
		requiredAccess = readD();
		password = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		GsConnection client = getConnection();

		GsAuthResponse resp = GameServerTable.registerGameServer(client, gameServerId, defaultAddress, ipRanges, port,
			maxPlayers, requiredAccess, password);

		switch (resp)
		{
			case AUTHED:
				getConnection().setState(State.AUTHED);
				DAOManager.getDAO(GameServersDAO.class).writeGameServerStatus(GameServerTable.getGameServerInfo(gameServerId));
				sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;

			default:
				client.close(new SM_GS_AUTH_RESPONSE(resp), true);
		}
	}
}
