/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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