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