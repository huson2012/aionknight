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

import gameserver.skill.model.SkillTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "skill_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillData
{	
	@XmlElement(name = "skill_template")
	private List<SkillTemplate> skillTemplates;
	private TIntObjectHashMap<SkillTemplate> skillData = new TIntObjectHashMap<SkillTemplate>();
	private TIntObjectHashMap<List<SkillTemplate>> delayData = new TIntObjectHashMap<List<SkillTemplate>>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		skillData.clear();
		delayData.clear();
		for(SkillTemplate skillTempalte: skillTemplates)
		{
			skillData.put(skillTempalte.getSkillId(), skillTempalte);
			
			List<SkillTemplate> sts = delayData.get(skillTempalte.getDelayId());
			if (sts == null)
				sts = new ArrayList<SkillTemplate>();
			sts.add(skillTempalte);
			delayData.put(skillTempalte.getDelayId(), sts);
		}
	}

	public SkillTemplate getSkillTemplate(int skillId)
	{
		return skillData.get(skillId);
	}
	
	public List<SkillTemplate> getSkillTemplatesForDelayId(int delayId)
	{
		return delayData.get(delayId);
	}

	public int size()
	{
		return skillData.size();
	}

	public List<SkillTemplate> getSkillTemplates()
	{
		return skillTemplates;
	}

	public void setSkillTemplates(List<SkillTemplate> skillTemplates)
	{
		this.skillTemplates = skillTemplates;
		afterUnmarshal(null, null);
	}
}