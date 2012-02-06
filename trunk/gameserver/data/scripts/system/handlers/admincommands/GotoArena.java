package admincommands;
/*
 * @author Geqyen
 * skype geqyen
 */

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.WorldMapInstance;
import gameserver.world.WorldMapType;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.configs.administration.AdminConfig;;

public class GotoArena extends AdminCommand {
	public GotoArena()
	{
		super("arena");
	}
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ARENA)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}
		if (admin.getCommonData().getRace().equals(Race.ELYOS))
		{
			TeleportService.freeTeleport(admin, 110070000, 444.9f, 251.61f, 127f);
			return;
		}
		if (admin.getCommonData().getRace().equals(Race.ASMODIANS))
		{
			TeleportService.freeTeleport(admin, 120080000, 549.99f, 204.3f, 94f);
			return;
		}
			}
}
