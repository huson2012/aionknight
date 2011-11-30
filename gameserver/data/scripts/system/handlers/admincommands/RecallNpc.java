/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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