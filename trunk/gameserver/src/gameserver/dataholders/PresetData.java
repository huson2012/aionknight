/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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