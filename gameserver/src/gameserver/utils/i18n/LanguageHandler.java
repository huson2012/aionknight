/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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
