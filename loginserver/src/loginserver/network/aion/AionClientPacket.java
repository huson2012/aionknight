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

package loginserver.network.aion;

import java.nio.ByteBuffer;
import loginserver.model.Account;
import org.apache.log4j.Logger;
import commons.network.packet.BaseClientPacket;

/**
 * Base class for every Aion -> LS Client Packet
 */
public abstract class AionClientPacket extends BaseClientPacket<AionConnection>
{
	/**
	 * Logger for this class.
	 */
	private static final Logger		log	= Logger.getLogger(AionClientPacket.class);

	/**
	 * Constructs new client packet.
	 * 
	 * @param buf       packet data
	 * @param client    client
	 * @param opcode    packet id
	 */
	protected AionClientPacket(ByteBuffer buf, AionConnection client, int opcode)
	{
		super(buf, opcode);
		setConnection(client);
	}

	/**
	 * run runImpl catching and logging Throwable.
	 */
	@Override
	public final void run()
	{
		try
		{
			runImpl();
		}
		catch (Throwable e)
		{
			String name;
			Account account = getConnection().getAccount();
			if (account != null)
			{
				name = account.getName();
			}
			else
			{
				name = getConnection().getIP();
			}

			log.error("error handling client (" + name + ") message " + this, e);
		}
	}

	/**
	 * Send new AionServerPacket to connection that is owner of this packet. This method is equvalent to:
	 * getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(AionServerPacket msg)
	{
		getConnection().sendPacket(msg);
	}
}
