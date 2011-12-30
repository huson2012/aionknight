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

public class SM_FRIEND_NOTIFY extends AionServerPacket
{
	/**
	 * Buddy has logged in
	 * (Or become visible)
	 */
	public static final int LOGIN 		= 0;
	/**
	 * Buddy has logged out
	 * (Or become invisible)
	 */
	public static final int LOGOUT 		= 1;
	/**
	 * Buddy has deleted you
	 */
	public static final int DELETED		= 2;
	
	private final int code;
	private final String name;
	
	/**
	 * Constructs a new notify packet
	 * @param code Message code
	 * @param name Name of friend
	 */
	public SM_FRIEND_NOTIFY(int code, String name)
	{
		this.code = code;
		this.name = name;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeS(buf, name);
		writeC(buf, code);
	}
}
