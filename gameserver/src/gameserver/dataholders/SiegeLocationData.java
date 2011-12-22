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

import gameserver.model.siege.SiegeLocation;
import gameserver.model.templates.siege.SiegeLocationTemplate;
import org.apache.log4j.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "siege_locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeLocationData
{
	@XmlElement(name = "siege_location")
	private List<SiegeLocationTemplate> siegeLocationTemplates;

	private HashMap<Integer, SiegeLocation> siegeLocations = new HashMap<Integer, SiegeLocation>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		Logger.getLogger(SiegeLocationData.class).debug("After unmarshal in SiegeLocationData...");
		siegeLocations.clear();
		for (SiegeLocationTemplate template : siegeLocationTemplates)
		{
			Logger.getLogger(SiegeLocationData.class).debug("["+siegeLocations.size()+"/"+siegeLocationTemplates.size()+"]Loading SiegeLocation #"+template.getId()+" with type "+template.getType());
			switch(template.getType())
			{
				case FORTRESS:
					siegeLocations.put(template.getId(), new SiegeLocation(template));
					break;
				case ARTIFACT:
					siegeLocations.put(template.getId(), new SiegeLocation(template));
					break;
				case BOSSRAID_LIGHT:
				case BOSSRAID_DARK:
					siegeLocations.put(template.getId(), new SiegeLocation(template));
					break;
				default:
					break;
			}
			Logger.getLogger(SiegeLocationData.class).debug("now there is "+siegeLocations.size()+" sieges locations...");
		}
	}
	
	public int size()
	{
		return siegeLocations.size();
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations()
	{
		return siegeLocations;
	}
}