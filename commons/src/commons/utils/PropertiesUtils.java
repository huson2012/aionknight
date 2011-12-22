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

package commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class PropertiesUtils
{

	public static Properties load(String file) throws IOException
	{
		return load(new File(file));
	}

	public static Properties load(File file) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		Properties p = new Properties();
		p.load(fis);
		fis.close();
		return p;
	}

	public static Properties[] load(String... files) throws IOException
	{
		Properties[] result = new Properties[files.length];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = load(files[i]);
		}
		return result;
	}

	public static Properties[] load(File... files) throws IOException
	{
		Properties[] result = new Properties[files.length];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = load(files[i]);
		}
		return result;
	}

	public static Properties[] loadAllFromDirectory(String dir) throws IOException
	{
		return loadAllFromDirectory(new File(dir), false);
	}

	public static Properties[] loadAllFromDirectory(File dir) throws IOException
	{
		return loadAllFromDirectory(dir, false);
	}

	public static Properties[] loadAllFromDirectory(String dir, boolean recursive) throws IOException
	{
		return loadAllFromDirectory(new File(dir), recursive);
	}

	public static Properties[] loadAllFromDirectory(File dir, boolean recursive) throws IOException
	{
		Collection<File> files = FileUtils.listFiles(dir, new String[] { "ini" }, recursive);
		return load(files.toArray(new File[files.size()]));
	}
}