/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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

		if (!Modifier.isPublic(modifiers))
			return false;

		return true;
	}
}