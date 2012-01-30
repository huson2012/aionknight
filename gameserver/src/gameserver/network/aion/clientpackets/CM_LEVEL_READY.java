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

package gameserver.network.aion.clientpackets;

import commons.database.dao.DAOManager;
import gameserver.configs.main.CustomConfig;
import gameserver.dao.PlayerCmotionListDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.windstreams.Location2D;
import gameserver.model.templates.windstreams.WindstreamTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_CMOTION;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM_LOCATIONS;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.services.WeatherService;
import gameserver.spawn.RiftSpawnManager;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_LEVEL_READY extends AionClientPacket
{
	private static Logger	log	= Logger.getLogger(CM_LEVEL_READY.class);	
	
	/**
	 * Constructs new instance of <tt>CM_LEVEL_READY </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_LEVEL_READY(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		if(activePlayer.isSpawned() && activePlayer.getOldWorldId()==activePlayer.getWorldId())
		{
			log.info("[ANTICHEAT] Fake CM_LEVEL_READY sent by player: " + activePlayer.getName());
			return;
		}

			
		WindstreamTemplate template = DataManager.WINDSTREAM_DATA.
			getStreamTemplate(activePlayer.getPosition().getMapId());
		Location2D location;
		if(template != null)
			for(int i = 0; i < template.getLocations().getLocation().size(); i++)
			{
				location = template.getLocations().getLocation().get(i);
				sendPacket(new SM_WINDSTREAM_LOCATIONS(location.getBidirectional(), 
					template.getMapid(), location.getId(), location.getBoost()));
			}
		location = null;
		template = null;
		sendPacket(new SM_PLAYER_INFO(activePlayer, false));
		activePlayer.getController().startProtectionActiveTask();

		/**
		 * Spawn player into the world.
		 */
		 // If already spawned, despawn before spawning into the world
		if(activePlayer.isSpawned())
			World.getInstance().despawn(activePlayer);
		World.getInstance().spawn(activePlayer);
		
		activePlayer.getController().refreshZoneImpl();
		
		/**
		 * Loading weather for the player's region
		 */
		WeatherService.getInstance().loadWeather(activePlayer);

		QuestEngine.getInstance().onEnterWorld(new QuestCookie(null, activePlayer, 0, 0));
		
		activePlayer.getController().onEnterWorld();
		// zone channel message
		sendPacket(new SM_SYSTEM_MESSAGE(1390122, activePlayer.getPosition().getInstanceId()));
		
		RiftSpawnManager.sendRiftStatus(activePlayer);
		
		activePlayer.getEffectController().updatePlayerEffectIcons();
		
	      //Cmotion 
        if(CustomConfig.RETAIL_CMOTIONS || activePlayer.getCmotionList().getCmotions() != null )
        {
            for (Cmotion cmotion : activePlayer.getCmotionList().getCmotions()) 
            {   
                int dummy = 0;
                if(!cmotion.getActive())
                    cmotion.setActive(false);
                else
                {
                    cmotion.setActive(true);
                    if(cmotion.getCmotionId() > 4)
                        dummy = cmotion.getCmotionId() - 4;
                    else
                        dummy = cmotion.getCmotionId();
                        
                    PacketSendUtility.sendPacket(activePlayer, new SM_CMOTION(dummy, cmotion.getCmotionId()));
                }
            }
            DAOManager.getDAO(PlayerCmotionListDAO.class).storeCmotions(activePlayer);
        }
        
        if((!CustomConfig.RETAIL_CMOTIONS) && (CustomConfig.CMOTIONS_GETLEVEL <= activePlayer.getLevel()))
        {
            for (int i = 1; i < 9; i++)
            {
                activePlayer.getCmotionList().add(i,  false, System.currentTimeMillis(),(60  * 60L));
            }
            
            for (Cmotion cmotion : activePlayer.getCmotionList().getCmotions()) 
            {   
                int dummy = 0;
                if(!cmotion.getActive())
                    cmotion.setActive(false);
                else
                {
                    cmotion.setActive(true);
                    if(cmotion.getCmotionId() > 4)
                        dummy = cmotion.getCmotionId() - 4;
                    else
                        dummy = cmotion.getCmotionId();                 
                    PacketSendUtility.sendPacket(activePlayer, new SM_CMOTION(dummy, cmotion.getCmotionId()));
                }
            }
        }
        PacketSendUtility.sendPacket(activePlayer, new SM_CMOTION(activePlayer));         
		
	}
}
