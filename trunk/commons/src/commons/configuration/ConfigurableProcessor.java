/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package commons.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ConfigurableProcessor
{
	private static final Logger	log	= Logger.getLogger(ConfigurableProcessor.class);
	@SuppressWarnings("rawtypes")
	public static void process(Object object, Properties... properties)
	{
		Class clazz;

		if(object instanceof Class)
		{
			clazz = (Class) object;
			object = null;
		}
		else
		{
			clazz = object.getClass();
		}

		process(clazz, object, properties);
	}

	@SuppressWarnings("rawtypes")
	private static void process(Class clazz, Object obj, Properties[] props)
	{
		processFields(clazz, obj, props);

		if(obj == null)
		{
			for(Class itf : clazz.getInterfaces())
			{
				process(itf, obj, props);
			}
		}

		Class superClass = clazz.getSuperclass();
		if(superClass != null && superClass != Object.class)
		{
			process(superClass, obj, props);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void processFields(Class clazz, Object obj, Properties[] props)
	{
		for(Field f : clazz.getDeclaredFields())
		{
			if(Modifier.isStatic(f.getModifiers()) && obj != null)
			{
				continue;
			}

			if(!Modifier.isStatic(f.getModifiers()) && obj == null)
			{
				continue;
			}

			if(f.isAnnotationPresent(Property.class))
			{
				if(Modifier.isFinal(f.getModifiers()))
				{
					RuntimeException re = new RuntimeException("Attempt to proceed final field " + f.getName()
						+ " of class " + clazz.getName());
					log.error(re);
					throw re;
				}
				else
				{
					processField(f, obj, props);
				}
			}
		}
	}
	private static void processField(Field f, Object obj, Properties[] props)
	{
		boolean oldAccessible = f.isAccessible();
		f.setAccessible(true);
		try
		{
			Property property = f.getAnnotation(Property.class);
			if(!Property.DEFAULT_VALUE.equals(property.defaultValue()) || isKeyPresent(property.key(), props))
			{
				f.set(obj, getFieldValue(f, props));
			}
			else if(log.isDebugEnabled())
			{
				log.debug("Field " + f.getName() + " of class " + f.getDeclaringClass().getName() + " wasn't modified");
			}
		}
		catch(Exception e)
		{
			RuntimeException re = new RuntimeException("Can't transform field " + f.getName() + " of class "
				+ f.getDeclaringClass(), e);
			log.error(re);
			throw re;
		}
		f.setAccessible(oldAccessible);
	}
	@SuppressWarnings("rawtypes")
	private static Object getFieldValue(Field field, Properties[] props) throws TransformationException
	{
		Property property = field.getAnnotation(Property.class);
		String defaultValue = property.defaultValue();
		String key = property.key();
		String value = null;

		if(key.isEmpty())
		{
			log.warn("Property " + field.getName() + " of class " + field.getDeclaringClass().getName()
				+ " has empty key");
		}
		else
		{
			value = findPropertyByKey(key, props);
		}

		if(value == null)
		{
			value = defaultValue;
			if(log.isDebugEnabled())
			{
				log.debug("Using default value for field " + field.getName() + " of class "
					+ field.getDeclaringClass().getName());
			}
		}

		PropertyTransformer pt = PropertyTransformerFactory.newTransformer(field.getType(), property
			.propertyTransformer());
		return pt.transform(value, field);
	}

	private static String findPropertyByKey(String key, Properties[] props)
	{
		for(Properties p : props)
		{
			if(p.containsKey(key))
			{
				return p.getProperty(key);
			}
		}

		return null;
	}

	private static boolean isKeyPresent(String key, Properties[] props)
	{
		return findPropertyByKey(key, props) != null;
	}
}