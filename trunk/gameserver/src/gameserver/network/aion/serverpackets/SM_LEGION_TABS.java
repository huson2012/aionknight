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

import gameserver.model.legion.LegionHistory;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SM_LEGION_TABS extends AionServerPacket
{
	private int	page;
	private Collection<LegionHistory> legionHistory;

	public SM_LEGION_TABS(Collection<LegionHistory> legionHistory)
	{
		this.legionHistory = legionHistory;
		this.page = 0;
	}

	public SM_LEGION_TABS(Collection<LegionHistory> legionHistory, int page)
	{
		this.legionHistory = legionHistory;
		this.page = page;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		/**
		 * If history size is less than page*8 return
		 */
		if(legionHistory.size() < (page * 8))
			return;
		
		// TODO: Formula's could use a refactor
		int hisSize = legionHistory.size() - (page * 8);

		if(page == 0 && legionHistory.size() > 8)
			hisSize = 8;
		if(page == 1 && legionHistory.size() > 16)
			hisSize = 8;
		if(page == 2 && legionHistory.size() > 24)
			hisSize = 8;

		writeD(buf, 0x12); // Unk
		writeD(buf, page); // current page
		writeD(buf, hisSize);

		int i = 0;
		for(LegionHistory history : legionHistory)
		{
			if(i >= (page * 8) && i <= (8 + (page * 8)))
			{
				writeD(buf, (int) (history.getTime().getTime() / 1000));
				writeC(buf, history.getLegionHistoryType().getHistoryId());
				writeC(buf, 0);
				if(history.getName().length() > 0)
				{
					writeS(buf, history.getName());
					int size = 134 - (history.getName().length() * 2 + 2);
					writeB(buf, new byte[size]);
				}
				else
					writeB(buf, new byte[134]);
			}
			i++;
			if(i >= (8 + (page * 8)))
				break;
		}
		writeH(buf, 0);
	}
}