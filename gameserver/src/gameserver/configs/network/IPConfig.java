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

package gameserver.configs.network;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import commons.network.IPRange;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IPConfig
{
	private static final Logger	log = Logger.getLogger(IPConfig.class);
	private static final String CONFIG_FILE	= "./config/ipconfig.xml";
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