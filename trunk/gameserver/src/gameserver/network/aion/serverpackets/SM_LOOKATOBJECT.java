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

import gameserver.model.gameobjects.VisibleObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LOOKATOBJECT extends AionServerPacket
{
	private VisibleObject	visibleObject;
	private int		targetObjectId;
	private int		heading;

	public SM_LOOKATOBJECT(VisibleObject visibleObject)
	{
		this.visibleObject = visibleObject;
		if(visibleObject.getTarget() != null)
		{
			this.targetObjectId = visibleObject.getTarget().getObjectId();
			this.heading = Math.abs(128 - visibleObject.getTarget().getHeading());
		}
		else
		{
			this.targetObjectId = 0;
			this.heading = visibleObject.getHeading();
		}
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, visibleObject.getObjectId());
		writeD(buf, targetObjectId);
		writeC(buf, heading);
	}
}
