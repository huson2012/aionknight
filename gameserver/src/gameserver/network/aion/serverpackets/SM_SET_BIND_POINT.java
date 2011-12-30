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

import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_SET_BIND_POINT extends AionServerPacket
{
	
	private final int		mapId;
	private final float		x;
	private final float		y;
	private final float		z;
	private final Kisk 		kisk;

	public SM_SET_BIND_POINT(int mapId, float x, float y, float z, Player player)
	{
		this.mapId = mapId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.kisk	= player.getKisk();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		// Appears 0x04 if bound to a kisk. 0x00If not.
		writeC(buf, (kisk == null ? 0x00 : 0x04)); 
		
		writeC(buf, 0x01);// unk
		writeD(buf, mapId);// map id
		writeF(buf, x); // coordinate x
		writeF(buf, y); // coordinate y
		writeF(buf, z); // coordinate z
		writeD(buf, (kisk == null ? 0x00 : kisk.getObjectId())); // kisk object id
	}
}