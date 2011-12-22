/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

import gameserver.GameServerError;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET;
import gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET.PacketElementType;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AdvSendFakeServerPacket extends AdminCommand
{
	private static final Logger logger = Logger.getLogger(AdvSendFakeServerPacket.class);

	private static final File FOLDER = new File("./data/packets");

	private Unmarshaller unmarshaller;

	/**
	 * Create an instance of admin command.
	 *
	 * @throws GameServerError on initialization error
	 */

	public AdvSendFakeServerPacket()
	{
		super("send");
		// init unmrshaller once.
		try
		{
			unmarshaller = JAXBContext.newInstance(Packets.class, Packet.class, Part.class).createUnmarshaller();
		}
		catch (Exception e)
		{
			throw new GameServerError("Failed to initialize unmarshaller.", e);
		}
	}

	/** {@inheritDoc} */

	@Override
	public void executeCommand(final Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ADVSENDFAKESERVERPACKET)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if (params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_SEND_SYNTAX));
			return;
		}

		final String mappingName = params[0];
		final Player target = getTargetPlayer(admin);

		//logger.debug("Mapping: " + mappingName);
		//logger.debug("Target: " + target);

		File packetsData = new File(FOLDER, mappingName + ".xml");

		if (!packetsData.exists())
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, mappingName));
			return;
		}

		final Packets packetsTemplate;

		try
		{
			packetsTemplate = (Packets) unmarshaller.unmarshal(packetsData);
		}
		catch (JAXBException e)
		{
			logger.error("Unmarshalling error", e);
			return;
		}

		if (packetsTemplate.getPackets().isEmpty())
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_SEND_NO_PACKET));
			return;
		}

		send(admin, target, packetsTemplate);
	}

	private void send(Player sender,final Player target, Packets packets)
	{
		final String senderObjectId = String.valueOf(sender.getObjectId());
		final String targetObjectId = String.valueOf(target.getObjectId());

		int packetIndex = 0;// first packet should be sent immediately.
		for (final Packet packetTemplate : packets)
		{
			//logger.debug("Processing: " + packetTemplate);

			final SM_CUSTOM_PACKET packet = new SM_CUSTOM_PACKET(packetTemplate.getOpcode());

			for (Part part : packetTemplate.getParts())
			{
				PacketElementType byCode = PacketElementType.getByCode(part.getType());

				String value = part.getValue();

				if (value.indexOf("${objectId}") != -1)
					value = value.replace("${objectId}", targetObjectId);
				if (value.indexOf("${senderObjectId}") != -1)
					value = value.replace("${senderObjectId}", senderObjectId);
				if (value.indexOf("${targetObjectId}") != -1)
					value = value.replace("${targetObjectId}", targetObjectId);

				if (part.getRepeatCount() == 1) // skip loop
				{
					packet.addElement(byCode, value);
				}
				else
				{
					for (int i = 0; i < part.getRepeatCount(); i++)
						packet.addElement(byCode, value);
				}
			}

			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					//logger.debug("Sending: " + packetTemplate);
					PacketSendUtility.sendPacket(target, packet);
				}
			}, packetIndex * packets.getDelay()); //Kamui: this is correct or a mistake?

			packetIndex++;
		}
	}

	private Player getTargetPlayer(Player admin)
	{
		if (admin.getTarget() instanceof Player)
			return (Player) admin.getTarget();
		else
			return admin;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "packets")
	private static class Packets implements Iterable<Packet>
	{
		@XmlElement(name = "packet")
		private List<Packet> packets = new ArrayList<Packet>();

		@XmlAttribute(name = "delay")
		private long delay = -1;

		public long getDelay()
		{
			return delay;
		}

		public List<Packet> getPackets()
		{
			return packets;
		}

		@SuppressWarnings("unused")
		public boolean add(Packet packet)
		{
			return packets.add(packet);
		}

		@Override
		public Iterator<Packet> iterator()
		{
			return packets.iterator();
		}

		@Override
		public String toString()
		{
			final StringBuilder sb = new StringBuilder();
			sb.append("Packets");
			sb.append("{delay=").append(delay);
			sb.append(", packets=").append(packets);
			sb.append('}');
			return sb.toString();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "packet")
	private static class Packet
	{
		@XmlElement(name = "part")
		private Collection<Part> parts = new ArrayList<Part>();

		@XmlAttribute(name = "opcode")
		private String opcode = "-1";

		public int getOpcode()
		{
			return Integer.decode(opcode);
		}

		public Collection<Part> getParts()
		{
			return parts;
		}

		@Override
		public String toString()
		{
			final StringBuilder sb = new StringBuilder();
			sb.append("Packet");
			sb.append("{opcode=").append(opcode);
			sb.append(", parts=").append(parts);
			sb.append('}');
			return sb.toString();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "part")
	private static class Part
	{
		@XmlAttribute(name = "type", required = true)
		private String type = null;

		@XmlAttribute(name = "value", required = true)
		private String value = null;

		@XmlAttribute(name = "repeat", required = true)
		private int repeatCount = 1;

		public char getType()
		{
			return type.charAt(0);
		}

		public String getValue()
		{
			return value;
		}

		public int getRepeatCount()
		{
			return repeatCount;
		}

		@Override
		public String toString()
		{
			final StringBuilder sb = new StringBuilder();
			sb.append("Part");
			sb.append("{type='").append(type).append('\'');
			sb.append(", value='").append(value).append('\'');
			sb.append(", repeatCount=").append(repeatCount);
			sb.append('}');
			return sb.toString();
		}
	}
}