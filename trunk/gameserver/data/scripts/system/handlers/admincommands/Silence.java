package admincommands;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;

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
