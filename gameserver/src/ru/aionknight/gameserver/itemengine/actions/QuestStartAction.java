package ru.aionknight.gameserver.itemengine.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.gameobjects.Item;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.QuestTemplate;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.utils.PacketSendUtility;


/**
 * @author Nemiroff
 *         Date: 17.12.2009
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStartAction")
public class QuestStartAction extends AbstractItemAction {

	protected int questid;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		QuestState qs = player.getQuestStateList().getQuestState(questid);
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questid);
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat(template.getMaxRepeatCount()))
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 4, questid));
	}
    
    public int getQuestId()
    {
    	return questid;
    }
    
    public void setQuestId(int questId)
    {
    	questid = questId;
    }
}
