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

import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CRAFT_UPDATE extends AionServerPacket
{
	private int skillId;
	private int itemId;
	private int action;
	private int success;
	private int failure;
	private int nameId;
	private int delay;

	/**
	 * @param skillId
	 * @param item
	 * @param success
	 * @param failure
	 * @param action
	 */
	public SM_CRAFT_UPDATE(int skillId, ItemTemplate item, int success, int failure, int action)
	{
		this.action = action;
		this.skillId = skillId;
		this.itemId = item.getTemplateId();
		this.success = success;
		this.failure = failure;
		this.nameId = item.getNameId();
		if(skillId == 40009)
			this.delay = 1500;
		else
			this.delay = 700;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, skillId);
		writeC(buf, action);
		writeD(buf, itemId);

		switch(action)
		{
			case 0: // �������������
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 1200);		// ��������, ����� ���� ��� ������ ��������� (��)
				writeD(buf, 1330048);	// ��������� � ������ ������
				writeH(buf, 0x24);
				writeD(buf, nameId);	// item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			case 1: // ���������� ����������
			case 2: // �������� ����������
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, delay);	// time of moving execution (ms)
				writeD(buf, 1200);	// delay after which bar will start moving (ms)
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			case 3: //crit
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			case 4:	// ������ ������
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 1330051);	// ��������� �� ������ ������
				writeH(buf, 0);
				break;
			case 5: // �������� �����
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);	
				writeD(buf, 1300788);	// ��������� �� �������� ������
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			case 6: // ������� ��� ������
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);	
				writeD(buf, 1330050);	// ��������� � ��������� ������
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
		}
	}
}