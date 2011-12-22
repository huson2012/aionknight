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

package commons.versionning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;

public class Version
{
	private static final Logger	log	= Logger.getLogger(Version.class);
	private String				revision;
	private String				date;

	public Version()
	{
	}

	public Version(Class<?> c)
	{
		loadInformation(c);
	}

	public void loadInformation(Class<?> c)
	{
		File jarName = null;
		try
		{
			jarName = Locator.getClassSource(c);
			JarFile jarFile = new JarFile(jarName);

			Attributes attrs = jarFile.getManifest().getMainAttributes();

			setRevision(attrs);

			setDate(attrs);

		}
		catch(IOException e)
		{
			log.error("Unable to get Soft information\nFile name '"
				+ (jarName == null ? "null" : jarName.getAbsolutePath()) + "' isn't a valid jar", e);
		}

	}

	public void transferInfo(String jarName, String type, File fileToWrite)
	{
		try
		{
			if(!fileToWrite.exists())
			{
				log.error("Unable to Find File :" + fileToWrite.getName() + " Please Update your " + type);
				return;
			}
			// Open the JAR file
			JarFile jarFile = new JarFile("./" + jarName);
			// Get the manifest
			Manifest manifest = jarFile.getManifest();
			// Write the manifest to a file
			OutputStream fos = new FileOutputStream(fileToWrite);
			manifest.write(fos);
			fos.close();
		}
		catch(IOException e)
		{
			log.error("Error, " + e);
		}
	}

	public void setRevision(Attributes attrs)
	{
		String revision = attrs.getValue("Revision");

		if(revision != null)
			this.revision = revision;
		else
			this.revision = "Unknown Revision Number.";
	}

	public String getRevision()
	{
		return revision;
	}

	public void setDate(Attributes attrs)
	{
		String date = attrs.getValue("Date");

		if(date != null)
			this.date = date;
		else
			this.date = "Unknown Date Time.";
	}

	public String getDate()
	{
		return date;
	}
}