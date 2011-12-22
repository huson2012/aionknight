/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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