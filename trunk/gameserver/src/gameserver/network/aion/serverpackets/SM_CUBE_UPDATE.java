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

public class SM_CUBE_UPDATE extends AionServerPacket
{
	private Player player;
	private int cubeType;
	private int advancedSlots;

	/**
	 * �������� ������ ������ SM_CUBE_UPDATE
	 * 
	 * @param player
	 */
	public SM_CUBE_UPDATE(Player player, int cubeType, int advancedSlots)
	{
		this.player = player;
		this.cubeType = cubeType;
		this.advancedSlots = advancedSlots;
	}

	public SM_CUBE_UPDATE(Player player, int cubeType)
	{
		this.player = player;
		this.cubeType = cubeType;
		this.advancedSlots = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, cubeType);
		writeC(buf, advancedSlots);
		switch(cubeType)
		{
			case 0:
				writeD(buf, player.getInventory().size());
				writeC(buf, player.getCubeSize()); // ������ ���� npc (max 5, � ��������� �����)
				writeC(buf, 0); // ������ ���� ��� ���������� ������ (���� 2, � ��������� �����)
				writeC(buf, 0); // ����������
				break;
			case 6:
				break;
			default:
				break;
		}
	}
}