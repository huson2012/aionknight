/*
 * This file is part of Aion-Knight Emu.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight Emu.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * author^
 * Fr0st;
 * Mr.Chayka.
*/

package org.openaion.gameserver.dataholders;
     
import org.openaion.gameserver.model.templates.road.RoadTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
     
@XmlRootElement(name="roads")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoadData {
	@XmlElement(name="road")
    private List<RoadTemplate> roadTemplates;
     
    public int size() {
		if (roadTemplates == null) {
			roadTemplates = new ArrayList<RoadTemplate>();
			return 0;
        }
		return roadTemplates.size();
	}
     
	public List<RoadTemplate> getRoadTemplates() {
		if (roadTemplates == null) {
			return new ArrayList<RoadTemplate>();
        }
		return roadTemplates;
    }
     
	public void addAll(Collection<RoadTemplate> templates) {
		if (roadTemplates == null) {
			roadTemplates = new ArrayList<RoadTemplate>();
        }
		roadTemplates.addAll(templates);
    }
}