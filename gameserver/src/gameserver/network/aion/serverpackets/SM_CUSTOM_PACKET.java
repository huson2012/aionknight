/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SM_CUSTOM_PACKET extends AionServerPacket
{
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

		public void writeValue(SM_CUSTOM_PACKET packet, ByteBuffer buf)
		{
			type.write(packet, buf, value);
		}
	}

	private List<PacketElement>	elements = new ArrayList<PacketElement>();
	public SM_CUSTOM_PACKET(int opcode)
	{
		super();
		setOpcode(opcode);
	}

	public void addElement(PacketElement packetElement)
	{
		elements.add(packetElement);
	}

	/**
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