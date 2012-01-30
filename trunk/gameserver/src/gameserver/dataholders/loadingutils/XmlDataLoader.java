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

package gameserver.dataholders.loadingutils;

import gameserver.dataholders.StaticData;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class XmlDataLoader
{
	private static final Logger	log = Logger.getLogger(XmlDataLoader.class);
	private final static String	XML_SCHEMA_FILE	= "./data/static_data/static_data.xsd";
	private static final String	CACHE_DIRECTORY	= "./cache/";
	private static final String	CACHE_XML_FILE = "./cache/static_data.xml";
	private static final String	MAIN_XML_FILE = "./data/static_data/static_data.xml";
	public static final XmlDataLoader getInstance()
	{
		return SingletonHolder.instance;
	}

	private XmlDataLoader()
	{
	}

	public StaticData loadStaticData()
	{
		makeCacheDirectory();

		File cachedXml = new File(CACHE_XML_FILE);
		File cleanMainXml = new File(MAIN_XML_FILE);

		mergeXmlFiles(cachedXml, cleanMainXml);

		try
		{
			JAXBContext jc = JAXBContext.newInstance(StaticData.class);
			Unmarshaller un = jc.createUnmarshaller();
			un.setSchema(getSchema());
			return (StaticData) un.unmarshal(new File(CACHE_XML_FILE));
		}
		catch(Exception e)
		{
			log.fatal("[!] Error while loading static data", e);
			throw new Error("[!] Error while loading static data", e);
		}
	}

	private Schema getSchema()
	{
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try
		{
			schema = sf.newSchema(new File(XML_SCHEMA_FILE));
		}
		catch(SAXException saxe)
		{
			log.fatal("[!] Error while getting schema", saxe);
			throw new Error("[!] Error while getting schema", saxe);
		}

		return schema;
	}

	private void makeCacheDirectory()
	{
		File cacheDir = new File(CACHE_DIRECTORY);
		if(!cacheDir.exists())
			cacheDir.mkdir();
	}

	private void mergeXmlFiles(File cachedXml, File cleanMainXml) throws Error
	{
		XmlMerger merger = new XmlMerger(cleanMainXml, cachedXml);
		try
		{
			merger.process();
		}
		catch(Exception e)
		{
			log.error("[!] Error while merging xml files", e);
			throw new Error("[!] Error while merging xml files", e);
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final XmlDataLoader instance = new XmlDataLoader();
	}
}