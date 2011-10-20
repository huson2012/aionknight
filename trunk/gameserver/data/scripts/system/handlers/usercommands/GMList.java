package usercommands;

import java.util.ArrayList;
import java.util.List;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.gameobjects.stats.modifiers.Executor;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.UserCommand;
import ru.aionknight.gameserver.world.World;


/**
 * @author Sylar
 *
 */
public class GMList extends UserCommand
{
	public GMList ()
	{
		super("gmlist");
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		final List<Player> admins = new ArrayList<Player>();
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			
			@Override
			public boolean run(Player object)
			{
				if(object.getAccessLevel() > 0)
				{
					admins.add(object);
				}
				return true;
			}
		}, true);
		
		if(admins.size() > 0)
		{
			PacketSendUtility.sendMessage(player, admins.size() + " GM(s) online :");
			for(Player a : admins)
			{
				PacketSendUtility.sendMessage(player, a.getName());
			}
		}
		else
			PacketSendUtility.sendMessage(player, "No GM is online currently.");
		
	}

}
