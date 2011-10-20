package admincommands;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.VisibleObject;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;

/**
* @author iopiop
*
*/
public class Dispel extends AdminCommand
{
	public Dispel()
	{
		super("dispel");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_DISPEL)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		VisibleObject creature = admin.getTarget();

		if (creature == null)
		{
			PacketSendUtility.sendMessage(admin, "You should select a target first!");
			return;
		}

		if (creature instanceof Player || creature instanceof Creature)
		{
			Creature targets = (Creature) creature;
			targets.getEffectController().removeAllEffects();
			PacketSendUtility.sendMessage(admin, creature.getName() + " had all buff effects dispelled !");
		}
	}
}