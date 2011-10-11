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

import java.util.*;

public class RoadData
{

    public RoadData()
    {
    }

    public int size()
    {
        if(roadTemplates == null)
        {
            roadTemplates = new ArrayList();
            return 0;
        } else
        {
            return roadTemplates.size();
        }
    }

    public List getRoadTemplates()
    {
        if(roadTemplates == null)
            return new ArrayList();
        else
            return roadTemplates;
    }

    public void addAll(Collection collection)
    {
        if(roadTemplates == null)
            roadTemplates = new ArrayList();
        roadTemplates.addAll(collection);
    }

    private List roadTemplates;
}