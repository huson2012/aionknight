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
 
package chatserver.network.aion.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.model.message.Message;
import chatserver.network.aion.AbstractServerPacket;
import chatserver.network.netty.handler.ClientChannelHandler;

public class SM_CHANNEL_MESSAGE extends AbstractServerPacket
{
	
	private Message message;
	
	public SM_CHANNEL_MESSAGE(Message message)
	{
		super(0x1A);
		this.message = message;
	}

	@Override
	protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeC(buf, 0x00);
		writeD(buf, message.getChannel().getChannelId());
		writeD(buf, message.getSender().getClientId());
		writeD(buf, 0x00);
		writeH(buf, message.getSender().getIdentifier().length / 2);
		writeB(buf, message.getSender().getIdentifier());
		writeH(buf, message.size() / 2);
		writeB(buf, message.getText());
	}
}