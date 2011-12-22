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

package commons.services;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javolution.util.FastMap;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import commons.scripting.scriptmanager.ScriptManager;

/**
 * Script Service class that is designed to manage all loaded contexts
 */
public class ScriptService
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(ScriptService.class);

	/**
	 * Container for ScriptManagers, sorted by file
	 */
	private final Map<File, ScriptManager>	map	= new FastMap<File, ScriptManager>().shared();

	/**
	 * Loads script descriptor from given directory or file
	 * 
	 * @param file
	 *           directory or file
	 * @throws RuntimeException
	 *            if failed to load script descriptor
	 */
	public void load(String file) throws RuntimeException
	{
		load(new File(file));
	}

	/**
	 * Loads script descriptor from given file or directory
	 * 
	 * @param file
	 *           file that has to be loaded
	 * @throws RuntimeException
	 *            if something went wrong
	 */
	public void load(File file) throws RuntimeException
	{
		if(file.isFile())
		{
			loadFile(file);
		}
		else if(file.isDirectory())
		{
			loadDir(file);
		}
	}

	/**
	 * Load script descriptor from given file
	 * 
	 * @param file
	 *           script descriptor
	 */
	private void loadFile(File file)
	{
		if(map.containsKey(file))
		{
			throw new IllegalArgumentException("ScriptManager by file:" + file + " already loaded");
		}

		ScriptManager sm = new ScriptManager();
		try
		{
			sm.load(file);
		}
		catch(Exception e)
		{
			log.error(e);
			throw new RuntimeException(e);
		}

		map.put(file, sm);
	}

	/**
	 * Loads all files from given directory
	 * 
	 * @param dir
	 *           directory to scan for files
	 */
	private void loadDir(File dir)
	{
		for(Object file : FileUtils.listFiles(dir, new String[] { "xml" }, false))
		{
			loadFile((File) file);
		}
	}

	/**
	 * Unloads given script descriptor
	 * 
	 * @param file
	 *           script descriptor
	 * @throws IllegalArgumentException
	 *            if descriptor is not loaded
	 */
	public void unload(File file) throws IllegalArgumentException
	{
		ScriptManager sm = map.remove(file);
		if(sm == null)
		{
			throw new IllegalArgumentException("ScriptManager by file " + file + " is not loaded.");
		}

		sm.shutdown();
	}

	/**
	 * Reloads given script descriptor
	 * 
	 * @param file
	 *           Script Descriptro
	 * @throws IllegalArgumentException
	 *            if descriptor is not loaded
	 */
	public void reload(File file) throws IllegalArgumentException
	{
		ScriptManager sm = map.get(file);
		if(sm == null)
		{
			throw new IllegalArgumentException("ScriptManager by file " + file + " is not loaded.");
		}

		sm.reload();
	}

	/**
	 * Adds script manager to script service.<br>
	 * Should be used if scriptManager uses custom loading logic for scripts.
	 * 
	 * @param scriptManager
	 *           Script manager object
	 * @param file
	 *           script descriptor file
	 */
	public void addScriptManager(ScriptManager scriptManager, File file)
	{
		if(map.containsKey(file))
		{
			throw new IllegalArgumentException("ScriptManager by file " + file + " is already loaded.");
		}

		map.put(file, scriptManager);
	}

	/**
	 * Returns unmodifiable map with loaded script managers
	 * 
	 * @return unmodifiable map
	 */
	public Map<File, ScriptManager> getLoadedScriptManagers()
	{
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Broadcast shutdown to all attached Script Managers.
	 */
	public void shutdown()
	{
		for(Iterator<Entry<File, ScriptManager>> it = this.map.entrySet().iterator(); it.hasNext();)
		{
			try
			{
				it.next().getValue().shutdown();
			}
			catch(Exception e)
			{
				log.warn("An exception occured during shudown procedure.", e);
			}

			it.remove();
		}
	}
}