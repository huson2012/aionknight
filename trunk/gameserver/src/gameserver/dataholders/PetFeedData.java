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

import gameserver.model.templates.pet.FoodGroups;
import gameserver.model.templates.pet.FoodType;
import gameserver.model.templates.pet.PetFlavour;
import gameserver.model.templates.pet.PetRewards;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "foodGroups", "flavours" })
@XmlRootElement(name = "pet_feed")

public class PetFeedData
{
	@XmlElement(name="groups", required = true)
	protected FoodGroups foodGroups;
	
	@XmlElement(name="flavour", required = true)
	protected List<PetFeedData.Flavour> flavours;

	public FoodGroups getFoodGroups() 
	{
		return foodGroups;
	}

	public List<PetFeedData.Flavour> getFlavours() 
	{
		if (flavours == null)
			flavours = new ArrayList<PetFeedData.Flavour>();

		return this.flavours;
	}

	public synchronized PetFlavour getFlavour(int flavour, int feedItemId)
	{
		List<FoodType> foodTypes = foodGroups.getFoodTypes(feedItemId);
		if (foodTypes.size() == 0 || PetFlavour.isReward(feedItemId))
			return null;

		for (PetFlavour f : flavours)
		{
			if (f.getId() != flavour)
				continue;
			
			List<PetRewards> rewards = null;
			for (FoodType foodType : foodTypes)
			{
				rewards = f.getRewards(foodType);
				if (rewards.size() != 0 && rewards.get(0).getResults().size() != 0)
					return f;
			}
		}
		
		return null;
	}

	public int size()
	{
		return getFlavours().size();
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Flavour extends PetFlavour
	{
	}
}