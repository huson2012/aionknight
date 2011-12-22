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

package gameserver.dataholders;

import gameserver.model.templates.npcskill.NpcSkillList;
import gnu.trove.TIntObjectHashMap;
import org.apache.log4j.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "npc_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcSkillData
{
	@XmlElement(name = "npcskills")
	private List<NpcSkillList> npcSkills;
	private TIntObjectHashMap<NpcSkillList>	npcSkillData	= new TIntObjectHashMap<NpcSkillList>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(NpcSkillList npcSkill : npcSkills)
		{
			npcSkillData.put(npcSkill.getNpcId(), npcSkill);
			
			if(npcSkill.getNpcSkills() == null)
				Logger.getLogger(NpcSkillData.class).error("NO SKILL");
		}
		
	}
	
	public int size()
	{
		return npcSkillData.size();
	}
	
	public NpcSkillList getNpcSkillList(int id)
	{
		return npcSkillData.get(id);
	}
}