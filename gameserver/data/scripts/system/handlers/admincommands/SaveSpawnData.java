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
package admincommands;

import java.io.File;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import gameserver.configs.administration.AdminConfig;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.SpawnsData;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;


/**
 * @author Luno
 (xml version)
 * 
 */

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
