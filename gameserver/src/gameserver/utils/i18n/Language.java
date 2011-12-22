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

import javolution.util.FastList;
import javolution.util.FastMap;
import java.util.List;
import java.util.Map;

public class Language
{
	private final List<String> supportedLanguages = new FastList<String> ();
	private final Map<CustomMessageId, String> translatedMessages = new FastMap<CustomMessageId, String>();
	
	public Language ()
	{
		
	}
	
	protected Language (String language)
	{	
		supportedLanguages.add(language);
	}
	
	protected void addSupportedLanguage(String language)
	{
		supportedLanguages.add(language);
	}
	
	public List<String> getSupportedLanguages ()
	{
		return supportedLanguages;
	}
	
	public String translate (CustomMessageId id, Object... params)
	{
		if (translatedMessages.containsKey(id))
		{
			return String.format(translatedMessages.get(id), params);
		}
		
		return String.format(id.getFallbackMessage(), params);
	}
	
	protected void addTranslatedMessage (CustomMessageId id, String message)
	{
		translatedMessages.put(id, message);
	}
}