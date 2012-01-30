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

package gameserver.model.drop;

import gnu.trove.TIntObjectHashMap;
import java.util.HashSet;
import java.util.Set;

public class DropList 
{	
	private TIntObjectHashMap<Set<DropTemplate>> templatesMap = new TIntObjectHashMap<Set<DropTemplate>>();
	
	public void addDropTemplate(int mobId, DropTemplate dropTemplate)
	{
		Set<DropTemplate> dropTemplates = templatesMap.get(mobId);
		if(dropTemplates == null)
		{
			dropTemplates = new HashSet<DropTemplate>();
			templatesMap.put(mobId, dropTemplates);
		}
		dropTemplates.add(dropTemplate);
	}
	
	public void removeDrop(final int mobId, final int itemId)
	{
		DropTemplate select = null;
		if(templatesMap.get(mobId)!=null)
		{
			Set<DropTemplate> npcDrops = templatesMap.get(mobId);
			for(DropTemplate drop : npcDrops)
			{
				if(drop.getItemId() == itemId)
				{
					select = drop;
					break;
				}
			}
		}
		if(select != null)
			templatesMap.get(mobId).remove(select);
	}
	
	public TIntObjectHashMap<Set<DropTemplate>> getAll()
	{
		return templatesMap;
	}
	
	public Set<DropTemplate> getDropsFor(int mobId)
	{
		Set<DropTemplate> drops = templatesMap.get(mobId);
		if (drops == null)
			return drops;
		
		Set<DropTemplate> copy = new HashSet<DropTemplate>();
		copy.addAll(drops);
		return copy;
	}
	
	public int getSize()
	{
		return templatesMap.size();
	}
}
