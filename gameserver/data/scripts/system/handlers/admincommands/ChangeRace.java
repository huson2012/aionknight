/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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
import gameserver.model.Race;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author DoYrdenDzirt
 * 
 **/

public class ChangeRace extends AdminCommand {

    public ChangeRace() {
        super("race");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
    	VisibleObject target = admin.getTarget();
    	final Player player;
    	
    	if(admin.getAccessLevel() < AdminConfig.COMMAND_RACE)
    	{
    		PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
    	}
    	
    	if(target == null)
    	{
    		PacketSendUtility.sendMessage(admin, "You should select a player first!");
    		return;
    	}
    	
    	if(target instanceof Player)
    		player = (Player) target;
    	else
    	{
    		PacketSendUtility.sendMessage(admin, "Target must be an instance of Player!");
    		return;
    	}
    	
        if(player.getCommonData().getRace() == Race.ASMODIANS)
        	player.getCommonData().setRace(Race.ELYOS);
        else
        	player.getCommonData().setRace(Race.ASMODIANS);

        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
        World.getInstance().despawn(player);
        World.getInstance().spawn(player);
        	
    }
    

}
