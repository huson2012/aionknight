/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
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

package loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;
import loginserver.network.aion.SessionKey;

/**
 * This packet is send to client to update sessionKey [for fast reconnection feature]
 */
public class SM_UPDATE_SESSION extends AionServerPacket
{
	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private final int	accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private final int	loginOk;

	/**
	 * Constructs new instance of <tt>SM_UPDATE_SESSION </tt> packet.
	 * 
	 * @param key session key
	 */
	public SM_UPDATE_SESSION(SessionKey key)
	{
		super(0x0c);

		this.accountId = key.accountId;
		this.loginOk = key.loginOk;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeD(buf, loginOk);
		writeC(buf, 0x00);// sysmsg if smth is wrong
	}
}
