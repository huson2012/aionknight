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

import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.PlayerInfo;
import java.nio.ByteBuffer;

/**
 * ���� ����� �������� ������� �� CM_CREATE_CHARACTER
 */
public class SM_CREATE_CHARACTER extends PlayerInfo
{	
	/** 
	 * ���� ����� ����� ��
	 */
	public static final int	RESPONSE_OK	= 0x00;
	
	/** 
	 * ������ �������� ���������
	 */
	public static final int	FAILED_TO_CREATE_THE_CHARACTER = 1;
	
	/** 
	 * �� ������� ������� ��������� ��-�� ������ � ��
	 */
	public static final int RESPONSE_DB_ERROR = 2;
	
	/** 
	 * ���������� ���������� ��������� ����������� ���������� ��� �������
	 */
	public static final int RESPONSE_SERVER_LIMIT_EXCEEDED = 4;
	
	/** 
	 * ������������ ��� ���������
	 */
	public static final int	RESPONSE_INVALID_NAME = 5;
	
	/** 
	 * ��� ��������� �������� ������������ ������� 
	 */
	public static final int RESPONSE_FORBIDDEN_CHAR_NAME= 9;
	
	/** 
	 * �������� � ����� ������ ��� ���������� 
	 */
	public static final int	RESPONSE_NAME_ALREADY_USED = 10;
	
	/** 
	 * ��� ��� ���������������
	 */
	public static final int RESPONSE_NAME_RESERVED = 11;
	
	/** 
	 * �� �� ������� ������� ���������� ������ ���� �� ����� � ��� �� ������� 
	 */
	public static final int RESPONSE_OTHER_RACE = 12;

	/**
	 * ��� ������
	 */
	private final int responseCode;

	/**
	 * ����� ��������� �����
	 */
	private final PlayerAccountData	player;

	/**
	 * �������� ������ SM_CREATE_CHARACTER ������
	 * 
	 * @param accPlData
	 *           playerAccountData �����, ������� ��� ������
	 * @param responseCode
	 *           ��� ������ (invalid nickname, nickname is already taken, ok)
	 */

	public SM_CREATE_CHARACTER(PlayerAccountData accPlData, int responseCode)
	{
		this.player = accPlData;
		this.responseCode = responseCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, responseCode);

		if(responseCode == RESPONSE_OK)
		{
			writePlayerInfo(buf, player); // ���� ��� ������, �� ��� ������ �� ��������� ����� ���������� � ��
		}
		else
		{
			writeB(buf, new byte[512]); // ���� ���-�� �� ���, � ����� ����� ������� ������ ��� ��������
		}
	}
}
