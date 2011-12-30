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
import gameserver.model.gameobjects.player.Player;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.zone.FlightZoneInstance;
import gameserver.world.zone.ZoneInstance;

public class Zone extends AdminCommand
{
	public Zone()
	{
		super("zone");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ZONE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length == 0)
		{
			ZoneInstance zoneInstance = admin.getZoneInstance();
			if (zoneInstance == null)
			{
				PacketSendUtility.sendMessage(admin, "You are out of any zone");
			}
			else
			{
				String zoneName = zoneInstance.getTemplate().getName().name();
				PacketSendUtility.sendMessage(admin, "You are in zone: " + zoneName);
			}
			if (ZoneService.getInstance().mapHasFightZones(admin.getWorldId()))
			{
				FlightZoneInstance currentFlightZoneName = ZoneService.getInstance().findFlightZoneInCurrentMap(admin.getPosition());
				if (currentFlightZoneName != null)
				{
					PacketSendUtility.sendMessage(admin, "You are in flightzone: "+currentFlightZoneName.getTemplate().getName());
				}
				else
					PacketSendUtility.sendMessage(admin, "You are out of any flightzone");
			}
			else
				PacketSendUtility.sendMessage(admin, "No flight zones in the map");
		}
		else if ("refresh".equalsIgnoreCase(params[0]))
		{
			ZoneService.getInstance().findZoneInCurrentMap(admin);
		}
	}
}