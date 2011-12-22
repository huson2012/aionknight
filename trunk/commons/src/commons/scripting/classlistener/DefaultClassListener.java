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

package commons.scripting.classlistener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.log4j.Logger;
import commons.scripting.metadata.OnClassLoad;
import commons.scripting.metadata.OnClassUnload;

public class DefaultClassListener implements ClassListener
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(DefaultClassListener.class);

	@Override
	public void postLoad(Class<?>[] classes)
	{
		for(Class<?> c : classes)
		{
			doMethodInvoke(c.getDeclaredMethods(), OnClassLoad.class);
		}
	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		for(Class<?> c : classes)
		{
			doMethodInvoke(c.getDeclaredMethods(), OnClassUnload.class);
		}
	}

	/**
	 * Actually invokes method where given annotation class is present. Only static methods can be invoked
	 * 
	 * @param methods
	 *           Methods to scan for annotations
	 * @param annotationClass
	 *           class of annotation to search for
	 */
	protected final void doMethodInvoke(Method[] methods, Class<? extends Annotation> annotationClass)
	{
		for(Method m : methods)
		{
			if(!Modifier.isStatic(m.getModifiers()))
				continue;

			boolean accessible = m.isAccessible();
			m.setAccessible(true);

			if(m.getAnnotation(annotationClass) != null)
			{
				try
				{
					m.invoke(null);
				}
				catch(IllegalAccessException e)
				{
					log.error("Can't access method " + m.getName() + " of class " + m.getDeclaringClass().getName(), e);
				}
				catch(InvocationTargetException e)
				{
					log.error("Can't invoke method " + m.getName() + " of class " + m.getDeclaringClass().getName(), e);
				}
			}

			m.setAccessible(accessible);
		}
	}
}