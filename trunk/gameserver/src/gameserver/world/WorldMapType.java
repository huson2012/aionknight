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

package gameserver.world;

public enum WorldMapType
{
	// Asmodea
	PANDAEMONIUM(120010000),
	ISHALGEN(220010000),
	MORHEIM(220020000),
	ALTGARD(220030000),
	BELUSLAN(220040000),
	BRUSTHONIN(220050000),

	//Balaurea
	INGGISON(210050000),
	GELKMAROS(220070000),	
	
	// Elysia
	SANCTUM(110010000),
	POETA(210010000),
	VERTERON(210030000),
	ELTNEN(210020000),
	HEIRON(210040000),
	THEOMOBOS(210060000),

	// Prison
	PRISON(510010000),

	RESHANTA(400010000),

	//Instances
	NOCHSANA_TRAINING_CAMP(300030000),
	DARK_POETA(300040000),
	ASTERIA_CHAMBER(300050000),
	SULFUR_TREE_NEST(300060000),
	CHAMBER_OF_ROAH(300070000),
	LEFT_WING_CHAMBER(300080000),
	RIGHT_WING_CHAMBER(300090000),
	STEEL_RAKE(300100000),
	DREDGION(300110000),
	KYSIS_CHAMBER(300120000),
	MIREN_CHAMBER(300130000),
	KROTAN_CHAMBER(300140000),
	AETHEROGENETICS_LAB(310050000),
	INDRATU_FORTRESS(310090000),
	AZOTURAN_FORTRESS(310100000),
	THEOBOMOS_LAB(310110000),
	SKY_TEMPLE_INTERIOR(320050000),
	DRAUPNIR_CAVE(320080000),
	FIRE_TEMPLE(320100000),
	ALQUIMIA(320110000),
	// 2.0 Instances
	SILENTERA_CANYON(600010000),
	UDAS_TEMPLE_UPPER(300150000),
	UDAS_TEMPLE_LOWER(300160000),
	BESHMUNDIR_TEMPLE(300170000),
	TALOCS_HOLLOW(300190000),
	HOUSING_BARRACK(900110000),
	DEBRIS_OF_ABYSS(300220000),
	HARAMEL(300200000),
	CHANTRA_DREDGION(300210000),
	KROMEDES_TRIAL(300230000),

	TEST_BASIC(900020000),
	ADMA_STRONGHOLD(320130000),
	IDABPRO(300010000),
	IDTEST_DUNGEON(300020000),

	//2.5 zones
	KAISINEL_ACADEMY(110070000),
	MARCHUTAN_PRIORY(120080000),
	TAHMES(300310000),
	ATURAN_SKY_FORTRESS(300240000),
	ESOTERRACE(300250000),
	LADIS_FOREST(300260000),
	DORGEL_MANOR(300270000),
	LENTOR_OUTPOST(300280000),
	EMPYREAN_CRUCIBLE(300300000),

	//2.6 zones
	CRUCIBLE_CHALLANGE(300320000),

	//2.7 zones
	IDDRAMATA_01(320150000),
	CRUCIBLE_COLISEUM_01(300350000),
	CRUCIBLE_COLISEUM_02(300360000),
	CRUCIBLE_COLISEUM_03(300420000),
	CRUCIBLE_COLISEUM_04(300430000);

	private final int worldId;

	WorldMapType(int worldId)
	{
		this.worldId = worldId;
	}

	public int getId()
	{
		return worldId;
	}

	/**
	 * @param id of world
	 * @return WorldMapType
	 */
	public static WorldMapType getWorld(int id)
	{
		for (WorldMapType type : WorldMapType.values())
		{
			if (type.worldId == id)
			{
				return type;
			}
		}
		return null;
	}
}
