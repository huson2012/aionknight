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

package commons.scripting.impl.javacompiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.tools.JavaFileObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import commons.scripting.ScriptClassLoader;
import commons.utils.ClassUtils;

/**
 * This classloader is used to load script classes. <br>
 * <br>
 * Due to JavaCompiler limitations we have to keep list of available classes here.
 */
public class ScriptClassLoaderImpl extends ScriptClassLoader
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(ScriptClassLoaderImpl.class);

	/**
	 * ClassFileManager that is related to this ClassLoader
	 */
	private final ClassFileManager	classFileManager;

	/**
	 * Creates new ScriptClassLoader with given ClassFileManger
	 * 
	 * @param classFileManager
	 *           classFileManager of this classLoader
	 */
	ScriptClassLoaderImpl(ClassFileManager classFileManager)
	{
		super(new URL[] {});
		this.classFileManager = classFileManager;
	}

	/**
	 * Creates new ScriptClassLoader with given ClassFileManger and another classLoader as parent
	 * 
	 * @param classFileManager
	 *           classFileManager of this classLoader
	 * @param parent
	 *           parent classLoader
	 */
	ScriptClassLoaderImpl(ClassFileManager classFileManager, ClassLoader parent)
	{
		super(new URL[] {}, parent);
		this.classFileManager = classFileManager;
	}

	/**
	 * Returns ClassFileManager that is related to this ClassLoader
	 * 
	 * @return classFileManager of this classLoader
	 */
	public ClassFileManager getClassFileManager()
	{
		return classFileManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getCompiledClasses()
	{
		Set<String> compiledClasses = classFileManager.getCompiledClasses().keySet();
		return Collections.unmodifiableSet(compiledClasses);
	}

	/**
	 * Returns list of classes that are members of a package
	 * 
	 * @param packageName
	 *           package to search for classes
	 * @return list of classes that are package members
	 * @throws IOException
	 *            if was unable to load class
	 */
	public Set<JavaFileObject> getClassesForPackage(String packageName) throws IOException
	{
		Set<JavaFileObject> result = new HashSet<JavaFileObject>();

		// load parent
		ClassLoader parent = getParent();
		if(parent instanceof ScriptClassLoaderImpl)
		{
			ScriptClassLoaderImpl pscl = (ScriptClassLoaderImpl) parent;
			result.addAll(pscl.getClassesForPackage(packageName));
		}

		// load current classloader compiled classes
		for(String cn : classFileManager.getCompiledClasses().keySet())
		{
			if(ClassUtils.isPackageMember(cn, packageName))
			{
				BinaryClass bc = classFileManager.getCompiledClasses().get(cn);
				result.add(bc);
			}
		}

		// load libraries
		for(String cn : libraryClasses)
		{
			if(ClassUtils.isPackageMember(cn, packageName))
			{
				BinaryClass bc = new BinaryClass(cn);
				try
				{
					byte[] data = getRawClassByName(cn);
					OutputStream os = bc.openOutputStream();
					os.write(data);
				}
				catch(IOException e)
				{
					log.error("Error while loading class from package " + packageName, e);
					throw e;
				}
				result.add(bc);
			}
		}

		return result;
	}

	/**
	 * Finds class with the specified name from the URL search path. Any URLs referring to JAR files are loaded and
	 * opened as needed until the class is found.
	 * 
	 * @param name
	 *           the name of the class
	 * @return the resulting class data
	 * @throws IOException
	 *            if the class could not be found
	 */
	protected byte[] getRawClassByName(String name) throws IOException
	{
		URL resource = findResource(name.replace('.', '/').concat(".class"));
		InputStream is = null;
		byte[] clazz = null;

		try
		{
			is = resource.openStream();
			clazz = IOUtils.toByteArray(is);
		}
		catch(IOException e)
		{
			log.error("Error while loading class data", e);
			throw e;
		}
		finally
		{
			if(is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
					log.error("Error while closing stream", e);
				}
			}
		}
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getByteCode(String className)
	{
		BinaryClass bc = getClassFileManager().getCompiledClasses().get(className);
		byte[] b = new byte[bc.getBytes().length];
		System.arraycopy(bc.getBytes(), 0, b, 0, b.length);
		return b;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getDefinedClass(String name)
	{
		BinaryClass bc = classFileManager.getCompiledClasses().get(name);
		if(bc == null)
		{
			return null;
		}

		return bc.getDefinedClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefinedClass(String name, Class<?> clazz)
	{
		BinaryClass bc = classFileManager.getCompiledClasses().get(name);

		if(bc == null)
		{
			throw new IllegalArgumentException("Attempt to set defined class for class that was not compiled?");
		}

		bc.setDefinedClass(clazz);
	}
}