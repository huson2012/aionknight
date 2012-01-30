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

package gameserver.services;

import commons.database.dao.DAOManager;
import gameserver.dao.PetitionDAO;
import gameserver.model.Petition;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_PETITION;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;
import java.util.*;

public class PetitionService
{
	private static Logger	log	= Logger.getLogger(PetitionService.class);
	
	private static SortedMap<Integer, Petition> registeredPetitions = Collections.synchronizedSortedMap(new TreeMap<Integer, Petition>());
	
	public static final PetitionService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public PetitionService()
	{
		log.info("Loading PetitionService ...");
		Set<Petition> petitions = DAOManager.getDAO(PetitionDAO.class).getPetitions();
		for(Petition p : petitions)
		{
			registeredPetitions.put(p.getPetitionId(), p);
		}
		log.info("Successfully loaded " + registeredPetitions.size() + " database petitions");
	}
	
	public Collection<Petition> getRegisteredPetitions()
	{
		return registeredPetitions.values();
	}
	
	public void deletePetition(int playerObjId)
	{
		synchronized (registeredPetitions)
		{
			for (Iterator<Petition> it = registeredPetitions.values().iterator(); it.hasNext(); )
			{
				Petition p = it.next();

				if(p.getPlayerObjId() == playerObjId)
					it.remove();
			}
		}
		
		DAOManager.getDAO(PetitionDAO.class).deletePetition(playerObjId);
		if(playerObjId > 0 && World.getInstance().findPlayer(playerObjId) != null)
		{
			Player p = World.getInstance().findPlayer(playerObjId);
			PacketSendUtility.sendPacket(p, new SM_PETITION());
		}
		rebroadcastPlayerData();
	}
	
	public void setPetitionReplied(int petitionId)
	{
		int playerObjId = registeredPetitions.get(petitionId).getPlayerObjId();
		DAOManager.getDAO(PetitionDAO.class).setReplied(petitionId);
		registeredPetitions.remove(petitionId);
		rebroadcastPlayerData();
		if(playerObjId > 0 && World.getInstance().findPlayer(playerObjId) != null)
		{
			Player p = World.getInstance().findPlayer(playerObjId);
			PacketSendUtility.sendPacket(p, new SM_PETITION());
		}
	}
	
	public synchronized Petition registerPetition(Player sender, int typeId, String title, String contentText, String additionalData)
	{
		int id = DAOManager.getDAO(PetitionDAO.class).getNextAvailableId();
		Petition ptt = new Petition(id, sender.getObjectId(), typeId, title, contentText, additionalData, 0);
		DAOManager.getDAO(PetitionDAO.class).insertPetition(ptt);
		registeredPetitions.put(ptt.getPetitionId(), ptt);
		broadcastMessageToGM(sender, ptt.getPetitionId());
		return ptt;
	}
	
	private void rebroadcastPlayerData()
	{
		synchronized (registeredPetitions)
		{
			for (Petition p : registeredPetitions.values())
			{
				Player player = World.getInstance().findPlayer(p.getPlayerObjId());
				if(player != null)
					PacketSendUtility.sendPacket(player, new SM_PETITION(p));
			}
		}
	}
	
	private void broadcastMessageToGM(final Player sender, final int petitionId)
	{
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				if(p.getAccessLevel() > 0)
				{
					PacketSendUtility.sendSysMessage(p, "New Support Petition from: " + sender.getName() + " (#" + petitionId + ')');
				}
				return true;
			}
		});
	}
	
	public boolean hasRegisteredPetition(Player player)
	{
		return hasRegisteredPetition(player.getObjectId());
	}
	
	public boolean hasRegisteredPetition(int playerObjId)
	{
		synchronized (registeredPetitions)
		{
			for (Petition p : registeredPetitions.values())
			{
				if (p.getPlayerObjId() == playerObjId)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Petition getPetition(int playerObjId)
	{
		synchronized (registeredPetitions)
		{
			for(Petition p : registeredPetitions.values())
			{
				if(p.getPlayerObjId() == playerObjId)
					return p;
			}
		}
		return null;
	}
	
	public synchronized int getNextAvailablePetitionId()
	{
		return 0;
	}
	
	public int getWaitingPlayers(int playerObjId)
	{
		int counter = 0;
		
		synchronized (registeredPetitions)
		{
			for(Petition p : registeredPetitions.values())
			{
				if(p.getPlayerObjId() == playerObjId)
					break;
				counter++;
			}
		}
		return counter;
	}
	
	public int calculateWaitTime(int playerObjId)
	{
		int timePerPetition = 15;
		int timeBetweenPetition = 30;
		int result = timeBetweenPetition;
		synchronized (registeredPetitions)
		{
			for(Petition p : registeredPetitions.values())
			{
				if(p.getPlayerObjId() == playerObjId)
					break;
				result += timePerPetition;
				result += timeBetweenPetition;
			}
		}
		return result;
	}
	
	public void onPlayerLogin(Player player)
	{
		if(hasRegisteredPetition(player))
			PacketSendUtility.sendPacket(player, new SM_PETITION(getPetition(player.getObjectId())));
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final PetitionService instance = new PetitionService();
	}
}
