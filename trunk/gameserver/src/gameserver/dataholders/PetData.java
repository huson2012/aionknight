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

import gameserver.model.templates.pet.PetTemplate;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pets")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetData
{
	@XmlElement(name="pet")
	private List<PetTemplate> pts;
	
	private TIntObjectHashMap<PetTemplate> templates;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		templates = new TIntObjectHashMap<PetTemplate>();
		for(PetTemplate pt: pts)
		{
			templates.put(pt.getPetId(), pt);
		}
		pts = null;
	}
	
	public PetTemplate getPetTemplate(int petId)
	{
		return templates.get(petId);
	}
	
	public PetTemplate getPetTemplateByEggId(int eggId)
	{
		for (TIntObjectIterator<PetTemplate> it = templates.iterator();it.hasNext();)
		{
			it.advance();
			if (it.value().getEggId()==eggId)
				return it.value();
		}
		return null;
	}

	public int size()
	{
		return templates.size();
	}
}