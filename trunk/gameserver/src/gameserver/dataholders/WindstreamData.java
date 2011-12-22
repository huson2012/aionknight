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

import gameserver.model.templates.windstreams.WindstreamTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "windstreams")
public class WindstreamData
{
	@XmlElement(name="windstream")
	private List<WindstreamTemplate> wts;
	
	private TIntObjectHashMap<WindstreamTemplate> windstreams;
		
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		windstreams = new TIntObjectHashMap<WindstreamTemplate>();
		for(WindstreamTemplate wt: wts)
		{
			windstreams.put(wt.getMapid(), wt);
		}
		wts = null;
	}
	
	public WindstreamTemplate getStreamTemplate(int mapId)
	{
		return windstreams.get(mapId);
	}

	public int size()
	{
		return windstreams.size();
	}
}