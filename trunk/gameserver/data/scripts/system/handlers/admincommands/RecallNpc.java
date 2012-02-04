/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package admincommands;

import commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.SpawnDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;
import org.apache.log4j.Logger;

public class RecallNpc extends AdminCommand
{
	@SuppressWarnings("unused")
	private static final Logger	log			= Logger.getLogger(FixZ.class);
	
	public RecallNpc()
	{
		super("recall_npc");
	}

	@Override
	public void executeCommand(final Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_RECALLNPC)
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
				DAOManager.getDAO(SpawnDAO.class).addSpawn(temp.getSpawnGroup().getNpcid(), admin.getObjectId(), comment.toString(), false, temp.getWorldId(), admin.getX(), admin.getY(), admin.getZ(), temp.getHeading(), target.getObjectId(),isObject);
				//spawn npc
				int time = 1000;
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(temp.getWorldId(), admin.getInstanceId(), temp.getSpawnGroup().getNpcid(), admin.getX(), admin.getY(), admin.getZ(), temp.getHeading(), temp.getWalkerId(), temp.getRandomWalkNr(), false, true);
						VisibleObject vs = SpawnEngine.getInstance().spawnObject(spawn, admin.getInstanceId(), false);
						vs.getKnownList().doUpdate();
					}
				}, time);
				PacketSendUtility.sendMessage(admin, comment.toString()+" spawned");
			}
			else 
			{
				PacketSendUtility.sendMessage(admin, "Only instances of NPC are allowed as target!");
				return;
			}
		}
		else
			PacketSendUtility.sendMessage(admin, "Target cant be null");
			
	}
}