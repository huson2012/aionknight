/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
			log.error("[!] Error loading " + getClass().getSimpleName() + ", file: " + file.getPath(), e);
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
			log.fatal("[FATAL] Error while saving " + desc, e);
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
					log.fatal("[FATAL] Error while closing save data file", e);
				}
			}
		}
	}
	protected abstract String getSaveFile();
	protected void saveEntries(FileWriter fileWriter) {}
}