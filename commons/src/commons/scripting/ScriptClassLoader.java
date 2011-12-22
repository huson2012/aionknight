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

package commons.scripting;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.log4j.Logger;
import commons.scripting.url.VirtualClassURLStreamHandler;

/**
 * 
 * Abstract class loader that should be extended by child classloaders. If needed, this class should wrap another
 * classloader.
 */
public abstract class ScriptClassLoader extends URLClassLoader
{
	/**
	 * Logger
	 */
	private static final Logger					log					= Logger.getLogger(ScriptClassLoader.class);

	/**
	 * URL Stream handler to allow valid url generation by {@link #getResource(String)}
	 */
	private final VirtualClassURLStreamHandler	urlStreamHandler	= new VirtualClassURLStreamHandler(this);

	/**
	 * Classes that were loaded from libraries. They are no parsed for any annotations, but they are needed by
	 * JavaCompiler to perform valid compilation
	 */
	protected Set<String>						libraryClasses		= new HashSet<String>();

	/**
	 * Just for compatibility with {@link URLClassLoader}
	 * 
	 * @param urls
	 *           list of urls
	 * @param parent
	 *           parent classloader
	 */
	public ScriptClassLoader(URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
	}

	/**
	 * Just for compatibility with {@link URLClassLoader}
	 * 
	 * @param urls
	 *           list of urls
	 */
	public ScriptClassLoader(URL[] urls)
	{
		super(urls);
	}

	/**
	 * Just for compatibility with {@link URLClassLoader}
	 * 
	 * @param urls
	 *           list of urls
	 * @param parent
	 *           parent classloader
	 * @param factory
	 *           {@link java.net.URLStreamHandlerFactory}
	 */
	public ScriptClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
	{
		super(urls, parent, factory);
	}

	/**
	 * Adds library to this classloader, it shuould be jar file
	 * 
	 * @param file
	 *           jar file
	 * @throws IOException
	 *            if can't add library
	 */
	public void addLibrary(File file) throws IOException
	{
		URL fileURL = file.toURI().toURL();
		super.addURL(fileURL);

		JarFile jarFile = new JarFile(file);

		Enumeration<JarEntry> entries = jarFile.entries();
		while(entries.hasMoreElements())
		{
			JarEntry entry = entries.nextElement();

			String name = entry.getName();
			if(name.endsWith(".class"))
			{
				name = name.substring(0, name.length() - 6);
				name = name.replace('/', '.');
				libraryClasses.add(name);
			}
		}

		jarFile.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getResource(String name)
	{
		if(!name.endsWith(".class"))
		{
			return super.getResource(name);
		}
		else
		{
			String newName = name.substring(0, name.length() - 6);
			newName = newName.replace('/', '.');
			if(getCompiledClasses().contains(newName))
			{
				try
				{
					return new URL(null, VirtualClassURLStreamHandler.HANDLER_PROTOCOL + newName, urlStreamHandler);
				}
				catch(MalformedURLException e)
				{
					log.error("Can't create url for compiled class", e);
				}
			}
		}

		return super.getResource(name);
	}

	/**
	 * Loads class from library, parent or compiled
	 * 
	 * @param name
	 *           class to load
	 * @return loaded class
	 * @throws ClassNotFoundException
	 *            if class not found
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{
		boolean isCompiled = getCompiledClasses().contains(name);
		if(!isCompiled)
		{
			return super.loadClass(name, true);
		}

		Class<?> c = getDefinedClass(name);
		if(c == null)
		{
			byte[] b = getByteCode(name);
			c = super.defineClass(name, b, 0, b.length);
			setDefinedClass(name, c);
		}
		return c;
	}

	/**
	 * Returns unmodifiable set of class names that were loaded from libraries
	 * 
	 * @return unmodifiable set of class names that were loaded from libraries
	 */
	public Set<String> getLibraryClasses()
	{
		return Collections.unmodifiableSet(libraryClasses);
	}

	/**
	 * Retuns unmodifiable set of class names that were compiled
	 * 
	 * @return unmodifiable set of class names that were compiled
	 */
	public abstract Set<String> getCompiledClasses();

	/**
	 * Returns bytecode for given className. Array is copy of actual bytecode, so modifications will not harm.
	 * 
	 * @param className
	 *           class name
	 * @return bytecode
	 */
	public abstract byte[] getByteCode(String className);

	/**
	 * Returns cached class instance for give name or null if is not cached yet
	 * 
	 * @param name
	 *           class name
	 * @return cached class instance or null
	 */
	public abstract Class<?> getDefinedClass(String name);

	/**
	 * Sets defined class into cache
	 * 
	 * @param name
	 *           class name
	 * @param clazz
	 *           class object
	 * @throws IllegalArgumentException
	 *            if class was not loaded by this class loader
	 */
	public abstract void setDefinedClass(String name, Class<?> clazz) throws IllegalArgumentException;
}