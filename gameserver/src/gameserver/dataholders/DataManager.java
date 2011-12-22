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

import gameserver.dataholders.loadingutils.XmlDataLoader;
import gameserver.utils.Util;
import org.apache.log4j.Logger;

public final class DataManager
{
	static Logger log = Logger.getLogger(DataManager.class);
	
	public static NpcData 				NPC_DATA;
	public static GatherableData		GATHERABLE_DATA;
	public static GuildsData			GUILDS_DATA;
	public static SpawnsData			SPAWNS_DATA;
	public static WorldMapsData			WORLD_MAPS_DATA;
	public static TradeListData 		TRADE_LIST_DATA;
	public static PlayerExperienceTable	PLAYER_EXPERIENCE_TABLE;
	public static TeleporterData 		TELEPORTER_DATA;
	public static TeleLocationData 		TELELOCATION_DATA;
	public static PresetData 			CUSTOM_PRESET_DATA;
	public static CubeExpandData 		CUBEEXPANDER_DATA;
	public static WarehouseExpandData 	WAREHOUSEEXPANDER_DATA;
	public static BindPointData 		BIND_POINT_DATA;
	public static QuestsData 			QUEST_DATA;
	public static BonusData 			BONUS_DATA;
	public static QuestScriptsData 		QUEST_SCRIPTS_DATA;
	public static PlayerStatsData 		PLAYER_STATS_DATA;
	public static SummonStatsData 		SUMMON_STATS_DATA;
	public static ItemData 				ITEM_DATA;
	public static WrappedItemData 		WRAPPED_ITEM_DATA;
	public static PetFeedData 			PET_FEED_DATA;
	public static RoadData 				ROAD_DATA;
	public static TitleData				TITLE_DATA;
	public static WindstreamData 		WINDSTREAM_DATA;	
	public static PlayerInitialData		PLAYER_INITIAL_DATA;
	public static SkillData				SKILL_DATA;
	public static SkillTreeData			SKILL_TREE_DATA;
	public static WalkerData			WALKER_DATA;
	public static ZoneData				ZONE_DATA;
	public static FlightZoneData		FLIGHT_ZONE_DATA;
	public static GoodsListData			GOODSLIST_DATA;
	public static TribeRelationsData	TRIBE_RELATIONS_DATA;
	public static RecipeData			RECIPE_DATA;
	public static PortalData			PORTAL_DATA;
	public static ChestData				CHEST_DATA;
	public static ItemSetData			ITEM_SET_DATA;
	public static NpcSkillData			NPC_SKILL_DATA;
	public static PetSkillData			PET_SKILL_DATA;
	public static SiegeLocationData		SIEGE_LOCATION_DATA;
	public static SiegeSpawnData		SIEGE_SPAWN_DATA;
	public static ShieldData			SHIELD_DATA;
	public static FlyRingData			FLY_RING_DATA;
	public static PetData				PET_DATA;
	public static DroplistData			DROPLIST_DATA;
	public static NpcShoutsData			NPC_SHOUTS_DATA;
	public static LevelUpSurveyData		LEVEL_UP_SURVEYS;
	public static StaticDoorData		STATICDOOR_DATA;	
	
	private XmlDataLoader loader;

	public static final DataManager getInstance()
	{
		return SingletonHolder.instance;
	}

	private DataManager()
	{
		Util.printSection("DataPack Manager");
		log.info("Reading data files: Ok.");
		log.info("Loading Data. Please wait...");
		log.info("==================================================");
		this.loader = XmlDataLoader.getInstance();
		long start = System.currentTimeMillis();
		StaticData data = loader.loadStaticData();
		long time = System.currentTimeMillis() - start;
		WORLD_MAPS_DATA = data.worldMapsData;
		PLAYER_EXPERIENCE_TABLE = data.playerExperienceTable;
		PLAYER_STATS_DATA = data.playerStatsData;
		SUMMON_STATS_DATA = data.summonStatsData;
		ITEM_DATA = data.itemData;
		WRAPPED_ITEM_DATA = data.wrappedItemData;
		PET_FEED_DATA = data.petFeedData;
		NPC_DATA = data.npcData;
		GATHERABLE_DATA = data.gatherableData;
		GUILDS_DATA = data.guildsData;
		PLAYER_INITIAL_DATA = data.playerInitialData;
		SKILL_DATA = data.skillData;
		SKILL_TREE_DATA = data.skillTreeData;
		SPAWNS_DATA = data.spawnsData;
		TITLE_DATA = data.titleData;
		WINDSTREAM_DATA = data.windstreamsData;
		TRADE_LIST_DATA = data.tradeListData;
		TELEPORTER_DATA = data.teleporterData;
		TELELOCATION_DATA = data.teleLocationData;
		CUSTOM_PRESET_DATA = data.customPresets;
		CUBEEXPANDER_DATA = data.cubeExpandData;
		WAREHOUSEEXPANDER_DATA = data.warehouseExpandData;
		BIND_POINT_DATA = data.bindPointData;
		QUEST_DATA = data.questData;
		BONUS_DATA = data.bonusData;
		QUEST_SCRIPTS_DATA = data.questsScriptData;
		ZONE_DATA = data.zoneData;
		FLIGHT_ZONE_DATA = data.flightZoneData;
		WALKER_DATA = data.walkerData;
		GOODSLIST_DATA = data.goodsListData;
		TRIBE_RELATIONS_DATA = data.tribeRelationsData;
		RECIPE_DATA = data.recipeData;
		PORTAL_DATA = data.portalData;
		CHEST_DATA = data.chestData;
		ITEM_SET_DATA = data.itemSetData;
		NPC_SKILL_DATA = data.npcSkillData;
		PET_SKILL_DATA = data.petSkillData;
		SIEGE_LOCATION_DATA = data.siegeLocationData;
		SIEGE_SPAWN_DATA = data.siegeSpawnData;
		SHIELD_DATA = data.shieldData;
		FLY_RING_DATA = data.flyRingData;
		PET_DATA = data.petData;
		ROAD_DATA = data.roadData;
		DROPLIST_DATA = data.droplistData;
		NPC_SHOUTS_DATA = data.npcShoutsData;
		LEVEL_UP_SURVEYS = data.levelUpSurveys;
		STATICDOOR_DATA = data.staticDoorData;

		long seconds = time / 1000;

		String timeMsg = seconds > 0 ? seconds + " sec." : time + " ms.";
		log.info("==================================================");
		log.info("DataPack load time: " + timeMsg);
		
	}
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DataManager instance = new DataManager();
	}
}