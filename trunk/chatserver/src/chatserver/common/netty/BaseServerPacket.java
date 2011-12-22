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

package chatserver.common.netty;

import org.jboss.netty.buffer.ChannelBuffer;

public abstract class BaseServerPacket extends AbstractPacket
{
	/**
	 * @param opCode
	 */
	public BaseServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeD(ChannelBuffer buf, int value)
	{
		buf.writeInt(value);
	}
	
	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeH(ChannelBuffer buf, int value)
	{
		buf.writeShort((short)value);
	}

	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeC(ChannelBuffer buf, int value)
	{
		buf.writeByte((byte)value);
	}

	/**
	 * Write double to buffer.
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeDF(ChannelBuffer buf, double value)
	{
		buf.writeDouble(value);
	}
	
	/**
	 * Write float to buffer.
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeF(ChannelBuffer buf, float value)
	{
		buf.writeFloat(value);
	}
	
	/**
	 * 
	 * @param buf
	 * @param data
	 */
	protected final void writeB(ChannelBuffer buf, byte[] data)
	{
		buf.writeBytes(data);
	}
	
	/**
	 * Запись строки в буфер
	 * 
	 * @param buf
	 * @param text
	 */
	protected final void writeS(ChannelBuffer buf, String text)
	{
		if (text == null)
		{
			buf.writeChar('\000');
		}
		else
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
				buf.writeChar(text.charAt(i));
			buf.writeChar('\000');
		}
	}
	
	/**
	 * @param buf
	 * @param data
	 */
	protected final void writeQ(ChannelBuffer buf, long data)
	{
		buf.writeLong(data);
	}
}