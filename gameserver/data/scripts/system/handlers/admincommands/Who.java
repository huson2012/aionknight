package admincommands;

import java.util.Collection;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.model.Race;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;
import ru.aionknight.gameserver.world.World;


/**
 * @author ginho1
 *
 */
public class Who extends AdminCommand
{
	public Who()
	{
		super("who");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_WHO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		Collection<Player> players = World.getInstance().getPlayers();

		PacketSendUtility.sendMessage(admin, "List players :");

		for(Player player : players)
		{
			if (params != null && params.length > 0)
			{
				String cmd = params[0].toLowerCase();

				if (("ely").startsWith(cmd))
				{
					if(player.getCommonData().getRace() == Race.ASMODIANS)
						continue;
				}

				if (("asmo").startsWith(cmd))
				{
					if(player.getCommonData().getRace() == Race.ELYOS)
						continue;
				}

				if (("member").startsWith(cmd) || ("premium").startsWith(cmd))
				{
					if(player.getPlayerAccount().getMembership() == 0)
						continue;
				}
			}			

			PacketSendUtility.sendMessage(admin, "Char: " + player.getName() + " - Race: " +  player.getCommonData().getRace().name() + " - Acc: " +  player.getAcountName());
		}		
	}
}
