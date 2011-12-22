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

package chatserver.network.gameserver.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.configs.Config;
import chatserver.network.gameserver.AbstractGameServerPacket;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.netty.handler.GameChannelHandler;

public class SM_GS_AUTH_RESPONSE extends AbstractGameServerPacket
{
	private GsAuthResponse	response;

	public SM_GS_AUTH_RESPONSE(GsAuthResponse resp)
	{
		super(0x00);
		this.response = resp;
	}

	@Override
	protected void writeImpl(GameChannelHandler gameChannelHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeC(buf, response.getResponseId());
		writeB(buf, Config.CHAT_ADDRESS.getAddress().getAddress());
		writeH(buf, Config.CHAT_ADDRESS.getPort());
	}
}