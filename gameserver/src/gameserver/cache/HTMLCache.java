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

package gameserver.cache;

import gameserver.configs.main.HTMLConfig;
import javolution.util.FastMap;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public final class HTMLCache
{
    private static final Logger log = Logger.getLogger(HTMLCache.class);
    private static final FileFilter HTML_FILTER = new FileFilter()
    {
		@Override
		public boolean accept(File file)
		{
			return file.isDirectory() || file.getName().endsWith(".xhtml");
		}
    };

    private static final File HTML_ROOT = new File(HTMLConfig.HTML_ROOT);
    private static final class SingletonHolder
    {
		private static final HTMLCache INSTANCE = new HTMLCache();
    }

    public static HTMLCache getInstance()
    {
		return SingletonHolder.INSTANCE;
    }

    private FastMap<String, String> cache   = new FastMap<String, String>(16000);
    private int loadedFiles;
    private int size;

    private HTMLCache()
    {
		reload(false);
    }

    @SuppressWarnings("Unchecked")
    public synchronized void reload(boolean deleteCacheFile)
    {
		cache.clear();
		loadedFiles = 0;
		size = 0;

		final File cacheFile = getCacheFile();

		if(deleteCacheFile && cacheFile.exists())
		{
			log.info("HTML Cache: Deleting cache file... Ok!");
			cacheFile.delete();
		}

		log.info("HTML Cache: Caching started... Ok!");

		if(cacheFile.exists())
		{
			log.info("HTML Cache: Using cache file... Ok!");
			ObjectInputStream ois = null;
			try
		{
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(getCacheFile())));
			cache = (FastMap<String, String>) ois.readObject();
			for(String html : cache.values())
		{
			loadedFiles++;
			size += html.length();
		}
			}
				catch(Exception e)
				{
					log.warn("", e);
					reload(true);
					return;
				}
                    finally
				{
					IOUtils.closeQuietly(ois);
				}
            }
            else
            {
				parseDir(HTML_ROOT);
            }
            log.info(String.valueOf(this));
            if(cacheFile.exists())
            {
				log.info("HTML Cache: Compaction skipped!");
            }
            else
            {
				log.info("HTML Cache: Compacting htmls... Ok!");
				final StringBuilder sb = new StringBuilder(8192);

				for(Entry<String, String> entry : cache.entrySet())
                    {
						try
					{
						final String oldHtml = entry.getValue();
						final String newHtml = compactHtml(sb, oldHtml);
						size -= oldHtml.length();
						size += newHtml.length();
						entry.setValue(newHtml);
					}
						catch(RuntimeException e)
					{
					log.warn("[!] HTML Cache: Error during compaction of " + entry.getKey(), e);
				}
			}
				log.info(String.valueOf(this));
            }

            if(!cacheFile.exists())
            {
				log.info("HTML Cache: Creating cache file... Ok!");
				ObjectOutputStream oos = null;
				try
				{
					oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getCacheFile())));
					oos.writeObject(cache);
				}
                catch(IOException e)
                {
					log.warn("", e);
                }
                finally
			{
				IOUtils.closeQuietly(oos);
            }
		}
    }

    private File getCacheFile()
    {
		return new File(HTMLConfig.HTML_CACHE_FILE);
    }

    private static final String[] TAGS_TO_COMPACT;
    static
    {
		final String[] tagsToCompact = { "html", "title", "body", "br", "br1", "p", "table", "tr", "td" };
		final List<String> list = new ArrayList<String>();

		for(String tag : tagsToCompact)
		{
			list.add('<' + tag + '>');
			list.add("</" + tag + '>');
			list.add('<' + tag + "/>");
			list.add('<' + tag + " />");
		}
            final List<String> list2 = new ArrayList<String>();
            for(String tag : list)
		{
			list2.add(tag);
			list2.add(tag + ' ');
			list2.add(' ' + tag);
		}

		TAGS_TO_COMPACT = list2.toArray(new String[list.size()]);
    }

    private String compactHtml(StringBuilder sb, String html)
    {
		sb.setLength(0);
		sb.append(html);
		
		for(int i = 0; i < sb.length(); i++)
		if(Character.isWhitespace(sb.charAt(i)))
		sb.setCharAt(i, ' ');
		replaceAll(sb, "  ", " ");
		replaceAll(sb, "< ", "<");
		replaceAll(sb, " >", ">");
		
		for(int i = 0; i < TAGS_TO_COMPACT.length; i += 3)
		{
			replaceAll(sb, TAGS_TO_COMPACT[i + 1], TAGS_TO_COMPACT[i]);
			replaceAll(sb, TAGS_TO_COMPACT[i + 2], TAGS_TO_COMPACT[i]);
		}
		
		replaceAll(sb, "  ", " ");
		
		int fromIndex = 0;
		int toIndex = sb.length();
		
		while(fromIndex < toIndex && sb.charAt(fromIndex) == ' ')
			fromIndex++;
		
		while(fromIndex < toIndex && sb.charAt(toIndex - 1) == ' ')
			toIndex--;
		
		return sb.substring(fromIndex, toIndex);
    }

    private void replaceAll(StringBuilder sb, String pattern, String value)
    {
		for(int index = 0; (index = sb.indexOf(pattern, index)) != -1;)
		sb.replace(index, index + pattern.length(), value);
    }

    public void reloadPath(File f)
    {
		parseDir(f);
		log.info("HTML Cache: Reloaded specified path.");
    }

    public void parseDir(File dir)
    {
		for(File file : dir.listFiles(HTML_FILTER))
		{
		if(!file.isDirectory())
			loadFile(file);
		else
			parseDir(file);
		}
    }

    public String loadFile(File file)
    {
		if(isLoadable(file))
		{
			BufferedInputStream bis = null;
			try
			{
				bis = new BufferedInputStream(new FileInputStream(file));
				byte[] raw = new byte[bis.available()];
				bis.read(raw);
	
				String content = new String(raw, HTMLConfig.HTML_ENCODING);
				String relpath = getRelativePath(HTML_ROOT, file);
	
				size += content.length();
	
				String oldContent = cache.get(relpath);
				if(oldContent == null)
					loadedFiles++;
				else
					size -= oldContent.length();
			cache.put(relpath, content);
				return content;
			}
			catch(Exception e)
			{
				log.warn("[!] Problem with HTML file: ", e);
			}
		finally
		{
			IOUtils.closeQuietly(bis);
		}
	}
	
	return null;
    }

    public String getHTML(String path)
    {
		return cache.get(path);
    }

    private boolean isLoadable(File file)
    {
		return file.exists() && !file.isDirectory() && HTML_FILTER.accept(file);
    }

    public boolean pathExists(String path)
    {
		return cache.containsKey(path);
    }

    @Override
    public String toString()
    {
		return "HTML Cache: " + String.format("%.3f", (float) size / 1024) + " Kb on " + loadedFiles
		+ " file(s)";
    }

    public static String getRelativePath(File base, File file)
    {
		return file.toURI().getPath().substring(base.toURI().getPath().length());
    }
}