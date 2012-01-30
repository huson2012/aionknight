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


package gameserver.utils.chathandlers;

import commons.scripting.classlistener.ClassListener;
import commons.scripting.classlistener.DefaultClassListener;
import commons.utils.ClassUtils;
import org.apache.log4j.Logger;
import java.lang.reflect.Modifier;

class ChatHandlersLoader
		extends DefaultClassListener
		implements ClassListener
{
	private static final Logger logger = Logger.getLogger(ChatHandlersLoader.class);
	
	private final CommandChatHandler adminCCH;

	public ChatHandlersLoader(CommandChatHandler handler)
	{
		this.adminCCH = handler;
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

			if (ClassUtils.isSubclass(c, AdminCommand.class))
			{
				Class<? extends AdminCommand> tmp = (Class<? extends AdminCommand>)c;
				if (tmp != null)
					try
					{
						adminCCH.registerAdminCommand(tmp.newInstance());
					}
					catch(InstantiationException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			if (ClassUtils.isSubclass(c, UserCommand.class))
			{
				Class<? extends UserCommand> tmp = (Class<? extends UserCommand>)c;
				if (tmp != null)
				{
					try
					{
						adminCCH.registerUserCommand(tmp.newInstance());
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
		}

		// call onClassLoad()
		super.postLoad(classes);
	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		if (logger.isDebugEnabled())
			for (Class<?> c : classes)// debug messages
				logger.debug("Unload class " + c.getName());

		// call onClassUnload()
		super.preUnload(classes);

		adminCCH.clearHandlers();// unload all admin handlers.
	}

	public boolean isValidClass(Class<?> clazz)
	{
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

        return Modifier.isPublic(modifiers);

    }
}
