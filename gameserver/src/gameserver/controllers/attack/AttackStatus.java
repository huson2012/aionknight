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

package gameserver.controllers.attack;

public enum AttackStatus
{
	DODGE(0),
	SUBHAND_DODGE(1),
	PARRY(2),
	SUBHAND_PARRY(3),
	BLOCK(4),
	SUBHAND_BLOCK(5),
	RESIST(6),
	SUBHAND_RESIST(7),
	BUF(8),
	SUBHAND_BUF(9),
	NORMALHIT(10),
	SUBHAND_NORMALHIT(11),
	CRITICAL_DODGE(-64),
	CRITICAL_PARRY(-62),
	CRITICAL_BLOCK(-60),
	CRITICAL_RESIST(-58),
	CRITICAL(-54),
	SUBHAND_CRITICAL_DODGE(-47),
	SUBHAND_CRITICAL_PARRY(-45),
	SUBHAND_CRITICAL_BLOCK(-43),
	SUBHAND_CRITICAL_RESIST(-41),
	SUBHAND_CRITICAL(-37);

	private int _type;

	private AttackStatus(int type)
	{
		this._type = type;
	}

	public int getId()
	{
		return _type;
	}
}