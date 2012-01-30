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

package gameserver.utils.i18n;

import commons.scripting.classlistener.ClassListener;
import commons.scripting.classlistener.DefaultClassListener;
import commons.utils.ClassUtils;
import org.apache.log4j.Logger;
import java.lang.reflect.Modifier;

public class LanguagesLoader extends DefaultClassListener implements ClassListener
{
	private static final Logger log = Logger.getLogger(Language.class);
	
	private final LanguageHandler handler;
	
	public LanguagesLoader (LanguageHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void postLoad(Class<?>[] classes)
	{
		for (Class<?> clazz : classes)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Loading class " + clazz.getName());
			}

			if (!isValidClass(clazz))
			{
				continue;
			}
			
			if (ClassUtils.isSubclass(clazz, Language.class))
			{
				Class<? extends Language> language = (Class<? extends Language>)clazz;
				if (language != null)
				{
					try
					{
						handler.registerLanguage(language.newInstance());
					}
					catch(Exception e)
					{
						log.error("Registering "+language.getName(), e);
					}
				}
			}
		}

		super.postLoad(classes);

		log.info("Loaded " + handler.size() + " custom message handlers.");
	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		if (log.isDebugEnabled())
		{
			for (Class<?> clazz : classes)
			{
				log.debug("Unload language " + clazz.getName());
			}
		}

		super.preUnload(classes);

		handler.clear();
	}

	public boolean isValidClass(Class<?> clazz)
	{
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
		{
			return false;
		}

        return Modifier.isPublic(modifiers);

    }
}
