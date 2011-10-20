package admincommands;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.gameobjects.stats.modifiers.Executor;
import ru.aionknight.gameserver.services.DredgionInstanceService;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;
import ru.aionknight.gameserver.world.World;


/*
 *
 * @author ginho1
 *
*/
public class Dredgion extends AdminCommand
{
	public Dredgion()
	{
		super("dredgion");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_DREDGION)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		World.getInstance().doOnAllPlayers(new Executor<Player> () {
			@Override
			public boolean run(Player player)
			{
				DredgionInstanceService.getInstance().sendDredgionEntry(player);
				return true;
			}
		});
	}
}
