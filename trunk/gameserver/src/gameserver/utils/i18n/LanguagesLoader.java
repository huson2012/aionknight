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

		if (!Modifier.isPublic(modifiers))
		{
			return false;
		}

		return true;
	}
}
