/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package commons.database.dao;

import java.lang.reflect.Modifier;
import commons.scripting.classlistener.ClassListener;
import commons.scripting.classlistener.DefaultClassListener;
import commons.utils.ClassUtils;

class DAOLoader extends DefaultClassListener implements ClassListener
{
	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes)
	{
		for(Class<?> clazz : classes)
		{
			if(!isValidDAO(clazz))
				continue;

			try
			{
				DAOManager.registerDAO((Class<? extends DAO>) clazz);
			}
			catch(Exception e)
			{
				throw new Error("Can't register DAO class", e);
			}
		}

		super.postLoad(classes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void preUnload(Class<?>[] classes)
	{
		super.postLoad(classes);

		for(Class<?> clazz : classes)
		{
			if(!isValidDAO(clazz))
				continue;

			try
			{
				DAOManager.unregisterDAO((Class<? extends DAO>) clazz);
			}
			catch(Exception e)
			{
				throw new Error("Can't unregister DAO class", e);
			}
		}
	}

	public boolean isValidDAO(Class<?> clazz)
	{
		if(!ClassUtils.isSubclass(clazz, DAO.class))
			return false;

		final int modifiers = clazz.getModifiers();

		if(Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

		if(!Modifier.isPublic(modifiers))
			return false;

		if(clazz.isAnnotationPresent(DisabledDAO.class))
			return false;

		return true;
	}
}