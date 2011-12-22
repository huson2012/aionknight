/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * ���� ����� ������������ ��� �������� ������� ��������������� �����-������� //fsc
 *
 * @see gameserver.utils.chathandlers.admincommands.SendFakeServerPacket
 */
public class SM_CUSTOM_PACKET extends AionServerPacket
{
	/** 
	 * ������������ ��������� ������.
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
		 * ���������� �������� � ����� � ������������ � ElementType
		 * 
		 * @param packet
		 *           �������, ��������
		 * @param buf
		 *           �������� ������ ������
		 * @param value
		 *           ������� ��������
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
		 * ���������� ��������, ����������� � PacketElement � �����
		 * 
		 * @param packet
		 *           �������, ��������.
		 * @param buf
		 *           �������� ������ ������.
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
	 * ���������� ��������� ����� ������.
	 * 
	 * @param packetElement
	 */
	public void addElement(PacketElement packetElement)
	{
		elements.add(packetElement);
	}

	/**
	 * ��������� ��������.
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