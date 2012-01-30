/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.utils.i18n;

import commons.scripting.scriptmanager.ScriptManager;
import gameserver.GameServerError;
import gameserver.configs.main.GSConfig;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.List;
import java.util.Map;

public class LanguageHandler
{
	private static final File LANGUAGE_DESCRIPTOR_FILE = new File("./data/scripts/system/languages.xml");
	
	private static Logger log = Logger.getLogger(Language.class);
	
	private Map<String, Language> languages = new FastMap<String, Language> ();
	
	private Language language;
	
	private static final LanguageHandler instance = new LanguageHandler();
	
	private static boolean initialized = false;
	
	private ScriptManager sm;
	
	private LanguageHandler() {	}
	
	public static final LanguageHandler getInstance ()
	{
		if (!initialized)
		{
			instance.sm = new ScriptManager();
		
			instance.sm.setGlobalClassListener(new LanguagesLoader(instance));
		
			try
			{
				instance.sm.load(LANGUAGE_DESCRIPTOR_FILE);
			}
			catch (Exception e)
			{
				throw new GameServerError("Cannot load languages", e);
			}
		
			instance.language = instance.getLanguage(GSConfig.LANG);
			
			initialized = true;
		}
		
		return instance;
	}
	
	public static String translate (CustomMessageId id, Object... params)
	{
		return instance.language.translate(id, params);
	}
	
	public void registerLanguage (Language language)
	{
		if (language == null)
		{
			throw new NullPointerException("Cannot register null Language");
		}
		
		List<String> langs = language.getSupportedLanguages();
		
		for (String lang : langs)
		{
			if (languages.containsKey(lang))
			{
				log.warn("Overriding language "+lang+" with class "+language.getClass().getName());
			}
		
			languages.put(lang, language);
		}
	}
	
	public Language getLanguage (String language)
	{
		if (!languages.containsKey(language))
		{
			return new Language();
		}
		
		return languages.get(language);
	}
	public void clear ()
	{
		languages.clear();
	}
	
	public int size ()
	{
		return languages.size();
	}
}
