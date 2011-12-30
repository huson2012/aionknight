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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_TARGET_SELECTED extends AionServerPacket
{
	@SuppressWarnings("unused")
	private Player		player;
	private int	level;
	private int	maxHp;
	private int	currentHp;
	private int targetObjId;

	public SM_TARGET_SELECTED(Player player)
	{
		this.player = player;
		if(player.getTarget() instanceof Creature)
		{
			this.level = ((Creature) player.getTarget()).getLevel();
			this.maxHp = ((Creature) player.getTarget()).getLifeStats().getMaxHp();
			this.currentHp = ((Creature) player.getTarget()).getLifeStats().getCurrentHp();
		}
		else
		{
			//TODO: check various gather on retail
			this.level = 1;
			this.maxHp = 1;
			this.currentHp = 1;
		}
		
		if(player.getTarget() != null)
			targetObjId = player.getTarget().getObjectId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, targetObjId);
		writeH(buf, level);
		writeD(buf, maxHp);
		writeD(buf, currentHp);
	}
}