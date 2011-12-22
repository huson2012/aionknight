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

import gameserver.model.templates.preset.PresetTemplate;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "custom_presets")
@XmlType(name = "", propOrder = { "presets" })
@XmlAccessorType(XmlAccessType.FIELD)
public class PresetData
{
	@XmlElement(name = "preset", required = true)
	private List<PresetData.Preset> presets;
	
	@XmlTransient
	private Map<String, PresetTemplate> presetByName;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		presetByName = new HashMap<String, PresetTemplate>();
		for(PresetTemplate it : presets)
		{
			presetByName.put(it.getName(), it);
		}
		
		presets.clear();
		presets = null;
	}
	
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Preset extends PresetTemplate
    {
    }
	
	public int size ()
	{
		return presetByName.size();
	}
	
	public PresetTemplate getPresetTemplate(String name)
	{
		name = name.toUpperCase();
		if (presetByName.containsKey(name))
			return presetByName.get(name);
		return null;
	}
	
	public Collection<PresetTemplate> getPresetTemplates()
	{
		return presetByName.values();
	}
}