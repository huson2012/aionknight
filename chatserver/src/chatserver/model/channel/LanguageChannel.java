/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 */

package chatserver.model.channel;

import chatserver.model.ChannelType;
import chatserver.model.Race;

public class LanguageChannel extends RaceChannel
{	
	/**
	 * @param race
	 */
	public LanguageChannel(Race race)
	{
		super(ChannelType.USER, race);
	}
}