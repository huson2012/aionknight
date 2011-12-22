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

package commons.scripting.url;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import commons.scripting.ScriptClassLoader;

/**
 * This class represents URL Stream handler that accepts {@value #HANDLER_PROTOCOL} protocol
 */
public class VirtualClassURLStreamHandler extends URLStreamHandler
{
	/**
	 * Script Handler protocol for classes compiled from source
	 */
	public static final String		HANDLER_PROTOCOL	= "aescript://";

	/**
	 * Script class loader that loaded those classes
	 */
	private final ScriptClassLoader	cl;

	/**
	 * Creates new instance of url stream handler with given classloader
	 * 
	 * @param cl
	 *           ScriptClassLoaderImpl that was used to load compiled class
	 */
	public VirtualClassURLStreamHandler(ScriptClassLoader cl)
	{
		this.cl = cl;
	}

	/**
	 * Opens new URL connection for URL
	 * 
	 * @param u
	 *           url
	 * @return Opened connection
	 * @throws IOException
	 *            never thrown
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException
	{
		return new VirtualClassURLConnection(u, cl);
	}
}
