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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.FlyRingData;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.flyring.FlyRingTemplate;
import gameserver.model.utils3d.Point3D;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.Map;

public class Ring extends AdminCommand
{
	private static Map<String, Map<Integer, FlyRingTemplate>> rings = new FastMap<String, Map<Integer, FlyRingTemplate>>();
	private static String zoneName = null;
	private static int ringId = 0;
	private static String ringName = null;
	private static Point3D center = null;
	private static Point3D p1 = null;
	private static Point3D p2 = null;
	private static int i = 0;
	
	private static Point3D[] ringPositions = new Point3D[] 
	{
        new Point3D(2816.3867f,929.11499f,1538.3674f),
        new Point3D(1220.2118f,2933.1677f,1650.3643f),
        new Point3D(959.63165f,2695.4968f,1628.2689f),
        new Point3D(1129.6411f,2960.25f,1642.4756f),
        new Point3D(3008.085f,1156.7328f,1510.2617f),
        new Point3D(2503.0789f,801.54901f,1521.7606f),
        new Point3D(2732.2942f,900.39032f,1507.7603f),
        new Point3D(1911.6974f,921.92468f,1577.4385f),
        new Point3D(1891.8741f,3065.2195f,1707.4513f),
        new Point3D(954.21027f,2787.4053f,1644.4749f),
        new Point3D(2975.625f,1073.8953f,1540.4493f),
		new Point3D(1015.1357f,2216.473f,1551.0059f),
		new Point3D(3214.3503f,1652.6426f,1441.3378f),
		new Point3D(3119.057f,2411.6426f,2509.3857f),
		new Point3D(2849.7976f,2750.131f,1487.8364f),
		new Point3D(2090f,963f,2938f),
		new Point3D(2446.957f,3076.6487f,1611.5688f),
		new Point3D(2308f,678f,2851f),
		new Point3D(2338f,683f,2860f),
		new Point3D(2823f,969f,2886f),
		new Point3D(2764.2476f,2858.7827f,1488.8556f),
		new Point3D(3144f,979f,2833f),
		new Point3D(3122.9878f,2393.4595f,1502.9283f),
		new Point3D(3213.2393f,1652.0164f,1439.3987f),
		new Point3D(1699f,2751f,2966f),
		new Point3D(1417f,2950f,2949f),
		new Point3D(1229f,2641f,2650f),
		new Point3D(779f,2480f,2949f),
		new Point3D(3118.1934f,1355.3033f,1439.0933f)
	};
	
	public Ring ()
	{
		super("ring");
		if (DataManager.FLY_RING_DATA.size() > 0)
		{
			for (FlyRingTemplate t : DataManager.FLY_RING_DATA.getFlyRingTemplates())
			{
				String region = t.getName().substring(0,t.getName().lastIndexOf('_'));
				int index = Integer.parseInt(t.getName().substring(t.getName().lastIndexOf('_')+1));
				if (!rings.containsKey(region))
				{
					rings.put(region, new FastMap<Integer, FlyRingTemplate>());
				}
				rings.get(region).put(index, t);
			}
		}
	}
	
	private int nextIdForRegion (String region)
	{
		int i = 1;
		
		if (!rings.containsKey(region))
		{
			rings.put(region, new FastMap<Integer, FlyRingTemplate>());
			return 1;
		}
		
		for (; rings.get(region).containsKey(i); i++);
		
		return i;
	}
	
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		String usage = "syntax; //ring <add (c|p1|p2)>|<save>|<next>";
		
		if (admin.getAccessLevel() < AdminConfig.COMMAND_RING)
		{
			PacketSendUtility.sendMessage(admin, "<You don't have the right to execute this command>");
		}
		
		if (params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, usage);
			return;
		}
		
		if (params[0].equalsIgnoreCase("add"))
		{
			if (params.length<2)
			{
				PacketSendUtility.sendMessage(admin, usage);
				return;
			}
			
			if (params[1].equalsIgnoreCase("c"))
			{
				if (params.length == 3 && params[2].equalsIgnoreCase("new"))
				{
					String newZoneName = admin.getZoneInstance().getTemplate().getName().name();
					if (zoneName != null && !zoneName.equalsIgnoreCase(newZoneName))
					{
						zoneName = newZoneName;
					}
				}
				
				if (zoneName == null)
				{
					zoneName = admin.getZoneInstance().getTemplate().getName().name();
				}
				
				center = new Point3D(admin.getX(),admin.getY(),admin.getZ());
				ringId = nextIdForRegion(zoneName);
				ringName = zoneName + "_" + ringId;
				PacketSendUtility.sendMessage(admin, "Center for "+ringName+" added");
			}
			
			if (params[1].equalsIgnoreCase("p1"))
			{
				p1 = new Point3D(admin.getX(),admin.getY(),admin.getZ());
				PacketSendUtility.sendMessage(admin, "Point p1 for "+ringName+" added");
			}
			
			if (params[1].equalsIgnoreCase("p2"))
			{
				p2 = new Point3D(admin.getX(),admin.getY(),admin.getZ());
				PacketSendUtility.sendMessage(admin, "Point p2 for "+ringName+" added");
			}
			
			if (center != null && p1 != null && p2 != null)
			{
				rings.get(zoneName).put(ringId, new FlyRingTemplate(ringName, admin.getWorldId(), center, p1, p2));
				center = null;
				p1 = null;
				p2 = null;
				PacketSendUtility.sendMessage(admin, "Added fly ring "+ringName+" !");
			}
			return;
		}
		
		if (params[0].equalsIgnoreCase("save"))
		{
			Schema schema = null;
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Logger log = Logger.getLogger(Ring.class);
			
			try
			{
				schema = sf.newSchema(new File("./data/static_data/fly_rings/fly_rings.xsd"));
			}
			catch (SAXException e1)
			{
				log.error("Error while saving data: "+e1.getMessage(),e1.getCause());
				PacketSendUtility.sendMessage(admin, "Unexpected error occured during saving");
				return;
			}
			
			File xml = new File("./data/static_data/fly_rings/generated_fly_rings.xml");
			JAXBContext jc;
			Marshaller marshaller;
			FlyRingData data = new FlyRingData();
			for (Map<Integer, FlyRingTemplate> e : rings.values())
			{
				data.addAll(e.values());
			}
			try
			{
				jc = JAXBContext.newInstance(FlyRingData.class);
				marshaller = jc.createMarshaller();
				marshaller.setSchema(schema);
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(data, xml);
			}
			catch (JAXBException e)
			{
				log.error("Error while saving data: "+e.getMessage(), e.getCause());
				PacketSendUtility.sendMessage(admin, "Unexpected error occured during saving");
				return;
			}
			
			PacketSendUtility.sendMessage(admin, "Saved successfully new fly_rings !");
		}
		
		if (params[0].equalsIgnoreCase("next"))
		{
			float x = (float)ringPositions[i].x;
			float y = (float)ringPositions[i].y;
			float z = (float)ringPositions[i].z;
			
			TeleportService.teleportTo(admin, admin.getWorldId(), x, y, z, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to ring "+(i+1)+"/"+ringPositions.length);
			
			i = (i + 1) % ringPositions.length;
		}
	}
}