/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.dataholders;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ae_static_data")
@XmlAccessorType(XmlAccessType.NONE)
public class StaticData
{
	@XmlElement(name = "world_maps")
	public WorldMapsData			worldMapsData;

	@XmlElement(name = "npc_trade_list")
	public TradeListData			tradeListData;

	@XmlElement(name = "npc_teleporter")
	public TeleporterData			teleporterData;

	@XmlElement(name = "teleport_location")
	public TeleLocationData			teleLocationData;
	
	@XmlElement(name = "custom_presets")
	public PresetData				customPresets;

	@XmlElement(name = "bind_points")
	public BindPointData			bindPointData;

	@XmlElement(name = "quests")
	public QuestsData				questData;

	@XmlElement(name = "bonuses")
	public BonusData				bonusData;

	@XmlElement(name = "quest_scripts")
	public QuestScriptsData			questsScriptData;

	@XmlElement(name = "player_experience_table")
	public PlayerExperienceTable	playerExperienceTable;

	@XmlElement(name = "player_stats_templates")
	public PlayerStatsData			playerStatsData;
	
	@XmlElement(name = "summon_stats_templates")
	public SummonStatsData			summonStatsData;

	@XmlElement(name = "item_templates")
	public ItemData					itemData;

	@XmlElement(name = "wrapped_items")
	public WrappedItemData			wrappedItemData;
	
	@XmlElement(name = "pet_feed")
	public PetFeedData				petFeedData;

	@XmlElement(name = "windstreams")
	public WindstreamData			windstreamsData;
	
	@XmlElement(name = "npc_templates")
	public NpcData					npcData;

	@XmlElement(name = "player_initial_data")
	public PlayerInitialData		playerInitialData;

	@XmlElement(name = "skill_data")
	public SkillData				skillData;

	@XmlElement(name = "skill_tree")
	public SkillTreeData			skillTreeData;

	@XmlElement(name = "cube_expander")
	public CubeExpandData			cubeExpandData;

	@XmlElement(name = "warehouse_expander")
	public WarehouseExpandData		warehouseExpandData;

	@XmlElement(name = "player_titles")
	public TitleData				titleData;

	@XmlElement(name = "gatherable_templates")
	public GatherableData			gatherableData;
	
	@XmlElement(name = "guild_templates")
	public GuildsData			guildsData;

	@XmlElement(name = "npc_walker")
	public WalkerData				walkerData;

	@XmlElement(name = "zones")
	public ZoneData					zoneData;
	
	@XmlElement(name = "flight_zones")
	public FlightZoneData			flightZoneData;

	@XmlElement(name = "goodslists")
	public GoodsListData			goodsListData;

	@XmlElement(name = "spawns")
	public SpawnsData				spawnsData;

	@XmlElement(name = "tribe_relations")
	public TribeRelationsData		tribeRelationsData;

	@XmlElement(name = "recipe_templates")
	public RecipeData				recipeData;

	@XmlElement(name = "portal_templates")
	public PortalData				portalData;
	
	@XmlElement(name = "chest_templates")
	public ChestData				chestData;

	@XmlElement(name = "item_sets")
	public ItemSetData				itemSetData;

	@XmlElement(name = "npc_skill_templates")
	public NpcSkillData				npcSkillData;
	
	@XmlElement(name = "pet_skill_templates")
	public PetSkillData				petSkillData;

	@XmlElement(name = "siege_locations")
	public SiegeLocationData		siegeLocationData;
	
	@XmlElement(name = "siege_spawns")
	public SiegeSpawnData			siegeSpawnData;
	
	@XmlElement(name = "shields")
	public ShieldData				shieldData;
	
	@XmlElement(name = "fly_rings")
	public FlyRingData				flyRingData;
	
	@XmlElement(name = "pets")
	public PetData					petData;
	
	@XmlElement(name = "roads")
	public RoadData                roadData;
	
	@XmlElement(name = "droplist")
	public DroplistData				droplistData;
	
	@XmlElement(name = "npc_shouts")
	public NpcShoutsData 			npcShoutsData;
	
