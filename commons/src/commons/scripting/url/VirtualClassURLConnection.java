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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import commons.scripting.ScriptClassLoader;

/**
 * This class represents URL Connection that is used to "connect" to scripts binary data that was loaded by specified
 * {@link commons.scripting.impl.javacompiler.ScriptCompilerImpl}.<br>
 * <br>
 * TODO: Implement all methods of {@link URLConnection} to ensure valid behaviour
 */
public class VirtualClassURLConnection extends URLConnection
{
	/**
	 * Input stream, is assigned from class
	 */
	private InputStream	is;

	/**
	 * Creates URL connections that "connects" to class binary data
	 * 
	 * @param url
	 *           class name
	 * @param cl
	 *           classloader
	 */
	protected VirtualClassURLConnection(URL url, ScriptClassLoader cl)
	{
		super(url);
		is = new ByteArrayInputStream(cl.getByteCode(url.getHost()));
	}

	/**
	 * This method is ignored
	 */
	@Override
	public void connect() throws IOException
	{

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws IOException
	{
		return is;
	}
}