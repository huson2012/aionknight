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

package gameserver.network.aion;

import commons.network.packet.BaseClientPacket;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;

public abstract class AionClientPacket extends BaseClientPacket<AionConnection> implements Cloneable
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(AionClientPacket.class);

	/**
	 * Constructs new client packet instance.
	 * 
	 * @param buf
	 *           packet data
	 * @param client
	 *           packet owner
	 * @param opcode
	 *           packet id
	 */
	@Deprecated
	protected AionClientPacket(ByteBuffer buf, AionConnection client, int opcode)
	{
		super(buf, opcode);
		setConnection(client);
	}

	/**
	 * Constructs new client packet instance. ByBuffer and ClientConnection should be later set manually, after using
	 * this constructor.
	 * 
	 * @param opcode
	 *           packet id
	 */
	protected AionClientPacket(int opcode)
	{
		super(opcode);
	}

	/**
	 * run runImpl catching and logging Throwable.
	 */
	@Override
	public final void run()
	{
		try
		{
			runImpl();
		}
		catch(Throwable e)
		{
			String name = getConnection().getAccount().getName();
			if(name == null)
				name = getConnection().getIP();

			log.error("Error handling client (" + name + ") message :" + this, e);
		}
	}

	/**
	 * Send new AionServerPacket to connection that is owner of this packet. This method is equvalent to:
	 * getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(AionServerPacket msg)
	{
		getConnection().sendPacket(msg);
	}

	/**
	 * Clones this packet object.
	 * 
	 * @return AionClientPacket
	 */
	public AionClientPacket clonePacket()
	{
		try
		{
			return (AionClientPacket) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}
}