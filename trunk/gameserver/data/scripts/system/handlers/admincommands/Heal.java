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
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Heal extends AdminCommand
{
	public Heal()
	{
		super("heal");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_HEAL)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		String syntax = "//heal : Full HP and MP\n" + "//heal dp : Full DP, must be used on a players only !" + "//heal fp";

		VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "No target selected");
			return;
		}

		Creature creature = (Creature) target;

		if (params == null || params.length < 1)
		{
			if (target instanceof Creature)
			{
				creature.getLifeStats().increaseHp(TYPE.HP, creature.getLifeStats().getMaxHp()+1); 
				creature.getLifeStats().increaseMp(TYPE.MP, creature.getLifeStats().getMaxMp()+1);
				PacketSendUtility.sendMessage(admin, creature.getName() + " HP and MP has been fully refreshed !");
			}
		}
		else if (params[0].equals("dp") && target instanceof Player)
		{
			((Player) creature).getCommonData().setDp(creature.getGameStats().getCurrentStat(StatEnum.MAXDP));
			
			PacketSendUtility.sendMessage(admin, creature.getName() + " DP has been fully refreshed !");
		}
		else if (params[0].equals("fp") && target instanceof Player)
		{
			((Player) creature).getLifeStats().setCurrentFp(((Player) creature).getLifeStats().getMaxFp());
			
			PacketSendUtility.sendMessage(admin, creature.getName() + " FP has been fully refreshed !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}
	}
}