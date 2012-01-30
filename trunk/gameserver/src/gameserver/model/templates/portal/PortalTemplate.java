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

package gameserver.model.templates.portal;

import gameserver.model.Race;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Portal")
public class PortalTemplate
{
	@XmlAttribute(name = "npcid")
	protected int				npcId;
	@XmlAttribute(name = "name")
	protected String			name;
	@XmlAttribute(name = "instance")
	protected boolean			instance;
	@XmlAttribute(name = "minlevel")
	protected int				minLevel;
	@XmlAttribute(name = "maxlevel")
	protected int				maxLevel;
	@XmlAttribute(name = "group")
	protected boolean			group;
	@XmlAttribute(name = "race")
	protected Race				race;
	@XmlAttribute(name = "questreq")
	protected int				questReq;
	@XmlAttribute(name = "itemreq")
	protected int				itemReq;
	@XmlElement(name = "entrypoint")
	protected List<EntryPoint>	entryPoint;
	@XmlElement(name = "exitpoint")
	protected List<ExitPoint>	exitPoint;
    @XmlElement(name = "portalitem")
    protected List<PortalItem> portalItem;
	@XmlAttribute(name = "titleid")
	protected int				IdTitle;

	/**
	 * @return the npcId
	 */
	public int getNpcId()
	{
		return npcId;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the instance
	 */
	public boolean isInstance()
	{
		return instance;
	}

	/**
	 * @return the minLevel
	 */
	public int getMinLevel()
	{
		return minLevel;
	}

	/**
	 * @return the maxLevel
	 */
	public int getMaxLevel()
	{
		return maxLevel;
	}

	/**
	 * @return the group
	 */
	public boolean isGroup()
	{
		return group;
	}

	/**
	 * @return the race
	 */
	public Race getRace()
	{
		return race;
	}
	
	/**
	 * @return the quest requirements
	 */
	public int getQuestReq()
	{
		return questReq;
	}
	
	/**
	 * @return the item requirements
	 */
	public int getItemReq()
	{
		return itemReq;
	}

	/**
	 * @return the entryPoint
	 */
	public List<EntryPoint> getEntryPoint()
	{
		return entryPoint;
	}

	/**
	 * @return the exitPoint
	 */
	public List<ExitPoint> getExitPoint()
	{
		return exitPoint;
	}
    /**
     * @return the portalItem
     */
    public List<PortalItem> getPortalItem()
     {
   		if(portalItem == null)			
			portalItem = new ArrayList<PortalItem>();	
				
      return portalItem;
    }


	/**
	 * @return the Title Id
	 */
	public int getIdTitle()
	{
		return IdTitle;
	}	
}
