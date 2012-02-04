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
import gameserver.configs.main.*;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import java.lang.reflect.Field;

public class Configure extends AdminCommand
{
	public Configure()
	{
		super("configure");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_CONFIGURE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		String command = "";
		if (params.length == 3)
		{
			//show
			command = params[0];
			if(!"show".equalsIgnoreCase(command))
			{
				PacketSendUtility.sendMessage(admin, "syntax //configure <set | show> <config name> <property> <new value>");
				return;
			}
		}
		
		else if (params.length == 4)
		{
			//set
			command = params[0];
			if (!"set".equalsIgnoreCase(command))
			{
				PacketSendUtility.sendMessage(admin, "syntax //configure <set | show> <config name> <property> <new value>");
				return;
			}
		}
		
		else
		{
			PacketSendUtility.sendMessage(admin, "syntax ///configure <set | show> <config name> <property> <new value>");
			return;
		}

		Class<?> classToMofify = null;
		String className = params[1];

		if ("admin".equalsIgnoreCase(className))
		{
			classToMofify = AdminConfig.class;
		}
		
		else if ("cache".equalsIgnoreCase(className))
		{
			classToMofify = CacheConfig.class;
		}
		
		else if ("custom".equalsIgnoreCase(className))
		{
			classToMofify = CustomConfig.class;
		}
		
		else if ("group".equalsIgnoreCase(className))
		{
			classToMofify = GroupConfig.class;
		}
		
		else if ("gs".equalsIgnoreCase(className))
		{
			classToMofify = GSConfig.class;
		}
		
		else if ("legion".equalsIgnoreCase(className))
		{
			classToMofify = LegionConfig.class;
		}
		else if ("ps".equalsIgnoreCase(className))
		{
			classToMofify = PeriodicSaveConfig.class;
		}
		else if ("rate".equalsIgnoreCase(className))
		{
			classToMofify = RateConfig.class;
		}
		else if ("shutdown".equalsIgnoreCase(className))
		{
			classToMofify = ShutdownConfig.class;
		}
		else if ("task".equalsIgnoreCase(className))
		{
			classToMofify = TaskManagerConfig.class;
		}
		else if ("ip".equalsIgnoreCase(className))
		{
			classToMofify = IPConfig.class;
		}
		else if ("network".equalsIgnoreCase(className))
		{
			classToMofify = NetworkConfig.class;
		}
		else if ("enchants".equalsIgnoreCase(className))
		{
			classToMofify = EnchantsConfig.class;
		}
		else if ("fd".equalsIgnoreCase(className))
		{
			classToMofify = FallDamageConfig.class;
		}
		else if ("nm".equalsIgnoreCase(className))
		{
			classToMofify = NpcMovementConfig.class;
		}
		else if ("prices".equalsIgnoreCase(className))
		{
			classToMofify = PricesConfig.class;
		}
		else if ("siege".equalsIgnoreCase(className))
		{
			classToMofify = SiegeConfig.class;
		}
		else if ("drop".equalsIgnoreCase(className))
		{
			classToMofify = DropConfig.class;
		}
		else if ("event".equalsIgnoreCase(className))
		{
			classToMofify = EventConfig.class;
		}

		if (command.equalsIgnoreCase("show"))
		{
			String fieldName = params[2];
			Field someField;
			try 
			{
				someField = classToMofify.getDeclaredField(fieldName.toUpperCase());
				PacketSendUtility.sendMessage(admin, "Current value is " + someField.get(null));
			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "Something really bad happend :)");
				return;
			}
		}
		else if (command.equalsIgnoreCase("set"))
		{
			String fieldName = params[2];
			String newValue = params[3];
			if (classToMofify != null)
			{
				Field someField;
				try 
				{
					someField = classToMofify.getDeclaredField(fieldName.toUpperCase());
					Class<?> classType = someField.getType();
					if (classType == String.class)
					{
						someField.set(null, newValue); 
					}
					else if (classType == int.class || classType == Integer.class)
					{
						someField.set(null, Integer.parseInt(newValue));
					}
					else if (classType == Boolean.class || classType == boolean.class)
					{
						someField.set(null, Boolean.valueOf(newValue));
					}
					else if (classType == Float.class || classType == float.class)
					{
						someField.set(null, Float.valueOf(newValue));
					}
				}
				catch (Exception e)
				{
					PacketSendUtility.sendMessage(admin, "Something really bad happend :)");
					return;
				}
			}
			PacketSendUtility.sendMessage(admin, "Property changed");
		}
	}
}