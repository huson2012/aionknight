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

package commons.utils.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleControl extends ResourceBundle.Control
{
	private String encoding	= "UTF-8";

	public ResourceBundleControl()
	{
	}

	public ResourceBundleControl(String encoding)
	{
		this.encoding = encoding;
	}

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
		throws IllegalAccessException, InstantiationException, IOException
	{
		String bundleName = toBundleName(baseName, locale);
		ResourceBundle bundle = null;
		if(format.equals("java.class"))
		{
			try
			{
				@SuppressWarnings( { "unchecked" })
				Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader
					.loadClass(bundleName);

				if(ResourceBundle.class.isAssignableFrom(bundleClass))
				{
					bundle = bundleClass.newInstance();
				}
				else
				{
					throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
				}
			}
			catch(ClassNotFoundException ignored)
			{
			}
		}
		else if(format.equals("java.properties"))
		{
			final String resourceName = toResourceName(bundleName, "properties");
			final ClassLoader classLoader = loader;
			final boolean reloadFlag = reload;
			InputStreamReader isr = null;
			InputStream stream;
			try
			{
				stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>(){
					@Override
					public InputStream run() throws IOException
					{
						InputStream is = null;
						if(reloadFlag)
						{
							URL url = classLoader.getResource(resourceName);
							if(url != null)
							{
								URLConnection connection = url.openConnection();
								if(connection != null)
								{
									connection.setUseCaches(false);
									is = connection.getInputStream();
								}
							}
						}
						else
						{
							is = classLoader.getResourceAsStream(resourceName);
						}
						return is;
					}
				});
				if(stream != null)
				{
					isr = new InputStreamReader(stream, encoding);
				}
			}
			catch(PrivilegedActionException e)
			{
				throw (IOException) e.getException();
			}
			if(isr != null)
			{
				try
				{
					bundle = new PropertyResourceBundle(isr);
				}
				finally
				{
					isr.close();
				}
			}
		}
		else
		{
			throw new IllegalArgumentException("unknown format: " + format);
		}
		return bundle;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
}