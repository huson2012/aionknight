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

import gameserver.model.templates.chest.ChestTemplate;
import gnu.trove.THashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "chest_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChestData
{
	@XmlElement(name = "chest")
	private List<ChestTemplate> chests;
	private TIntObjectHashMap<ChestTemplate> chestData	= new TIntObjectHashMap<ChestTemplate>();
	private TIntObjectHashMap<ArrayList<ChestTemplate>> instancesMap = new TIntObjectHashMap<ArrayList<ChestTemplate>>();
	private THashMap<String, ChestTemplate> namedChests = new THashMap<String, ChestTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		chestData.clear();
		instancesMap.clear();
		namedChests.clear();
		
		for(ChestTemplate chest : chests)
		{
			chestData.put(chest.getNpcId(), chest);
			if(chest.getName() != null && !chest.getName().isEmpty())
				namedChests.put(chest.getName(), chest);
		}
	}
	
	public int size()
	{
		return chestData.size();
	}

	public ChestTemplate getChestTemplate(int npcId)
	{
		return chestData.get(npcId);
	}

	public List<ChestTemplate> getChests()
	{
		return chests;
	}

	public void setChests(List<ChestTemplate> chests)
	{
		this.chests = chests;
		afterUnmarshal(null, null);
	}
}