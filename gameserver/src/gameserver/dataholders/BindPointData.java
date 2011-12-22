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

import gameserver.model.templates.BindPointTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "bind_points")
@XmlAccessorType(XmlAccessType.FIELD)
public class BindPointData
{
	@XmlElement(name = "bind_point")
	private List<BindPointTemplate> bplist;
	private TIntObjectHashMap<BindPointTemplate> bindplistData	= new TIntObjectHashMap<BindPointTemplate>();
	private TIntObjectHashMap<BindPointTemplate> bindplistData2	= new TIntObjectHashMap<BindPointTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(BindPointTemplate bind: bplist)
		{
			bindplistData.put(bind.getNpcId(), bind);
			bindplistData2.put(bind.getBindId(), bind);
		}
	}

	public int size()
	{
		return bindplistData.size();
	}

	public BindPointTemplate getBindPointTemplate(int npcId)
	{
		return bindplistData.get(npcId);
	}

	public BindPointTemplate getBindPointTemplate2(int bindPointId)
	{
		return bindplistData2.get(bindPointId);
	}
}