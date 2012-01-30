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

package gameserver.configs.network;

import commons.network.IPRange;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IPConfig
{
	private static final Logger	log = Logger.getLogger(IPConfig.class);
	private static final String CONFIG_FILE	= "./config/ipconfig.ini";
	private static final List<IPRange> ranges = new ArrayList<IPRange>();
	private static byte[] defaultAddress;
	public static void load()
	{
		try
		{
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File(CONFIG_FILE), new DefaultHandler(){

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException
				{

					if(qName.equals("ipconfig"))
					{
						try
						{
							defaultAddress = InetAddress.getByName(attributes.getValue("default")).getAddress();
						}
						catch(UnknownHostException e)
						{
							throw new RuntimeException("Failed to resolve DSN for address: "
								+ attributes.getValue("default"), e);
						}
					}
					else if(qName.equals("iprange"))
					{
						String min = attributes.getValue("min");
						String max = attributes.getValue("max");
						String address = attributes.getValue("address");
						IPRange ipRange = new IPRange(min, max, address);
						ranges.add(ipRange);
					}
				}
			});
		}
		catch(Exception e)
		{
			log.fatal("Critical error while parsing ipConfig", e);
			throw new Error("Can't load ipConfig", e);
		}
	}

	public static List<IPRange> getRanges()
	{
		return ranges;
	}

	public static byte[] getDefaultAddress()
	{
		return defaultAddress;
	}
}