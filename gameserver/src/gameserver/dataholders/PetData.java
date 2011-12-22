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

import gameserver.model.templates.pet.PetTemplate;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pets")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetData
{
	@XmlElement(name="pet")
	private List<PetTemplate> pts;
	
	private TIntObjectHashMap<PetTemplate> templates;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		templates = new TIntObjectHashMap<PetTemplate>();
		for(PetTemplate pt: pts)
		{
			templates.put(pt.getPetId(), pt);
		}
		pts = null;
	}
	
	public PetTemplate getPetTemplate(int petId)
	{
		return templates.get(petId);
	}
	
	public PetTemplate getPetTemplateByEggId(int eggId)
	{
		for (TIntObjectIterator<PetTemplate> it = templates.iterator();it.hasNext();)
		{
			it.advance();
			if (it.value().getEggId()==eggId)
				return it.value();
		}
		return null;
	}

	public int size()
	{
		return templates.size();
	}
}