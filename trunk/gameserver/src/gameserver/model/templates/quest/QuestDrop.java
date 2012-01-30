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

package gameserver.model.templates.quest;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestDrop")
public class QuestDrop
{

	@XmlAttribute(name = "npc_id")
	protected Integer	npcId;
	@XmlAttribute(name = "item_id")
	protected Integer	itemId;
	@XmlAttribute
	protected Integer	chance;
	@XmlAttribute(name = "drop_each_member")
	protected Boolean	dropEachMember;
	@XmlAttribute(name = "mentor")
	private Boolean	mentor;

	@XmlTransient
	protected Integer	questId;

	/**
	 * Gets the value of the npcId property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getNpcId()
	{
		return npcId;
	}

	/**
	 * Gets the value of the itemId property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getItemId()
	{
		return itemId;
	}

	/**
	 * Gets the value of the chance property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getChance()
	{
		return chance;
	}

	/**
	 * Gets the value of the dropEachMember property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isDropEachMember()
	{
		return dropEachMember;
	}

	/**
	 * @return the questId
	 */
	public Integer getQuestId()
	{
		return questId;
	}

	/**
	 * @param questId
	 *           the questId to set
	 */
	public void setQuestId(Integer questId)
	{
		this.questId = questId;
	}

	/**
	 * @return the mentor
	 */
	protected Boolean IsMentor()
	{
		if (mentor == null)
			return false;
		return mentor;
	}

}
