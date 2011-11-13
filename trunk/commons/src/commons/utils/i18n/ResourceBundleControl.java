/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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