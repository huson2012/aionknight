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

package gameserver.dataholders;

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.stats.CalculatedPlayerStatsTemplate;
import gameserver.model.templates.stats.PlayerStatsTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "player_stats_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerStatsData
{
	@XmlElement(name = "player_stats", required = true)
	private List<PlayerStatsType> templatesList = new ArrayList<PlayerStatsType>();
	
	private final TIntObjectHashMap<PlayerStatsTemplate> playerTemplates = new TIntObjectHashMap<PlayerStatsTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (PlayerStatsType pt : templatesList)
		{
			int code = makeHash(pt.getRequiredPlayerClass(), pt.getRequiredLevel());
			playerTemplates.put(code, pt.getTemplate());
		}
		playerTemplates.put(makeHash(PlayerClass.WARRIOR, 0), new CalculatedPlayerStatsTemplate(PlayerClass.WARRIOR));
		playerTemplates.put(makeHash(PlayerClass.ASSASSIN, 0), new CalculatedPlayerStatsTemplate(PlayerClass.ASSASSIN));
		playerTemplates.put(makeHash(PlayerClass.CHANTER, 0), new CalculatedPlayerStatsTemplate(PlayerClass.CHANTER));
		playerTemplates.put(makeHash(PlayerClass.CLERIC, 0), new CalculatedPlayerStatsTemplate(PlayerClass.CLERIC));
		playerTemplates.put(makeHash(PlayerClass.GLADIATOR, 0), new CalculatedPlayerStatsTemplate(PlayerClass.GLADIATOR));
		playerTemplates.put(makeHash(PlayerClass.MAGE, 0), new CalculatedPlayerStatsTemplate(PlayerClass.MAGE));
		playerTemplates.put(makeHash(PlayerClass.PRIEST, 0), new CalculatedPlayerStatsTemplate(PlayerClass.PRIEST));
		playerTemplates.put(makeHash(PlayerClass.RANGER, 0), new CalculatedPlayerStatsTemplate(PlayerClass.RANGER));
		playerTemplates.put(makeHash(PlayerClass.SCOUT, 0), new CalculatedPlayerStatsTemplate(PlayerClass.SCOUT));
		playerTemplates.put(makeHash(PlayerClass.SORCERER, 0), new CalculatedPlayerStatsTemplate(PlayerClass.SORCERER));
		playerTemplates.put(makeHash(PlayerClass.SPIRIT_MASTER, 0), new CalculatedPlayerStatsTemplate(PlayerClass.SPIRIT_MASTER));
		playerTemplates.put(makeHash(PlayerClass.TEMPLAR, 0), new CalculatedPlayerStatsTemplate(PlayerClass.TEMPLAR));
		
		templatesList.clear();
		templatesList = null;
	}

	public PlayerStatsTemplate getTemplate(Player player)
	{
		PlayerStatsTemplate template = getTemplate(player.getCommonData().getPlayerClass(), player.getLevel());
		if(template == null)
			template = getTemplate(player.getCommonData().getPlayerClass(), 0);
		return template;
	}

	public PlayerStatsTemplate getTemplate(PlayerClass playerClass, int level)
	{
		PlayerStatsTemplate template =  playerTemplates.get(makeHash(playerClass, level));
		if(template == null)
			template = getTemplate(playerClass, 0);
		return template;
	}

	public int size()
	{
		return playerTemplates.size();
	}
	
	@XmlRootElement(name="playerStatsTemplateType")
	private static class PlayerStatsType
	{
		@XmlAttribute(name = "class", required = true)
		private PlayerClass requiredPlayerClass;
		
		@XmlAttribute(name = "level", required = true)
		private int requiredLevel;

		@XmlElement(name="stats_template")
		private PlayerStatsTemplate template;

		public PlayerClass getRequiredPlayerClass()
		{
			return requiredPlayerClass;
		}

		public int getRequiredLevel()
		{
			return requiredLevel;
		}

		public PlayerStatsTemplate getTemplate()
		{
			return template;
		}
	}

	private static int makeHash(PlayerClass playerClass, int level)
	{
		return level << 8 | playerClass.ordinal();
	}
}