	@XmlElement(name = "levelup_surveys")
	public LevelUpSurveyData		levelUpSurveys;

	@XmlElement(name = "staticdoor_templates")
	public StaticDoorData			staticDoorData;

	@SuppressWarnings("unused")
	private void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
	{
		DataManager.log.info("Loaded world maps data: " + worldMapsData.size() + " maps");
		DataManager.log.info("Loaded player exp table: " + playerExperienceTable.getMaxLevel() + " levels");
		DataManager.log.info("Loaded " + playerStatsData.size() + " player stat templates");
		DataManager.log.info("Loaded " + summonStatsData.size() + " summon stat templates");
		DataManager.log.info("Loaded " + itemData.size() + " item templates");
		DataManager.log.info("Loaded " + wrappedItemData.size() + " item wrappers");
		DataManager.log.info("Loaded " + petFeedData.size() + " pet flavours");
		DataManager.log.info("Loaded " + npcData.size() + " npc templates");
		DataManager.log.info("Loaded " + playerInitialData.size() + " initial player templates");
		DataManager.log.info("Loaded " + tradeListData.size() + " trade lists");
		DataManager.log.info("Loaded " + teleporterData.size() + " npc teleporter templates");
		DataManager.log.info("Loaded " + teleLocationData.size() + " teleport locations");
		DataManager.log.info("Loaded " + customPresets.size() + " preset entries");
		DataManager.log.info("Loaded " + skillData.size() + " skill templates");
		DataManager.log.info("Loaded " + skillTreeData.size() + " skill learn entries");
		DataManager.log.info("Loaded " + cubeExpandData.size() + " cube expand entries");
		DataManager.log.info("Loaded " + warehouseExpandData.size() + " warehouse expand entries");
		DataManager.log.info("Loaded " + bindPointData.size() + " bind point entries");
		DataManager.log.info("Loaded " + questData.size() + " quest data entries");
		DataManager.log.info("Loaded " + bonusData.size() + " quest bonus data entries");
		DataManager.log.info("Loaded " + gatherableData.size() + " gatherable entries");
		DataManager.log.info("Loaded " + guildsData.size() + " guild entries");
		DataManager.log.info("Loaded " + titleData.size() + " title entries");
		DataManager.log.info("Loaded " + walkerData.size() + " walker routes");
		DataManager.log.info("Loaded " + zoneData.size() + " zone entries");
		DataManager.log.info("Loaded " + flightZoneData.size() + " flightzone entries");
		DataManager.log.info("Loaded " + goodsListData.size() + " goodslist entries");
		DataManager.log.info("Loaded " + spawnsData.size() + " spawn entries");
		DataManager.log.info("Loaded " + tribeRelationsData.size() + " tribe relation entries");
		DataManager.log.info("Loaded " + recipeData.size() + " recipe entries");
		DataManager.log.info("Loaded " + portalData.size() + " portal entries");
		DataManager.log.info("Loaded " + chestData.size() + " chest entries");
		DataManager.log.info("Loaded " + itemSetData.size() + " item set entries");
		DataManager.log.info("Loaded " + npcSkillData.size() + " npc skill list entries");
		DataManager.log.info("Loaded " + petSkillData.size() + " pet skill list entries");
		DataManager.log.info("Loaded " + siegeLocationData.size() + " siege location entries");
		DataManager.log.info("Loaded " + siegeSpawnData.size() + " siege spawn entries");
		DataManager.log.info("Loaded " + shieldData.size() + " shield entries");
		DataManager.log.info("Loaded " + flyRingData.size() + " fly ring entries");
		DataManager.log.info("Loaded " + petData.size() + " pet entries");
		DataManager.log.info("Loaded " + windstreamsData.size() + " windstream entries");
		DataManager.log.info("Loaded " + droplistData.size() + " npc drops");
		DataManager.log.info("Loaded " + npcShoutsData.size() + " npc shouts");
		DataManager.log.info("Loaded " + roadData.size() + " roads entries");
		DataManager.log.info("Loaded " + levelUpSurveys.size() + " level up surveys");
		DataManager.log.info("Loaded " + staticDoorData.size() + " static door locations");	
	}
}