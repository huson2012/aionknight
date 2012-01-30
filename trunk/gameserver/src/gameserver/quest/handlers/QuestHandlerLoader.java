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

package gameserver.quest.handlers;

import commons.scripting.classlistener.ClassListener;
import commons.scripting.classlistener.DefaultClassListener;
import commons.utils.ClassUtils;
import gameserver.quest.QuestEngine;
import org.apache.log4j.Logger;
import java.lang.reflect.Modifier;

public class QuestHandlerLoader extends DefaultClassListener implements ClassListener
{
	private static final Logger logger = Logger.getLogger(QuestHandlerLoader.class);
	public QuestHandlerLoader()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes)
	{
		for (Class<?> c : classes)
		{
			if (logger.isDebugEnabled())
				logger.debug("Load class " + c.getName());

			if (!isValidClass(c))
				continue;

			if (ClassUtils.isSubclass(c, QuestHandler.class))
			{
				try
				{
					Class<? extends QuestHandler> tmp = (Class<? extends QuestHandler>)c;
					if (tmp != null)
					{
						QuestHandler tmpo = tmp.newInstance();
						QuestEngine.getInstance().TEMP_HANDLERS.put(tmpo.getQuestId(), tmpo);
					}
				}
				catch(InstantiationException e)
				{
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
		super.postLoad(classes);

	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		if (logger.isDebugEnabled())
			for (Class<?> c : classes)
				logger.debug("Unload class " + c.getName());

		super.preUnload(classes);

		QuestEngine.getInstance().clear();
	}

	public boolean isValidClass(Class<?> clazz)
	{
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

        return Modifier.isPublic(modifiers);

    }
}
