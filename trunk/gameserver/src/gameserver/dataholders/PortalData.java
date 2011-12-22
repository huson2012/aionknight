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

import gameserver.model.Race;
import gameserver.model.templates.portal.ExitPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@XmlRootElement(name = "portal_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class PortalData
{
	@XmlElement(name = "portal")
	private List<PortalTemplate> portals;
	private TIntObjectHashMap<PortalTemplate> portalData	= new TIntObjectHashMap<PortalTemplate>();
	private HashMap<Integer, ArrayList<PortalTemplate>> instancesMap = new HashMap<Integer, ArrayList<PortalTemplate>>();
	private HashMap<String, PortalTemplate> namedPortals = new HashMap<String, PortalTemplate>();
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		portalData.clear();
		instancesMap.clear();
		namedPortals.clear();
		
		for(PortalTemplate portal : portals)
		{
			portalData.put(portal.getNpcId(), portal);
			if(portal.isInstance())
			{
				for (ExitPoint exit : portal.getExitPoint())
				{
					int mapId = exit.getMapId();
					ArrayList<PortalTemplate> templates = instancesMap.get(mapId);
					if(templates == null)
					{
						templates = new ArrayList<PortalTemplate>();
						instancesMap.put(mapId, templates);
					}
					templates.add(portal);
				}
			}
			if(portal.getName() != null && !portal.getName().isEmpty())
				namedPortals.put(portal.getName(), portal);
		}
	}
	
	public int size()
	{
		return portalData.size();
	}

	public PortalTemplate getPortalTemplate(int npcId)
	{
		return portalData.get(npcId);
	}

	public PortalTemplate getInstancePortalTemplate(int worldId, Race race)
	{
		List<PortalTemplate> portals = instancesMap.get(worldId);
		
		if (portals == null)
		{

			return null;
		}
		
		for(PortalTemplate portal : portals)
		{
			if(portal.getRace() == null || portal.getRace().equals(race))
				return portal;
		}

		return null;
	}

	public PortalTemplate getTemplateByNameAndWorld(int worldId, String name)
	{
		PortalTemplate portal = namedPortals.get(name);

		if(portal == null)
			return null;
		
		for (ExitPoint point : portal.getExitPoint())
		{
			if(portal != null && point.getMapId() != worldId)
				throw new IllegalArgumentException("Invalid combination of world and name: " + worldId + " " + name);	
		}	
		return portal;
	}

	public List<PortalTemplate> getPortals()
	{
		return portals;
	}

	public void setPortals(List<PortalTemplate> portals)
	{
		this.portals = portals;
		afterUnmarshal(null, null);
	}
}