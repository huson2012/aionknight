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

package loginserver.network.gameserver;

import java.nio.ByteBuffer;
import org.apache.log4j.Logger;
import commons.network.packet.BaseClientPacket;

/**
 * Base class for every GameServer -> LS Client Packet
 */
public abstract class GsClientPacket extends BaseClientPacket<GsConnection>
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(GsClientPacket.class);

	/**
	 * Creates new packet instance. 
	 * 
	 * @param buf packet data
	 * @param client client
	 * @param opcode packet id
	 */
	protected GsClientPacket(ByteBuffer buf, GsConnection client, int opcode)
	{
		super(buf, opcode);
		setConnection(client);
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
		catch (Throwable e)
		{
			log.warn("error handling gs (" + getConnection().getIP() + ") message " + this, e);
		}
	}

	/**
	 * Send new GsServerPacket to connection that is owner of this packet. This method is equivalent to:
	 * getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(GsServerPacket msg)
	{
		getConnection().sendPacket(msg);
	}
}
