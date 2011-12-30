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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_INSTANCE_COOLDOWN extends AionServerPacket
{
	private Player player;
	private boolean init = false;
	private int instanceId = 0;
	private int remainingTime = 0;
	private int type = 2;
	private boolean self = false;

	public SM_INSTANCE_COOLDOWN(Player player)
	{
		this.player = player;
	}
	
	public SM_INSTANCE_COOLDOWN(boolean init)
	{
		this.init = init;
	}
	
	public SM_INSTANCE_COOLDOWN(Player player, int id, int time, int type, boolean self)
	{
		this.player = player;
		this.instanceId = id;
		this.remainingTime = time;
		this.type = type;
		this.self = self;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(!init)
		{
			writeH(buf, type);
			writeD(buf, 0x0);
			writeH(buf, 0x1);
			writeD(buf, player.getObjectId());
			if(instanceId != 0 && remainingTime != 0)
			{
				writeH(buf, 0x1); //instance info
				writeD(buf, instanceId);
				writeD(buf, 0x0);
				writeD(buf, remainingTime); //remaingTime in seconds
				if(self)
					writeH(buf, 0x0);
				writeS(buf, player.getName());
			}
			else
			{
				writeH(buf, 0x0); //not instance info
				writeS(buf, player.getName());
			}
		}
		else
		{
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
		}
	}
}
