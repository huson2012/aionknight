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

import gameserver.model.items.WrapperItem;
import gameserver.model.templates.item.ItemRace;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "wrapped_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedItemData
{
	
	@XmlElement(required = true, name = "wrapper_item")
	protected List<WrapperItem> list;

	private TIntObjectHashMap<WrapperItem> wrappedItemData;

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		wrappedItemData = new TIntObjectHashMap<WrapperItem>();
		for(WrapperItem it : list)
		{
			wrappedItemData.put(it.getItemId(), it);
		}
		list = null;
	}

	public int size()
	{
		return wrappedItemData.size();
	}
	
	public WrapperItem getItemWrapper(int itemId)
	{
		return wrappedItemData.get(itemId);
	}
	
	public TIntIntHashMap rollItems(int wrapperItemId, int playerLevel, ItemRace race)
	{
		TIntIntHashMap itemCountMap = new TIntIntHashMap();
		
		final WrapperItem wrapperItem = wrappedItemData.get(wrapperItemId);
		if (wrapperItem == null)
			return itemCountMap;
		
		return wrapperItem.rollItems(playerLevel, race);
	}	
}