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

package gameserver.quest.handlers.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterInfo")
public class MonsterInfo
{
	@XmlAttribute(name = "var_id", required = true)
	protected int varId;
	@XmlAttribute(name = "min_var_value")
	protected Integer minVarValue;
	@XmlAttribute(name = "max_kill", required = true)
	protected int maxKill;
	@XmlAttribute(name = "npc_id", required = true)
	protected int npcId;

	public int getVarId()
	{
		return varId;
	}

	public Integer getMinVarValue()
	{
		return minVarValue;
	}

	public int getMaxKill()
	{
		return maxKill;
	}

	public int getNpcId()
	{
		return npcId;
	}
}