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

package gameserver.quest.handlers.models.xmlQuest.operations;

import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestOperations", propOrder = { "operations" })
public class QuestOperations
{

    @XmlElements({
        @XmlElement(name = "take_item", type = TakeItemOperation.class),
        @XmlElement(name = "npc_dialog", type = NpcDialogOperation.class),
        @XmlElement(name = "set_quest_status", type = SetQuestStatusOperation.class),
        @XmlElement(name = "give_item", type = GiveItemOperation.class),
        @XmlElement(name = "start_quest", type = StartQuestOperation.class),
        @XmlElement(name = "npc_use", type = ActionItemUseOperation.class),
        @XmlElement(name = "set_quest_var", type = SetQuestVarOperation.class),
        @XmlElement(name = "collect_items", type = CollectItemQuestOperation.class)
    })
	protected List<QuestOperation>	operations;
	@XmlAttribute
	protected Boolean override;

	/**
	 * Gets the value of the override property.
	 * @return possible object is {@link Boolean }
	 */
	public boolean isOverride()
	{
		if(override == null)
		{
			return true;
		}
		else
		{
			return override;
		}
	}

	public boolean operate(QuestCookie env)
	{
		if(operations != null)
		{
			for(QuestOperation oper : operations)
			{
				oper.doOperate(env);
			}
		}
		return isOverride();
	}
}