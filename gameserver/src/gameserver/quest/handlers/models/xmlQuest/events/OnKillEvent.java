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

package gameserver.quest.handlers.models.xmlQuest.events;

import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.handlers.models.MonsterInfo;
import gameserver.quest.handlers.models.xmlQuest.operations.QuestOperations;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnKillEvent", propOrder = { "monsterInfos", "complite" })
public class OnKillEvent extends QuestEvent
{

	@XmlElement(name = "monster_infos")
	protected List<MonsterInfo>	monsterInfos;
	protected QuestOperations	complite;

	public List<MonsterInfo> getMonsterInfos()
	{
		if(monsterInfos == null)
		{
			monsterInfos = new ArrayList<MonsterInfo>();
		}
		return this.monsterInfos;
	}

	public boolean operate(QuestCookie env)
	{
		if(monsterInfos == null || !(env.getVisibleObject() instanceof Npc))
			return false;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(env.getQuestId());
		if(qs == null)
			return false;

		Npc npc = (Npc) env.getVisibleObject();
		for(MonsterInfo monsterInfo : monsterInfos)
		{
			if(monsterInfo.getNpcId() == npc.getNpcId())
			{
				int var = qs.getQuestVarById(monsterInfo.getVarId());
				if(var >= (monsterInfo.getMinVarValue() == null ? 0 : monsterInfo.getMinVarValue()) && var < monsterInfo.getMaxKill())
				{
					qs.setQuestVarById(monsterInfo.getVarId(), var + 1);
					PacketSendUtility.sendPacket(env.getPlayer(), new SM_QUEST_ACCEPTED(2, env.getQuestId(), qs.getStatus(),
						qs.getQuestVars().getQuestVars()));
				}
			}
		}

		if(complite != null)
		{
			for(MonsterInfo monsterInfo : monsterInfos)
			{
				if(qs.getQuestVarById(monsterInfo.getVarId()) != qs.getQuestVarById(monsterInfo.getVarId()))
					return false;
			}
			complite.operate(env);
		}
		return false;
	}
}
