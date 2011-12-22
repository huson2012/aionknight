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

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс используется для передачи клиенту сгенерированные админ-команды //fsc
 *
 * @see gameserver.utils.chathandlers.admincommands.SendFakeServerPacket
 */
public class SM_CUSTOM_PACKET extends AionServerPacket
{
	/** 
	 * Перечисление элементов пакета.
	 */
	public static enum PacketElementType
	{
		D('d')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeD(buf, Integer.decode(value));
			}
		},
		H('h')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeH(buf, Integer.decode(value));
			}
		},
		C('c')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeC(buf, Integer.decode(value));
			}
		},
		F('f')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeF(buf, Float.valueOf(value));
			}
		},
		DF('e')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeDF(buf, Double.valueOf(value));
			}
		},
		Q('q')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeQ(buf, Long.decode(value));
			}
		},
		S('s')
		{
			@Override
			public void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value)
			{
				packet.writeS(buf, value);
			}
		};

		private final char	code;

		private PacketElementType(char code)
		{
			this.code = code;
		}

		public static PacketElementType getByCode(char code)
		{
			for(PacketElementType type : values())
				if(type.code == code)
					return type;
			return null;
		}

		/**
		 * Записывает значения в буфер в соответствии с ElementType
		 * 
		 * @param packet
		 *           пакетов, например
		 * @param buf
		 *           пакетная запись буфера
		 * @param value
		 *           элемент значение
		 */
		public abstract void write(SM_CUSTOM_PACKET packet, ByteBuffer buf, String value);
	}

	public static class PacketElement
	{
		private final PacketElementType	type;
		private final String			value;

		public PacketElement(PacketElementType type, String value)
		{
			this.type = type;
			this.value = value;
		}

		/**
		 * Записывает значение, сохраненное в PacketElement в буфер
		 * 
		 * @param packet
		 *           пакетов, например.
		 * @param buf
		 *           пакетная запись буфера.
		 */
		public void writeValue(SM_CUSTOM_PACKET packet, ByteBuffer buf)
		{
			type.write(packet, buf, value);
		}
	}

	private List<PacketElement>	elements	= new ArrayList<PacketElement>();
	public SM_CUSTOM_PACKET(int opcode)
	{
		super();
		setOpcode(opcode);
	}

	/**
	 * Добавление элементов этому пакету.
	 * 
	 * @param packetElement
	 */
	public void addElement(PacketElement packetElement)
	{
		elements.add(packetElement);
	}

	/**
	 * Возможные элементы.
	 * 
	 * @param type
	 * @param value
	 */
	public void addElement(PacketElementType type, String value)
	{
		elements.add(new PacketElement(type, value));
	}

	/** {@inheritDoc} */
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		for(PacketElement el : elements)
		{
			el.writeValue(this, buf);
		}
	}
}