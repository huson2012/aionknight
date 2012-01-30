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

package gameserver.quest.handlers.models.xmlQuest;

import gameserver.model.gameobjects.Npc;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestNpc", propOrder = { "dialog" })
public class QuestNpc
{

	protected List<QuestDialog>	dialog;
	@XmlAttribute(required = true)
	protected int				id;
	public boolean operate(QuestCookie env, QuestState qs)
	{
		int npcId = -1;
		if(env.getVisibleObject() instanceof Npc)
			npcId = ((Npc) env.getVisibleObject()).getNpcId();
		if (npcId != id)
			return false;
		for (QuestDialog questDialog : dialog)
		{
			if (questDialog.operate(env, qs))
			return true;
		}
		return false;
	}
}
