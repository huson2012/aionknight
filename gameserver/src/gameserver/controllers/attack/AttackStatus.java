/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers.attack;

/**

 *
 */
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
	BUF(8),// ??
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
