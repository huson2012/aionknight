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

package gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import gameserver.model.templates.walker.WalkerTemplate;

@XmlRootElement(name = "npc_walker")
@XmlAccessorType(XmlAccessType.FIELD)
public class WalkerData
{
	@XmlElement(name = "walker_template")
	private List<WalkerTemplate> walkerlist;
	
	private TIntObjectHashMap<WalkerTemplate>	walkerlistData	= new TIntObjectHashMap<WalkerTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(WalkerTemplate route: walkerlist)
		{
			walkerlistData.put(route.getRouteId(), route);
		}
	}
	
	public int size()
	{
		return walkerlistData.size();
	}

	public WalkerTemplate getWalkerTemplate(int id)
	{
		return walkerlistData.get(id);
	}
}