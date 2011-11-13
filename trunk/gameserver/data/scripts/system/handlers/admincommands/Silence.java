package admincommands;


import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Kamui
 * 
 */
public class Silence extends AdminCommand
{
	public Silence()
	{
		super("silence");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_SILENCE)
		{
			PacketSendUtility.sendMessage(admin, "You do not have enough rights to execute this command");
			return;
		}

		if(admin.isWhisperable())
		{
			admin.setWhisperable(false);
			PacketSendUtility.sendMessage(admin, "Whisper refusal mode enabled.");
			PacketSendUtility.sendMessage(admin, "You are not able to receive whispers.");
		}
		else
		{
			admin.setWhisperable(true);
			PacketSendUtility.sendMessage(admin, "Whisper refusal mode disabled.");
			PacketSendUtility.sendMessage(admin, "You are now able to receive whispers.");
		}
	}
}
