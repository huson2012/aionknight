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

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;

/**
 * The universal packet for account/IP bans
 */
public class SM_BAN extends LsServerPacket
{
	/**
	 * Ban type
	 * 1 = account
	 * 2 = IP
	 * 3 = Full ban (account and IP)
	 */
	private	final	byte		type;
	
	/**
	 * Account to ban
	 */
	private	final	int			accountId;

	/**
	 * IP or mask to ban
	 */
	private	final	String		ip;

	/**
	 * Time in minutes. 0 = infinity;
	 * If time < 0 then it's unban command
	 */
	private	final	int			time;
	
	/**
	 * Object ID of Admin, who request the ban
	 */
	private	final	int			adminObjId;

	public SM_BAN(byte type, int accountId, String ip, int time, int adminObjId)
	{
		super(0x06);

		this.type = type;
		this.accountId = accountId;
		this.ip = ip;
		this.time = time;
		this.adminObjId = adminObjId;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		
		writeC(buf, type);
		writeD(buf, accountId);
		writeS(buf, ip);
		writeD(buf, time);
		writeD(buf, adminObjId);
	}
}
