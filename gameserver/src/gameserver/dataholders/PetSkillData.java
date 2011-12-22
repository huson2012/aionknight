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

import gameserver.model.templates.petskill.PetSkillTemplate;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pet_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetSkillData
{
	@XmlElement(name = "pet_skill")
	private List<PetSkillTemplate> petSkills;

	private TIntObjectHashMap<TIntIntHashMap>	petSkillData	= new TIntObjectHashMap<TIntIntHashMap>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(PetSkillTemplate petSkill : petSkills)
		{
			TIntIntHashMap orderSkillMap = petSkillData.get(petSkill.getOrderSkill());
			if(orderSkillMap == null)
			{
				orderSkillMap = new TIntIntHashMap();
				petSkillData.put(petSkill.getOrderSkill(), orderSkillMap);
			}
				
			orderSkillMap.put(petSkill.getPetId(), petSkill.getSkillId());
		}		
	}
	
	public int size()
	{
		return petSkillData.size();
	}
	
	public int getPetOrderSkill(int orderSkill, int petNpcId)
	{
		try{
			return petSkillData.get(orderSkill).get(petNpcId);
		} catch(NullPointerException ex) {
			return -1;
		}
		
	}
}