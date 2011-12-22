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

import commons.network.packet.BaseServerPacket;
import gameserver.network.Crypt;
import java.nio.ByteBuffer;

public abstract class AionServerPacket extends BaseServerPacket
{
	/**
	 * ByteBuffer that contains this packet data
	 */
	private ByteBuffer			buf;

	/**
	 * Constructs new server packet
	 */
	protected AionServerPacket()
	{
		super();
		setOpcode(ServerPacketsOpcodes.getOpcode(getClass()));
	}

	/**
	 * Write packet short opcodec, static and two additional bytes for opcode check
	 *
	 * @param buf
	 * @param value
	 */
	private final void writeOP(ByteBuffer buf, int value)
	{
		/** obfuscate packet id */
		byte[] op= new byte[2];
		op = toBytes((short)value);
		op[0]= Crypt.encodeOpcodec(value);

		buf.put(op[0]);
		buf.put(op[1]);

		/** put static server packet code */
		buf.put(Crypt.staticServerPacketCode);

		/** for checksum? */
		buf.put((byte) ~op[0]);
		buf.put((byte) ~op[1]);
	}
	
	private final byte[] toBytes(short s) {
		return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
	}

	public final void write(AionConnection con)
	{
		write(con, buf);
	}
	/**
	 * Write and encrypt this packet data for given connection, to given buffer.
	 *
	 * @param con
	 * @param buf
	 */
	public final void write(AionConnection con, ByteBuffer buf)
	{
		buf.putShort((short) 0);
		writeOP(buf, getOpcode());
		writeImpl(con, buf);
		buf.flip();
		buf.putShort((short) buf.limit());
		ByteBuffer b = buf.slice();
		buf.position(0);
		con.encrypt(b);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 *
	 * @param con
	 * @param buf
	 */
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{

	}

	/**
	 * @return the buf
	 */
	public ByteBuffer getBuf()
	{
		return buf;
	}

	/**
	 * @param buf the buf to set
	 */
	public void setBuf(ByteBuffer buf)
	{
		this.buf = buf;
	}
}
