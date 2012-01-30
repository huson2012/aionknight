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