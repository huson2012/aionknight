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

package gameserver.model.gameobjects.stats.modifiers;

import gameserver.model.gameobjects.AionObject;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import java.util.Collection;

public abstract class Executor<T extends AionObject>
{
	private static final Logger log = Logger.getLogger(Executor.class);
	
	public abstract boolean run (T object);
	
	private final void runImpl (Collection<T> objects)
	{
		try {
			for (T o : objects)
			{
				if (!Executor.this.run(o))
					break;
			}
		}
		catch (Exception e)
		{
			log.warn(e.getMessage(), e);
		}
	}
	
	public final void execute (final Collection<T> objects, boolean now)
	{
		if (now)
		{
			runImpl(objects);
		}
		else
		{
			ThreadPoolManager.getInstance().execute(new Runnable () {
				@Override
				public void run ()
				{
					runImpl(objects);
				}
			});
		}
	}
	
	public final void execute (final Collection<T> objects)
	{
		execute(objects,false);
	}
}
