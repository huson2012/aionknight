/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
 *
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������)
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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