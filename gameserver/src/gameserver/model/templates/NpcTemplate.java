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

import gameserver.model.NpcType;
import gameserver.model.Race;
import gameserver.model.items.NpcEquippedGear;
import gameserver.model.templates.stats.KiskStatsTemplate;
import gameserver.model.templates.stats.NpcRank;
import gameserver.model.templates.stats.NpcStatsTemplate;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "npc_template")
public class NpcTemplate extends VisibleObjectTemplate
{
	private int					npcId;
	@XmlAttribute(name = "level", required = true)
	private byte				level;
	@XmlAttribute(name = "name_id", required = true)
	private int					nameId;
	@XmlAttribute(name = "title_id")
	private int					titleId;
	@XmlAttribute(name = "name")
	private String				name;
	@XmlAttribute(name = "height")
	private float				height			= 1;
	@SuppressWarnings("unused")
	@XmlAttribute(name = "talking_distance")
	private int					talkingDistance	= 2;
	@XmlAttribute(name = "npc_type", required = true)
	private NpcType				npcType;
	@XmlElement(name = "stats")
	private NpcStatsTemplate	statsTemplate;
	@XmlElement(name = "equipment")
	private NpcEquippedGear		equipment;
	@XmlElement(name = "kisk_stats")
	private KiskStatsTemplate	kiskStatsTemplate;
	@SuppressWarnings("unused")
	@XmlElement(name = "ammo_speed")
	private int					ammoSpeed		= 0;
	@XmlAttribute(name = "rank")
	private NpcRank				rank;
	@XmlAttribute(name = "srange")
	private int					aggrorange;
	@XmlAttribute(name = "arange")
	private int					attackRange;
	@XmlAttribute(name = "srange")
	private int					attackRate;
	@XmlAttribute(name = "hpgauge")
	private int					hpGauge;
	@XmlAttribute(name = "tribe")
	private String				tribe;
	@XmlAttribute
	private Race				race;
	@XmlAttribute
	private int					state;
	
	@Override
	public int getTemplateId()
	{
		return npcId;
	}

	@Override
	public int getNameId()
	{
		return nameId;
	}

	public int getTitleId()
	{
		return titleId;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return float
	 */
	public float getHeight()
	{
		return height;
	}

	public NpcType getNpcType()
	{
		return npcType;
	}

	public NpcEquippedGear getEquipment()
	{
		return equipment;
	}

	public byte getLevel()
	{
		return level;
	}
	
	public void setLevel(byte level)
	{
		this.level = level;
	}

	/**
	 * @return the statsTemplate
	 */
	public NpcStatsTemplate getStatsTemplate()
	{
		return statsTemplate;
	}

	/**
	 * @param statsTemplate the statsTemplate to set
	 */
	public void setStatsTemplate(NpcStatsTemplate statsTemplate)
	{
		this.statsTemplate = statsTemplate;
	}
	
	public KiskStatsTemplate getKiskStatsTemplate()
	{
		return kiskStatsTemplate;
	}
	
	/**
	 * @return the tribe
	 */
	public String getTribe()
	{
		return tribe;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Npc Template id: " + npcId + " name: " + name;
	}

	
	@SuppressWarnings("unused")
	@XmlID
	@XmlAttribute(name = "npc_id", required = true)
	private void setXmlUid(String uid)
	{
		/**
		 * This method is used only by JAXB unmarshaller.
		 * I couldn't set annotations at field, because
		 * ID must be a string. 
		 */
		npcId = Integer.parseInt(uid);
	}

	/**
	 * @return the rank
	 */
	public NpcRank getRank()
	{
		return rank;
	}
	
	public int getAggroRange()
	{
		return aggrorange;
	}

	/**
	 * @return the attackRange
	 */
	public int getAttackRange()
	{
		return attackRange;
	}

	/**
	 * @return the attackRate
	 */
	public int getAttackRate()
	{
		return attackRate;
	}

	/**
	 * @return the hpGauge
	 */
	public int getHpGauge()
	{
		return hpGauge;
	}

	/**
	 * @return the race
	 */
	public Race getRace()
	{
		return race;
	}

	/**
	 * @return the state
	 */
	public int getState()
	{
		return state;
	}
}