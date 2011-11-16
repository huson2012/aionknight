/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import gameserver.configs.main.GSConfig;

public class EncryptionKeyPair
{
	/**
	 * ���������� ������ ��� ������� � �������
	 * @see EncryptionKeyPair.keys
	 */
	private static final int SERVER = 0;
	/**
	 * ������ ������ ��� ������� � �������
	 * @see EncryptionKeyPair.keys
	 */
	private static final int CLIENT = 1;
	/**
	 * ����������� 'XOR' ����
	 */
	private final static byte[]	staticKey = "nKO/WctQ0AVLbpzfBkS6NevDYT8ourG5CRlmdjyJ72aswx4EPq1UgZhFMXH?3iI9".getBytes();
    /**
     * ������ ������ ���� ������ ������ ���� ����� �����
     */
	private static byte	staticClientPacketCode	= 0x55;
	/**
	 * ���� �������� ���� ������ � ��������
	 */
	private static final long KEY_VALIDITY = 600;
	/**
	 * ������� ����, ������������ ��� �������� ������/������ ������
	 */
	private int baseKey = 0;
	/**
	 * ����� ����������
	 */
	private byte[][] keys = null;
	/**
	 * ���� ���������� ������������� �����
	 */
	private Date lastUpdate = null;
	/**
	 * ������������� ������ '������/������' �� ������ 'baseKey'
	 * @param baseKey random integer
	 */
	public EncryptionKeyPair (int baseKey) 
	{
		this.baseKey = baseKey;
		this.keys = new byte[2][];
		this.keys[SERVER] = new byte[] {
			(byte) (baseKey & 0xff),
			(byte) ((baseKey >> 8) & 0xff),
			(byte) ((baseKey >> 16) & 0xff),
			(byte) ((baseKey >> 24) & 0xff),
			(byte) 0xa1,
			(byte) 0x6c,
			(byte) 0x54,
			(byte) 0x87
		};
		this.keys[CLIENT] = new byte[this.keys[SERVER].length];
		System.arraycopy(this.keys[SERVER], 0, this.keys[CLIENT], 0, this.keys[SERVER].length);
		this.lastUpdate = Calendar.getInstance().getTime();
	}
	/**
	 * @return � 'baseKey' ������������ ��� ��������� ���� ������
	 */
	public int getBaseKey ()
	{
		return baseKey;
	}

	@Override
	public String toString ()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{c:0x");
		for (int i=0; i<keys[CLIENT].length;i++)
		{
			sb.append(Integer.toHexString(keys[CLIENT][i]&0xff));
		}
		sb.append(",s:0x");
		for (int i=0; i<keys[SERVER].length;i++)
		{
			sb.append(Integer.toHexString(keys[SERVER][i]&0xff));
		}
		sb.append(",baseKey:0x");
		sb.append(Integer.toHexString(baseKey));
		sb.append(",updated:"+lastUpdate+"}");
		return sb.toString();
	}
	/**
	 * �������� �� �����������/������������� ������ �������
	 */
	private final boolean validateClientPacket(ByteBuffer buf)
	{
		if(GSConfig.SERVER_VERSION.startsWith("2.5"))
		staticClientPacketCode = 0x57;
		boolean valid = false;
		byte buf0 = buf.get(0);
		byte buf1 = buf.get(1);
		byte buf2 = buf.get(2);
		valid = buf0 == ~buf2;
		valid = valid && buf1 == staticClientPacketCode;
		return valid;
	}
	/**
	 * Decrypt client packet from this ByteBuffer
	 * If decryption is successful, update client key 
	 * @return true if decryption was successful
	 */
	public boolean decrypt (ByteBuffer buf)
	{
		final byte[] data = buf.array();
		final int size = buf.remaining();
		byte[] clientPacketKey = keys[CLIENT];

		/** index to byte that should be decrypted now */
		int arrayIndex = buf.arrayOffset() + buf.position();

		/** prev encrypted byte */
		int prev = data[arrayIndex];

		/** decrypt first byte */
		data[arrayIndex++] ^= (clientPacketKey[0] & 0xff);

		/** decrypt loop */
		for(int i = 1; i < size; i++, arrayIndex++)
		{
			int curr = data[arrayIndex] & 0xff;
			data[arrayIndex] ^= (staticKey[i & 63] & 0xff) ^ (clientPacketKey[i & 7] & 0xff) ^ prev;
			prev = curr;
		}
		
		/** oldKey value as long */
		long oldKey = (((long) clientPacketKey[0] & 0xff) << 0) | (((long) clientPacketKey[1] & 0xff) << 8)
		| (((long) clientPacketKey[2] & 0xff) << 16) | (((long) clientPacketKey[3] & 0xff) << 24)
		| (((long) clientPacketKey[4] & 0xff) << 32) | (((long) clientPacketKey[5] & 0xff) << 40)
		| (((long) clientPacketKey[6] & 0xff) << 48) | (((long) clientPacketKey[7] & 0xff) << 56);

		/** change key */
		oldKey += size;

		if (validateClientPacket(buf))
		{
			/** set key new value */
			clientPacketKey[0] = (byte) (oldKey >> 0 & 0xff);
			clientPacketKey[1] = (byte) (oldKey >> 8 & 0xff);
			clientPacketKey[2] = (byte) (oldKey >> 16 & 0xff);
			clientPacketKey[3] = (byte) (oldKey >> 24 & 0xff);
			clientPacketKey[4] = (byte) (oldKey >> 32 & 0xff);
			clientPacketKey[5] = (byte) (oldKey >> 40 & 0xff);
			clientPacketKey[6] = (byte) (oldKey >> 48 & 0xff);
			clientPacketKey[7] = (byte) (oldKey >> 56 & 0xff);
			return true;
		}
		return false;
	}
	
