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

package gameserver.quest.handlers.models;

import gameserver.quest.QuestEngine;
import gameserver.quest.handlers.template.MonsterHunt;
import javolution.util.FastMap;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterHuntData", propOrder = { "monsterInfos" })
public class MonsterHuntData extends QuestScriptData
{
	@XmlElement(name = "monster_infos", required = true)
	protected List<MonsterInfo>	monsterInfos;
	@XmlAttribute(name = "start_npc_id")
	protected int startNpcId;
	@XmlAttribute(name = "end_npc_id")
	protected int endNpcId;
	@Override
	public void register(QuestEngine questEngine)
	{
		FastMap<Integer, MonsterInfo> monsterInfo = new FastMap<Integer, MonsterInfo>();
		for(MonsterInfo mi : monsterInfos) monsterInfo.put(mi.getNpcId(), mi);
		MonsterHunt template = new MonsterHunt(id, startNpcId, endNpcId, monsterInfo);
		questEngine.TEMP_HANDLERS.put(template.getQuestId(), template);
	}
}