/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.model.instances;

import java.sql.Timestamp;

public class InstanceCD
{
	private Timestamp CDEnd = null;
	private int instanceId = 0;
	private int groupId = 0;
	public InstanceCD(Timestamp CDEnd, int instanceId, int groupId)
	{
		this.CDEnd = CDEnd;
		this.instanceId = instanceId;
		this.groupId = groupId;
	}

	public Timestamp getCDEndTime()
	{
		return CDEnd;
	}

	public int getInstanceId()
	{
		return instanceId;
	}

	public int getGroupId()
	{
		return groupId;
	}
}