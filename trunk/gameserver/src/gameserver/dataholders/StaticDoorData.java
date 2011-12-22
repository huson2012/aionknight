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

import gameserver.model.templates.staticdoor.StaticDoorTemplate;
import gnu.trove.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "staticdoor_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticDoorData
{
	@XmlElement(name = "staticdoor")
	private List<StaticDoorTemplate> staticDoors;
	
	/** A map containing all door templates */
	private TIntObjectHashMap<StaticDoorTemplate> staticDoorData	= new TIntObjectHashMap<StaticDoorTemplate>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		staticDoorData.clear();
		
		for(StaticDoorTemplate staticDoor : staticDoors)
		{
			staticDoorData.put(staticDoor.getDoorId(), staticDoor);
		}
	}
	
	public int size()
	{
		return staticDoorData.size();
	}
	
	/**
	 * 
	 * @param doorId
	 * @return
	 */
	public StaticDoorTemplate getStaticDoorTemplate(int doorId)
	{
		return staticDoorData.get(doorId);
	}

	/**
	 * @return the static doors
	 */
	public List<StaticDoorTemplate> getStaticDoors()
	{
		return staticDoors;
	}

	/**
	 *
	 */
	public void setStaticDoors(List<StaticDoorTemplate> staticDoors)
	{
		this.staticDoors = staticDoors;
		afterUnmarshal(null, null);
	}
}