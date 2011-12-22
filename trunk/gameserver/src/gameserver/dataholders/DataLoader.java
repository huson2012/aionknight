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

package gameserver.dataholders;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

abstract class DataLoader
{
	protected Logger log = Logger.getLogger(getClass().getName());
	private static final String	PATH = "./data/static_data/";
	private File dataFile;
	DataLoader(String file)
	{
		this.dataFile = new File(PATH + file);
	}

	protected void loadData()
	{
		if(dataFile.isDirectory())
		{
			Collection<?> files = FileUtils.listFiles(dataFile, FileFilterUtils.and(FileFilterUtils
				.and(FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("new")), FileFilterUtils
					.suffixFileFilter(".txt")), HiddenFileFilter.VISIBLE), HiddenFileFilter.VISIBLE);

			for(Object file1 : files)
			{
				File f = (File) file1;
				loadFile(f);
			}
		}
		else
		{
			loadFile(dataFile);
		}
	}

	private void loadFile(File file)
	{
		LineIterator it = null;
		try
		{
			it = FileUtils.lineIterator(file);
			while(it.hasNext())
			{
				String line = it.nextLine(); 
				if(line.isEmpty() || line.startsWith("#"))
				{
					continue;
				}
				parse(line);
			}
		}
		catch(IOException e)
		{
			log.error("Error loading " + getClass().getSimpleName() + ", file: " + file.getPath(), e);
		}
		finally
		{
			LineIterator.closeQuietly(it);
		}
	}

	protected abstract void parse(String dataEntry);
	public boolean saveData()
	{
		String desc = PATH + getSaveFile();

		log.info("Saving " + desc);

		FileWriter fr = null;
		try
		{
			fr = new FileWriter(desc);

			saveEntries(fr);

			fr.flush();

			return true;
		}
		catch(Exception e)
		{
			log.fatal("Error while saving " + desc, e);
			return false;
		}
		finally
		{
			if(fr != null)
			{
				try
				{
					fr.close();
				}
				catch(Exception e)
				{
					log.fatal("Error while closing save data file", e);
				}
			}
		}
	}
	protected abstract String getSaveFile();
	protected void saveEntries(FileWriter fileWriter) throws Exception
	{}
}