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

package commons.scripting.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import commons.scripting.CompilationResult;
import commons.scripting.ScriptCompiler;
import commons.scripting.ScriptContext;
import commons.scripting.classlistener.ClassListener;
import commons.scripting.classlistener.DefaultClassListener;

/**
 * This class is actual implementation of {@link commons.scripting.ScriptContext}
 */
public class ScriptContextImpl implements ScriptContext
{
	/**
	 * logger for this class
	 */
	private static final Logger	log	= Logger.getLogger(ScriptContextImpl.class);

	/**
	 * Script context that is parent for this script context
	 */
	private final ScriptContext	parentScriptContext;

	/**
	 * Libraries (list of jar files) that have to be loaded class loader
	 */
	private Iterable<File>		libraries;

	/**
	 * Root directory of this script context. It and it's subdirectories will be scanned for .java files.
	 */
	private final File			root;

	/**
	 * Result of compilation of script context
	 */
	private CompilationResult	compilationResult;

	/**
	 * List of child script contexts
	 */
	private Set<ScriptContext>	childScriptContexts;

	/**
	 * Classlistener for this script context
	 */
	private ClassListener		classListener;

	/**
	 * Class name of the compiler that will be used to compile sources
	 */
	private String				compilerClassName;

	/**
	 * Creates new scriptcontext with given root file
	 * 
	 * @param root
	 *           file that represents root directory of this script context
	 * @throws NullPointerException
	 *            if root is null
	 * @throws IllegalArgumentException
	 *            if root directory doesn't exists or is not a directory
	 */
	public ScriptContextImpl(File root)
	{
		this(root, null);
	}

	/**
	 * Creates new ScriptContext with given file as root and another ScriptContext as parent
	 * 
	 * @param root
	 *           file that represents root directory of this script context
	 * @param parent
	 *           parent ScriptContex. It's classes and libraries will be accessible for this script context
	 * @throws NullPointerException
	 *            if root is null
	 * @throws IllegalArgumentException
	 *            if root directory doesn't exists or is not a directory
	 */
	public ScriptContextImpl(File root, ScriptContext parent)
	{
		if(root == null)
		{
			throw new NullPointerException("Root file must be specified");
		}

		if(!root.exists() || !root.isDirectory())
		{
			throw new IllegalArgumentException("Root directory not exists or is not a directory");
		}

		this.root = root;
		this.parentScriptContext = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void init()
	{

		if(compilationResult != null)
		{
			log.error(new Exception("Init request on initialized ScriptContext"));
			return;
		}

		ScriptCompiler scriptCompiler = instantiateCompiler();

		Collection<File> files = FileUtils.listFiles(root, scriptCompiler.getSupportedFileTypes(), true);

		if(parentScriptContext != null)
		{
			scriptCompiler.setParentClassLoader(parentScriptContext.getCompilationResult().getClassLoader());
		}

		scriptCompiler.setLibraires(libraries);
		compilationResult = scriptCompiler.compile(files);

		getClassListener().postLoad(compilationResult.getCompiledClasses());

		if(childScriptContexts != null)
		{
			for(ScriptContext context : childScriptContexts)
			{
				context.init();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void shutdown()
	{

		if(compilationResult == null)
		{
			log.error("Shutdown of not initialized stript context", new Exception());
			return;
		}

		if(childScriptContexts != null)
		{
			for(ScriptContext child : childScriptContexts)
			{
				child.shutdown();
			}
		}

		getClassListener().preUnload(compilationResult.getCompiledClasses());
		compilationResult = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reload()
	{
		shutdown();
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getRoot()
	{
		return root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompilationResult getCompilationResult()
	{
		return compilationResult;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isInitialized()
	{
		return compilationResult != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLibraries(Iterable<File> files)
	{
		this.libraries = files;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<File> getLibraries()
	{
		return libraries;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptContext getParentScriptContext()
	{
		return parentScriptContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ScriptContext> getChildScriptContexts()
	{
		return childScriptContexts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addChildScriptContext(ScriptContext context)
	{

		synchronized(this)
		{
			if(childScriptContexts == null)
			{
				childScriptContexts = new HashSet<ScriptContext>();
			}

			if(childScriptContexts.contains(context))
			{
				log.error("Double child definition, root: " + root.getAbsolutePath() + ", child: "
					+ context.getRoot().getAbsolutePath());
				return;
			}

			if(isInitialized())
			{
				context.init();
			}
		}

		childScriptContexts.add(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClassListener(ClassListener cl)
	{
		classListener = cl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassListener getClassListener()
	{
		if(classListener == null)
		{
			if(getParentScriptContext() == null)
			{
				setClassListener(new DefaultClassListener());
				return classListener;
			}
			else
			{
				return getParentScriptContext().getClassListener();
			}
		}
		else
		{
			return classListener;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCompilerClassName(String className)
	{
		this.compilerClassName = className;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCompilerClassName()
	{
		return this.compilerClassName;
	}

	/**
	 * Creates new instance of ScriptCompiler that should be used with this ScriptContext
	 * 
	 * @return instance of ScriptCompiler
	 * @throws RuntimeException
	 *            if failed to create instance
	 */
	protected ScriptCompiler instantiateCompiler() throws RuntimeException
	{
		ClassLoader cl = getClass().getClassLoader();
		if(getParentScriptContext() != null)
		{
			cl = getParentScriptContext().getCompilationResult().getClassLoader();
		}

		ScriptCompiler sc;
		try
		{
			sc = (ScriptCompiler) Class.forName(getCompilerClassName(), true, cl).newInstance();
		}
		catch(Exception e)
		{
			RuntimeException e1 = new RuntimeException("Can't create instance of compiler", e);
			log.error(e1);
			throw e1;
		}

		return sc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ScriptContextImpl))
		{
			return false;
		}

		ScriptContextImpl another = (ScriptContextImpl) obj;

		if(parentScriptContext == null)
		{
			return another.getRoot().equals(root);
		}
		else
		{
			return another.getRoot().equals(root) && parentScriptContext.equals(another.parentScriptContext);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		int result = parentScriptContext != null ? parentScriptContext.hashCode() : 0;
		result = 31 * result + root.hashCode();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalize() throws Throwable
	{
		if(compilationResult != null)
		{
			log.error("Finalization of initialized ScriptContext. Forcing context shutdown.");
			shutdown();
		}
		super.finalize();
	}
}