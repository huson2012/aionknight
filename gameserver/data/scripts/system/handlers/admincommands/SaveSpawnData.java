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
import gameserver.dataholders.SpawnsData;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.List;

public class SaveSpawnData extends AdminCommand
{
	private static Logger log = Logger.getLogger(SaveSpawnData.class);

	public SaveSpawnData()
	{
		super("save_spawn");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_SAVESPAWNDATA)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try
		{
			schema = sf.newSchema(new File("./data/static_data/spawns/spawns.xsd"));
		}
		catch (SAXException e1)
		{
			log.error(e1.getCause());
			PacketSendUtility.sendMessage(admin, "Unexpected error occured during saving");
			return;
		}
		
		boolean isAllSave = params.length == 1 && "all".equalsIgnoreCase(params[0]);
		
		SpawnsData spawnsData = DataManager.SPAWNS_DATA;
		for (WorldMapTemplate template : DataManager.WORLD_MAPS_DATA)
		{
			List<SpawnGroup> spawnsForWorld = null;
			if (isAllSave)
				spawnsForWorld = spawnsData.getSpawnsForWorld(template.getMapId());
			else
				spawnsForWorld = spawnsData.getNewSpawnsForWorld(template.getMapId());

			if (spawnsForWorld != null && spawnsForWorld.size() > 0)
			{
				SpawnsData data = new SpawnsData();
				data.getSpawnGroups().addAll(spawnsForWorld);

				File xml = new File("./data/static_data/spawns/new/" + template.getMapId() + ".xml");
				
				JAXBContext jc;
				Marshaller marshaller;
				try
				{
					jc = JAXBContext.newInstance(SpawnsData.class);
					marshaller = jc.createMarshaller();
					marshaller.setSchema(schema);
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					marshaller.marshal(data, xml);
				}
				catch (JAXBException e)
				{
					log.error(e.getCause());
					PacketSendUtility.sendMessage(admin, "Unexpected error occured during saving");
					return;
				}
			}
		}
		PacketSendUtility.sendMessage(admin, "Spawn data was saved into /static_data/spawns/new folder");
	}
}