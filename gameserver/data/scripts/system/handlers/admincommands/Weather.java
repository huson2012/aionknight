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
import gameserver.services.WeatherService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.WorldMapType;

public class Weather extends AdminCommand
{

	private final static String	RESET	= "reset";

	private final static String	COMMAND	= "weather";

	public Weather()
	{
		super(COMMAND);
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		// Check restriction level
		if (admin.getAccessLevel() < AdminConfig.COMMAND_WEATHER)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length == 0 || params.length > 2)
		{
			// Syntax :
			// - //weather poeta 0 -> to set clear sky in this region
			// - //weather reset -> to change randomly all weathers in the world

			PacketSendUtility.sendMessage(admin, "syntax //weather <location name> <value>\n//weather reset");
			return;
		}

		String regionName = null;
		int weatherType = -1;

		regionName = new String(params[0]);

		if (params.length == 2)
		{
			try
			{
				weatherType = Integer.parseInt(params[1]);
			}
			catch(NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "weather type parameter need to be an integer [0-8].");
				return;
			}
		}

		if (regionName.equals(RESET))
		{
			WeatherService.getInstance().resetWeather();
			return;
		}

		// Retrieving regionId by name
		WorldMapType region = null;
		for (WorldMapType worldMapType : WorldMapType.values())
		{
			if (worldMapType.name().toLowerCase().equals(regionName.toLowerCase()))
			{
				region = worldMapType;
			}
		}

		if (region != null)
		{
			if (weatherType > -1 && weatherType < 9)
			{
				WeatherService.getInstance().changeRegionWeather(region.getId(), new Integer(weatherType));
			}
			else
			{
				PacketSendUtility.sendMessage(admin, "Weather type must be between 0 and 8");
				return;
			}
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Region " + regionName + " not found");
			return;
		}
	}
}