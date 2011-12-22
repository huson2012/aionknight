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

package chatserver.network.aion;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.common.netty.AbstractPacketHandler;
import chatserver.network.aion.clientpackets.CM_CHANNEL_MESSAGE;
import chatserver.network.aion.clientpackets.CM_CHANNEL_REQUEST;
import chatserver.network.aion.clientpackets.CM_PLAYER_AUTH;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.network.netty.handler.ClientChannelHandler.State;

public class ClientPacketHandler extends AbstractPacketHandler
{
	/**
	 * Reads one packet from ChannelBuffer
	 * @param buf
	 * @param channelHandler
	 * @return AbstractClientPacket
	 */
	public static AbstractClientPacket handle(ChannelBuffer buf, ClientChannelHandler channelHandler)
	{
		byte opCode = buf.readByte();
		State state = channelHandler.getState();
		AbstractClientPacket clientPacket = null;

		switch (state)
		{
			case CONNECTED:
				switch (opCode)
				{
					case 0x05:
						clientPacket = new CM_PLAYER_AUTH(buf, channelHandler);
						break;
					default:
					//unknownPacket(opCode, state.toString());
				}
				break;
			case AUTHED:
				switch (opCode)
				{
					case 0x10:
						clientPacket = new CM_CHANNEL_REQUEST(buf, channelHandler);
						break;
					case 0x18:
						clientPacket = new CM_CHANNEL_MESSAGE(buf, channelHandler);
					default:
					//unknownPacket(opCode, state.toString());
				}
				break;
		}
		return clientPacket;
	}
}