	/**
	 * Encrypt server packet from this ByteBuffer
	 */
	public void encrypt (ByteBuffer buf)
	{
		final byte[] data = buf.array();
		final int size = buf.remaining();
		byte[] serverPacketKey = keys[SERVER];

		/** index to byte that should be encrypted now */
		int arrayIndex = buf.arrayOffset() + buf.position();

		/** encrypt first byte */
		data[arrayIndex] ^= (serverPacketKey[0] & 0xff);

		/** prev encrypted byte */
		int prev = data[arrayIndex++];

		/** encrypt loop */
		for(int i = 1; i < size; i++, arrayIndex++)
		{
			data[arrayIndex] ^= (staticKey[i & 63] & 0xff) ^ (serverPacketKey[i & 7] & 0xff) ^ prev;
			prev = data[arrayIndex];
		}

		/** oldKey value as long */
		long oldKey = (((long) serverPacketKey[0] & 0xff) << 0) | (((long) serverPacketKey[1] & 0xff) << 8)
			| (((long) serverPacketKey[2] & 0xff) << 16) | (((long) serverPacketKey[3] & 0xff) << 24)
			| (((long) serverPacketKey[4] & 0xff) << 32) | (((long) serverPacketKey[5] & 0xff) << 40)
			| (((long) serverPacketKey[6] & 0xff) << 48) | (((long) serverPacketKey[7] & 0xff) << 56);

		/** ����� ����� */
		oldKey += size;

		/** ��������� ������ �������� ����� */
		serverPacketKey[0] = (byte) (oldKey >> 0 & 0xff);
		serverPacketKey[1] = (byte) (oldKey >> 8 & 0xff);
		serverPacketKey[2] = (byte) (oldKey >> 16 & 0xff);
		serverPacketKey[3] = (byte) (oldKey >> 24 & 0xff);
		serverPacketKey[4] = (byte) (oldKey >> 32 & 0xff);
		serverPacketKey[5] = (byte) (oldKey >> 40 & 0xff);
		serverPacketKey[6] = (byte) (oldKey >> 48 & 0xff);
		serverPacketKey[7] = (byte) (oldKey >> 56 & 0xff);
	}
	/**
	 * @return true, ���� ���� �����
	 */
	public boolean isExpired ()
	{
		return ((Calendar.getInstance().getTimeInMillis()-lastUpdate.getTime())>=(KEY_VALIDITY*1000));
	}
	/**
	 * ��������� ���� ���������� ����������
	 */
	public void update ()
	{
		lastUpdate = Calendar.getInstance().getTime();
	}
}