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
package gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import commons.database.dao.DAOManager;
import gameserver.configs.main.CustomConfig;
import gameserver.dao.PlayerCmotionListDAO;
import gameserver.model.gameobjects.player.Cmotion;

import gameserver.dataholders.DataManager;
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
                if(cmotion.getActive() == false)
                    cmotion.setActive(false);
                else
                {
                    cmotion.setActive(true);
                    if(cmotion.getCmotionId() > 4)
                        dummy = cmotion.getCmotionId() - 4;
                    else
                        dummy = cmotion.getCmotionId();
                        
                    PacketSendUtility.sendPacket(activePlayer, new SM_CMOTION(activePlayer, dummy, cmotion.getCmotionId()));
                }
            }
            DAOManager.getDAO(PlayerCmotionListDAO.class).storeCmotions(activePlayer);
        }
        
        if((CustomConfig.RETAIL_CMOTIONS == false ) && (CustomConfig.CMOTIONS_GETLEVEL <= activePlayer.getLevel()))
        {
            for (int i = 1; i < 9; i++)
            {
                activePlayer.getCmotionList().add(i,  false, System.currentTimeMillis(),(60  * 60L));
            }
            
            for (Cmotion cmotion : activePlayer.getCmotionList().getCmotions()) 
            {   
                int dummy = 0;
                if(cmotion.getActive() == false)
                    cmotion.setActive(false);
                else
                {
                    cmotion.setActive(true);
                    if(cmotion.getCmotionId() > 4)
                        dummy = cmotion.getCmotionId() - 4;
                    else
                        dummy = cmotion.getCmotionId();                 
                    PacketSendUtility.sendPacket(activePlayer, new SM_CMOTION(activePlayer, dummy, cmotion.getCmotionId()));
                }
            }
        }
        PacketSendUtility.sendPacket(activePlayer, new SM_CMOTION(activePlayer));         
		
	}
}
