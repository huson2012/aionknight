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

package usercommands;

import commons.database.dao.DAOManager;
import gameserver.controllers.PlayerController;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.world.World;
import gameserver.world.WorldPosition;

public class Repair extends UserCommand {
	
	public Repair() {
        super("repair");
    }

    @Override
    public void executeCommand(Player player, String arg) {
    	WorldPosition position	= null;
        Player repairPlayer		= null;
    	PlayerCommonData pcd 	= DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(arg);
        int accountId 			= DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(arg);
        
        if(accountId != player.getAccount().getId())
        {
        	PacketSendUtility.sendMessage(player, "You cannot repair that player");
        	return;
        }
        
        if(player.getName().toLowerCase().equalsIgnoreCase(pcd.getName().toLowerCase()))
        {
        	PacketSendUtility.sendMessage(player, "You cannot repair yourself");
        	return;
        }
        
        switch(pcd.getRace())
        {
        	case ELYOS:
        		position = World.getInstance().createPosition(210010000, 1212.9423f, 1044.8516f, 140.75568f, (byte) 32);
        		pcd.setPosition(position, true);
        		break;
        	case ASMODIANS:
        		position = World.getInstance().createPosition(220010000, 571.0388f, 2787.3420f, 299.8750f, (byte) 32);
        		pcd.setPosition(position, true);
        		break;
        }
        
        repairPlayer = new Player(new PlayerController(), pcd, null, player.getAccount());
        
        DAOManager.getDAO(PlayerDAO.class).storePlayer(repairPlayer);
        PacketSendUtility.sendMessage(player, "Player " + pcd.getName() + " was repaired");
    }
}