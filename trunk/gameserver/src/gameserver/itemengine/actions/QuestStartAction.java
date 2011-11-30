/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.itemengine.actions;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

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