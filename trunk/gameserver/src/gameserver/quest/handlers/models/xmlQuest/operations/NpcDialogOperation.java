/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.quest.handlers.models.xmlQuest.operations;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.model.QuestCookie;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NpcDialogOperation")
public class NpcDialogOperation extends QuestOperation
{
	@XmlAttribute(required = true)
	protected int		id;
	@XmlAttribute(name = "quest_id")
	protected Integer	questId;

	@Override
	public void doOperate(QuestCookie env)
	{
        Player player = env.getPlayer();
        VisibleObject obj = env.getVisibleObject();
        int qId = env.getQuestId();
        if (questId != null)
                qId = questId;
        if (qId == 0)
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(obj.getObjectId(), id));
        else
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(obj.getObjectId(), id, qId));
	}
}
