/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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
package gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**

 *
 */
@XmlType(name = "quality")
@XmlEnum
public enum ItemQuality
{
	// TODO: Reorder and rename - requires ATracer parser update  (?)
	COMMON,		// 1 Common		- White
	RARE,		// 2 Superior	- Green
	UNIQUE,		// 4 Fabled		- Yellow
	LEGEND,		// 3 Heroic		- Blue
	MYTHIC,		// 6 Test		- Purple
	EPIC,		// 5 Eternal	- Orange
	JUNK		// 0 Junk		- Gray
}
