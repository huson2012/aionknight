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

package gameserver.model.gameobjects;

import gameserver.model.Race;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.survey.SurveyItem;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SurveyTemplate", propOrder = { "title", "message", "select_text", "item" })
public class Survey
{
	@XmlTransient
	private int survey_id;
	
	@XmlTransient
	private int player_id;
	
	@XmlElement(required = true)
	private String title;
	
    @XmlElement(required = true)
	private String message;
    
    @XmlElement(name="select", required = true)
	private String select_text;
    
    @XmlElement(name="item", required = true)
	private SurveyItem item;
    
    @XmlAttribute(name = "level", required = true)
	private int level;
    
    @XmlAttribute(name = "race")
	private ItemRace race = ItemRace.ALL;
    
	@XmlTransient
	private long itemExistTime;
	
	@XmlTransient
	private int itemTradeTime;

	public Survey()
	{
	}
	
	public Survey(int survey_id, int player_id, String title, String message, String select_text, int itemId, int itemCount, long itemExistTime, int itemTradeTime)
	{
		this.survey_id = survey_id;
		this.player_id = player_id;
		this.title= title;
		this.message = message;
		this.select_text = select_text;
		this.item = new SurveyItem(itemId, itemCount);
		this.itemExistTime = itemExistTime;
		this.itemTradeTime = itemTradeTime;
	}

	public int getSurveyId()
	{
		return survey_id;
	}

	public int getPlayerId()
	{
		return player_id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getMessage()
	{
		return message;
	}

	public String getSelectText()
	{
		return select_text;
	}

	public int getItemId()
	{
		return item.getId();
	}

	public int getItemCount()
	{
		return item.getCount();
	}
	
	public long getItemExistTime()
	{
		return itemExistTime;
	}
	
	public int getItemTradeTime()
	{
		return itemTradeTime;
	}

	/**
	 * @return the level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @return the race
	 */
	public Race getRace()
	{
		if (race == ItemRace.ALL)
			return Race.PC_ALL;
		else if (race == ItemRace.ASMODIANS)
			return Race.ASMODIANS;
		else
			return Race.ELYOS;
	}
}
