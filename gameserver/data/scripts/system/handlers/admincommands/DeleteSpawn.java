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
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.PacketSendUtility;
import gameserver.dao.SpawnDAO;

import commons.database.dao.DAOManager;

/**
 * @author Luno
 * 
 */

public class DeleteSpawn extends AdminCommand
{

	public DeleteSpawn()
	{
		super("delete");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_DELETESPAWN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		VisibleObject cre = admin.getTarget();
		if (!(cre instanceof Npc))
		{
			PacketSendUtility.sendMessage(admin, "Не был взят тагрет спавна.");
			return;
		}
		
		Npc npc = (Npc) cre;
        SpawnTemplate template = npc.getSpawn();
        int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(template.getSpawnGroup().getNpcid(), template.getX(), template.getY());
        DAOManager.getDAO(SpawnDAO.class).deleteSpawn(spawnId);
		
        DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
        npc.getController().delete();
        PacketSendUtility.sendMessage(admin, "Спавн был удален!");
		

	}
}
