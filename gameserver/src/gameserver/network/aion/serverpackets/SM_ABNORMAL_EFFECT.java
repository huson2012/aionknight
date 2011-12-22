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
import gameserver.skill.model.Effect;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SM_ABNORMAL_EFFECT extends AionServerPacket
{	
	private int effectedId;
	private int abnormals;
	private Collection<Effect> effects;
	
	public SM_ABNORMAL_EFFECT(int effectedId, int abnormals,  Collection<Effect> effects)
	{
		this.effects = effects;
		this.abnormals = abnormals;
		this.effectedId = effectedId;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, effectedId); 
		writeC(buf, 1);
		writeD(buf, 0);
		writeD(buf, abnormals);
		writeD(buf, 0);

		writeH(buf, effects.size()); 
		
		for(Effect effect : effects)
		{
			writeH(buf, effect.getSkillId()); 
			writeC(buf, effect.getSkillLevel());
			writeC(buf, effect.getTargetSlot()); 
			writeD(buf, effect.getElapsedTime());
		}
	}
}