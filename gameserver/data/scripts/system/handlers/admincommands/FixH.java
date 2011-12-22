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

import commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.SpawnDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;

public class FixH extends AdminCommand
{
	private byte heading = 0;
	
	public FixH()
	{
		super("fixh");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_FIXH)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
			return;
		}

		if (params.length == 0 && admin.getTarget() != null)
		{
			if(admin.getTarget() instanceof Npc)
			{
				Npc target = (Npc) admin.getTarget();
				final SpawnTemplate temp = target.getSpawn();
				int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(temp.getSpawnGroup().getNpcid(),temp.getX(),temp.getY());
				
				//count heading
				heading = admin.getHeading();
				if (heading > 60)
					heading -= 60;
				else
					heading += 60;
									
				if (spawnId != 0)
				{
					//update heading in db
					if (!DAOManager.getDAO(SpawnDAO.class).updateHeading(spawnId,heading))
					{
						PacketSendUtility.sendMessage(admin, "Couldnt update heading with "+heading+" at spawnid: "+spawnId+" !");
						return;
					}
					
					//delete spawn,npc
					DataManager.SPAWNS_DATA.removeSpawn(temp);
					target.getController().delete();
														
					//spawn npc
					int time = 1500;
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(temp.getWorldId(), 1, temp.getSpawnGroup().getNpcid(), temp.getX(), temp.getY(), temp.getZ(), heading, temp.getWalkerId(), temp.getRandomWalkNr(), false, true);
							SpawnEngine.getInstance().spawnObject(spawn, 1, false);
						}
					}, time);
				}
				else //mob isnt saved in db yet
				{
					//delete spawn,npc
					DataManager.SPAWNS_DATA.removeSpawn(temp);
					target.getController().delete();
					
					//create groupname
					//ex: Aetherogenetics Lab Entrance (Object Normal lvl:1)
					StringBuilder comment = new StringBuilder();
					comment.append(target.getObjectTemplate().getName()+" (");
					int isObject = target.getSpawn().getStaticid();
					if (isObject > 0)
						comment.append("Object");
					else
						comment.append("NPC");
					comment.append(" "+target.getObjectTemplate().getRank().name()+" ");
					comment.append("lvl:"+target.getLevel()+")");
					
					//save to db
					DAOManager.getDAO(SpawnDAO.class).addSpawn(temp.getSpawnGroup().getNpcid(), admin.getObjectId(), comment.toString(), false, temp.getWorldId(), temp.getX(), temp.getY(), temp.getZ(), heading, target.getObjectId(),isObject);
					
					//spawn npc
					int time = 1500;
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(temp.getWorldId(), 1, temp.getSpawnGroup().getNpcid(), temp.getX(), temp.getY(), temp.getZ(), heading, temp.getWalkerId(), temp.getRandomWalkNr(), false, true);
							SpawnEngine.getInstance().spawnObject(spawn, 1, false);
						}
					}, time);
				}
			
			
			}
			else 
			{
				PacketSendUtility.sendMessage(admin, "Only instances of NPC are allowed as target!");
				return;
			}
		}
	}
}
