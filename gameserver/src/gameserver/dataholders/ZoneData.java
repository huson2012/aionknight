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

import gameserver.model.templates.zone.ZoneTemplate;
import gameserver.world.zone.ZoneName;
import gnu.trove.THashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.Iterator;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "zones")
public class ZoneData implements Iterable<ZoneTemplate>
{
	@XmlElement(name = "zone")
	protected List<ZoneTemplate> zoneList;
	
	private THashMap<ZoneName, ZoneTemplate> zoneNameMap = new THashMap<ZoneName, ZoneTemplate>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(ZoneTemplate zone : zoneList)
		{
			zoneNameMap.put(zone.getName(), zone);
		}
	}
	
	@Override
	public Iterator<ZoneTemplate> iterator()
	{
		return zoneList.iterator();
	}
	
	public int size()
	{
		return zoneList.size();
	}
}