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

package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.shield.Shield;
import gameserver.model.templates.shield.ShieldTemplate;
import org.apache.log4j.Logger;

public class ShieldService
{
	
	private static final Logger log = Logger.getLogger(ShieldService.class);
	
	public static final ShieldService getInstance()
	{
		return SingletonHolder.instance;
	}

	private ShieldService ()
	{
		for (ShieldTemplate t : DataManager.SHIELD_DATA.getSchieldTemplates())
		{
			Shield s = new Shield(t);
			s.spawn();
			log.debug("Added schield "+t.getName()+" for "+t.getRace()+" in World");
		}
	}
	
	private static class SingletonHolder
	{
		protected static final ShieldService instance = new ShieldService();
	}
}