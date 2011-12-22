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

package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_LS_CHARACTER_COUNT;
import gameserver.services.AccountService;

/**
 * Packet sent by login server to request account characters count
 */
public class CM_LS_REQUEST_CHARACTER_COUNT extends LsClientPacket
{	
	private int	accountId;
	
	/**
	 * @param opcode
	 */
	public CM_LS_REQUEST_CHARACTER_COUNT(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		accountId = readD();
	}

	@Override
	protected void runImpl()
	{
		int characterCount = AccountService.getCharacterCountFor(accountId);
		sendPacket(new SM_LS_CHARACTER_COUNT(accountId, characterCount));
	}
}