package admincommands;


import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.CubeExpandService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;


/**
 * @author Kamui
 *
 */

public class AddCube extends AdminCommand
{

	public AddCube()
	{
		super("addcube");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDCUBE)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if (params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_SYNTAX));
			return;
		}

		Player receiver = null;

		receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (receiver == null)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.PLAYER_NOT_ONLINE, Util.convertName(params[0])));
			return;
		}

		if (receiver != null)
		{
			if (receiver.getCubeSize() < 9)
			{
				CubeExpandService.expand(receiver);
				PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_ADMIN_SUCCESS, receiver.getName()));
				PacketSendUtility.sendMessage(receiver, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_PLAYER_SUCCESS, admin.getName()));
			}
			else
			{
				PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_FAILURE, receiver.getName()));
				return;
			}
		}
	}
}
