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

import gameserver.dataholders.DataManager;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.SkillTemplate;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SM_SKILL_COOLDOWN extends AionServerPacket
{

	private Map<Integer, Long> cooldowns;
	
	public SM_SKILL_COOLDOWN(Map<Integer, Long> cooldowns)
	{
		this.cooldowns = cooldowns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		long currentTime = System.currentTimeMillis();
		Map<Integer, Integer> cooldowns = new HashMap<Integer, Integer>();
		for(Map.Entry<Integer, Long> entry : this.cooldowns.entrySet())
		{
			List<SkillTemplate> sts = DataManager.SKILL_DATA.getSkillTemplatesForDelayId(entry.getKey());
			int left = Math.round((entry.getValue() - currentTime) / 1000);
			for (SkillTemplate st : sts)
			{
				cooldowns.put(st.getSkillId(), left > 0 ? left : 0);
			}
		}
		
		writeH(buf, cooldowns.size());
		for(Map.Entry<Integer, Integer> entry : cooldowns.entrySet())
		{
			writeH(buf, entry.getKey());
			writeD(buf, entry.getValue());
		}
	}
}
