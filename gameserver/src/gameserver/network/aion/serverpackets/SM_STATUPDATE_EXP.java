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

/**
 * This packet is used to update current exp / recoverable exp / max exp values.
 */
public class SM_STATUPDATE_EXP extends AionServerPacket
{
	private long	currentExp;
	private long	recoverableExp;
	private long	maxExp;

	/**
	 * 
	 * @param currentExp
	 * @param recoverableExp
	 * @param maxExp
	 */
	public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp)
	{
		this.currentExp = currentExp;
		this.recoverableExp = recoverableExp;
		this.maxExp = maxExp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeQ(buf, currentExp);
		writeQ(buf, recoverableExp);
		writeQ(buf, maxExp);
		if(con.getActivePlayer() != null && con.getActivePlayer().getCommonData().getRepletionState() > 0)
		{
			writeQ(buf, con.getActivePlayer().getCommonData().getRepletionState());
			writeQ(buf, (((con.getActivePlayer().getLevel() * 1000) * 2) * con.getActivePlayer().getLevel()));
		}
		else
			writeQ(buf, 0);
			writeQ(buf, 0);
	}

}
