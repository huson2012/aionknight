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

package commons.utils;

public class ClassUtils
{

	public static boolean isSubclass(Class<?> a, Class<?> b)
	{
		if(a == b)
		{
			return true;
		}
		if(a == null || b == null)
		{
			return false;
		}
		for(Class<?> x = a; x != null; x = x.getSuperclass())
		{
			if(x == b)
			{
				return true;
			}
			if(b.isInterface())
			{
				Class<?>[] interfaces = x.getInterfaces();
				for(Class<?> anInterface : interfaces)
				{
					if(isSubclass(anInterface, b))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean isPackageMember(Class<?> clazz, String packageName)
	{
		return isPackageMember(clazz.getName(), packageName);
	}
	public static boolean isPackageMember(String className, String packageName)
	{
		if(!className.contains("."))
		{
			return packageName == null || packageName.isEmpty();
		}
		else
		{
			String classPackage = className.substring(0, className.lastIndexOf('.'));
			return packageName.equals(classPackage);
		}
	}
}