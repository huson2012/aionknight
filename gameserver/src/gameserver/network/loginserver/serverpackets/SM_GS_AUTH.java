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

package gameserver.network.loginserver.serverpackets;

import commons.network.IPRange;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * This is authentication packet that gs will send to login server for registration.
 */
public class SM_GS_AUTH extends LsServerPacket
{
	/**
	 * Constructs new instance of <tt>SM_GS_AUTH </tt> packet.
	 * 
	 */
	public SM_GS_AUTH()
	{
		super(0x00);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, NetworkConfig.GAMESERVER_ID);
		writeC(buf, IPConfig.getDefaultAddress().length);
		writeB(buf, IPConfig.getDefaultAddress());

		List<IPRange> ranges = IPConfig.getRanges();
		int size = ranges.size();
		writeD(buf, size);
		for(int i = 0; i < size; i++)
		{
			IPRange ipRange = ranges.get(i);
			byte[] min = ipRange.getMinAsByteArray();
			byte[] max = ipRange.getMaxAsByteArray();
			writeC(buf, min.length);
			writeB(buf, min);
			writeC(buf, max.length);
			writeB(buf, max);
			writeC(buf, ipRange.getAddress().length);
			writeB(buf, ipRange.getAddress());
		}

		writeH(buf, NetworkConfig.GAME_PORT);
		writeD(buf, NetworkConfig.MAX_ONLINE_PLAYERS);
		writeD(buf, NetworkConfig.REQUIRED_ACCESS);
		writeS(buf, NetworkConfig.LOGIN_PASSWORD);
	}
}
