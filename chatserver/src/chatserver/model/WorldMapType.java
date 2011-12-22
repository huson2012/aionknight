/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package chatserver.model;

public enum WorldMapType
{
	// Asmodea
	PANDAEMONIUM(120010000), MARCHUTAN(120020000), ISHALGEN(220010000), MORHEIM(220020000), 
	ALTGARD(220030000),	BELUSLAN(220040000), BRUSTHONIN(220050000),

	// Elysia
	SANCTUM(110010000),	KAISINEL(110020000), POETA(210010000), VERTERON(210030000),
	ELTNEN(210020000), HEIRON(210040000), THEOMOBOS(210060000), 

	// Balaurea
	INGGISON(210050000), GELKMAROS(220070000), UNDERPASS(600010000),
	
	// Prison
	PRISON(510010000), DF_PRISON(520010000), RESHANTA(400010000),

	//Instances
	NOCHSANA_TRAINING_CAMP(300030000), DARK_POETA(300040000), ASTERIA_CHAMBER(300050000),
	SULFUR_TREE_NEST(300060000), CHAMBER_OF_ROAH(300070000), LEFT_WING_CHAMBER(300080000),
	RIGHT_WING_CHAMBER(300090000), STEEL_RAKE(300100000), DREDGION(300110000),
	KYSIS_CHAMBER(300120000), MIREN_CHAMBER(300130000), KROTAN_CHAMBER(300140000),
	TEMPLEUP(300150000), TEMPLELOW(300160000), CATACOMBS(300170000), TALOCSHOLLOW(300190000),
	NOVICE(300200000), DREDGION2(300210000), DEBRIS_OF_ABYSS(300220000), CROMEDE(300230000),
	AZOTURAN_FORTRESS(310100000), AERDINA(310030000), AETHEROGENETICS_LAB(310050000),
	INDRATU_FORTRESS(310090000), THEOBOMOS_LAB(310110000), BREGIRUN(320030000),
	SKY_TEMPLE_INTERIOR(320050000), DRAUPNIR_CAVE(320080000), FIRE_TEMPLE(320100000),
	DF1B(320060000), SPACE(320070000), DC1_ARENA(320090000), ALQUIMIA(320110000),
	DC1_ARENA_3F(320120000), ADMA_STRONGHOLD(320130000), ABPROD3(320140000),
	 
	//Test Map
	TEST_BASIC(900020000), TEST_SERVER(900030000), TEST_GIANTMONSTER(900100000),
	HOUSING_BARRACK(900110000);

	private final int worldId;

	WorldMapType(int worldId)
	{
		this.worldId = worldId;
	}

	public int getId()
	{
		return worldId;
	}
}