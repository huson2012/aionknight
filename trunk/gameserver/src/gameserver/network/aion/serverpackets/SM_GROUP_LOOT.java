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

public class SM_GROUP_LOOT extends AionServerPacket
{	
	private int groupId;
	private int unk1;
	private int unk2;
	private int itemId;
	private int itemIndex;
	private int lootCorpseId;
	private int distributionId;
	private int playerId;
	private int luck;

	/**
	 * Start the roll options.
	 * 
	 * @param Player
	 *           Id must be 0 to start the Roll Options
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = 0;
		this.luck = 1;
	}

	/**
	 * Update the roll when someone rolls or passes.
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId, int playerId, int luck)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = playerId;
		this.luck = luck;
	}

	/**
	 * Send a packet with the winner.
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId, int playerId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = playerId;
		this.luck = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, groupId);
		writeD(buf, unk1);
		writeD(buf, unk2);
		writeD(buf, itemId);
		writeC(buf, itemIndex);
		writeD(buf, lootCorpseId);
		writeC(buf, distributionId);
		writeD(buf, playerId);
		writeD(buf, luck);
	}
}
