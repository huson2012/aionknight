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

import gameserver.model.account.Account;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.PlayerInfo;
import java.nio.ByteBuffer;

public class SM_CHARACTER_LIST extends PlayerInfo
{
	private final int	playOk2;

    public SM_CHARACTER_LIST(int playOk2)
	{
		this.playOk2 = playOk2;
	}

    @Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playOk2);

		Account account = con.getAccount();
		writeC(buf, account.size());

		for(PlayerAccountData playerData : account.getSortedAccountsList())
		{
			writePlayerInfo(buf, playerData);

			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeC(buf, 0);
			writeC(buf, 0);
			writeB(buf, new byte[28]);
		}
	}
}