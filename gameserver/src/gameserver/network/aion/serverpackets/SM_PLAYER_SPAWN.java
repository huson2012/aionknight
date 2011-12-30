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

public class SM_PLAYER_SPAWN extends AionServerPacket
{
	/**
	 * Player that is entering game.
	 */
	private final Player player;

	/**
	 * Constructor.
	 * 
	 * @param player
	 */
	public SM_PLAYER_SPAWN(Player player)
	{
		super();
		this.player = player;
	}

    /**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, player.getWorldId());
		writeD(buf, player.getWorldId());//world + chnl
		writeD(buf, 0x00);// unk
		writeC(buf, 0x00);// unk
		writeF(buf, player.getX());// x
		writeF(buf, player.getY());// y
		writeF(buf, player.getZ());// z
		writeC(buf, player.getHeading());// heading
		writeD(buf, 0x00); // unk 2.5
		writeD(buf, 0x00); // unk 2.5
	}
}