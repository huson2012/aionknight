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

package gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum Race
{
	ELYOS(0), ASMODIANS(1),	LYCAN(2), CONSTRUCT(3),	CARRIER(4), DRAKAN(5),
	LIZARDMAN(6), TELEPORTER(7), NAGA(8), BROWNIE(9), KRALL(10), SHULACK(11),
	BARRIER(12), PC_LIGHT_CASTLE_DOOR(13), PC_DARK_CASTLE_DOOR(14),
	DRAGON_CASTLE_DOOR(15), GCHIEF_LIGHT(16), GCHIEF_DARK(17), DRAGON(18),
	OUTSIDER(19), RATMAN(20), DEMIHUMANOID(21),	UNDEAD(22),	BEAST(23),
	MAGICALMONSTER(24),	ELEMENTAL(25), NONE(26), PC_ALL(27);

	private int	raceId;
	private Race(int raceId)
	{
		this.raceId = raceId;
	}

	public int getRaceId()
	{
		return raceId;
	}
}