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
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;

public class SM_PLAY_FAIL extends AionServerPacket
{
	/**
	 * response - why play fail
	 */
	private AionAuthResponse	response;

	/**
	 * Constructs new instance of <tt>SM_PLAY_FAIL</tt> packet.
	 * 
	 * @param response auth response
	 */
	public SM_PLAY_FAIL(AionAuthResponse response)
	{
		super(0x06);

		this.response = response;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, response.getMessageId());
	}
}
