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

package gameserver.model.templates;

import gameserver.model.Race;
import gameserver.model.templates.guild.GuildQuests;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guildTemplate", propOrder = {"guildQuests"})

public class GuildTemplate
{
	@XmlElement(name = "guild_quests")
	protected GuildQuests		guildQuests;

    @XmlAttribute
    protected int id;
	
	@XmlAttribute
    protected int nameId;
	
	@XmlAttribute
	protected int npcId;
	
	@XmlAttribute
    protected int requiredLevel;

    /**
     * Gets the value of the guildQuests property.
     * 
     * @return
     *    possible object is
     *    {@link GuildQuests }
     *    
     */
    public GuildQuests getGuildQuests()
    {
        return guildQuests;
    }
    
    /**
     * Gets the value of the id property.  
     */
	public int getGuildId()
    {
        return id;
    }

    /**
     * Gets the value of the nameId property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public int getNameID()
    {
        return nameId;
    }
    
    /**
     * Gets the value of the npcId property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public int getNpcId()
    {
        return npcId;
    }
    
    /**
     * Gets the itemId of the requiredItem property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public int getRequiredLevel()
    {
        return requiredLevel;
    }
    
    public Race getGuildRace()
    {
    	Race race = null;
    	switch(this.id)
    	{
    		case 10:
    		case 11:
    		case 12:
    			race = Race.ELYOS;
    			break;
    		case 20:
    		case 21:
    		case 22:
    			race = Race.ASMODIANS;
    			break;
    	}
        return race;
    }
}