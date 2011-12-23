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

package loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;
import loginserver.GameServerInfo;
import loginserver.controller.AccountController;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

/**
 * Packet sent to game server to request account characters count
 * When all characters count have been received, send server list to client 
 */
public class CM_GS_CHARACTER_COUNT extends GsClientPacket
{
	private int accountId;
	private int characterCount;
	
	/**
	 * @param buf
	 * @param client
	 */
	public CM_GS_CHARACTER_COUNT(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x07);
	}

	@Override
	protected void readImpl()
	{
		accountId = readD();
		characterCount = readC();
	}

	@Override
	protected void runImpl()
	{
		GameServerInfo gsi = getConnection().getGameServerInfo();
		
		AccountController.addCharacterCountFor(accountId, gsi.getId(), characterCount);
		
		if(AccountController.hasAllCharacterCounts(accountId))
		{
			AccountController.sendServerListFor(accountId);
		}
	}
}
