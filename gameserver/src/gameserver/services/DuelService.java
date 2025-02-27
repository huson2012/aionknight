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

import gameserver.model.DuelResult;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_DUEL;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.model.SkillTargetSlot;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public class DuelService
{
	private static Logger				log		= Logger.getLogger(DuelService.class);

	private FastMap<Integer, Integer>	duels;

	public static final DuelService getInstance()
	{
		return SingletonHolder.instance;
	}

	
	/**
	 * @param duels
	 */
	private DuelService()
	{
		this.duels = new FastMap<Integer, Integer>();
		log.info("DuelService started.");
	}


	/**
	 * Send the duel request to the owner
	 * 
	 * @param requester
	 *           the player who requested the duel
	 * @param responder
	 *           the player who respond to duel request
	 */
	public void onDuelRequest(Player requester, Player responder)
	{
		/**
		 * Check if requester isn't already in a duel and responder is same race
		 */
		if(requester.getInArena() || responder.getInArena())
		{
			PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_PARTNER_INVALID(responder.getName()));
			return;
		}
		if(requester.isEnemyPlayer(responder) || isDueling(requester.getObjectId()))
			return;

		RequestResponseHandler rrh = new RequestResponseHandler(requester){
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				rejectDuelRequest((Player) requester, responder);
			}

			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				startDuel((Player) requester, responder);
			}
		};
		responder.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_DUEL, rrh);
		PacketSendUtility.sendPacket(responder, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_DUEL,
			0, requester.getName()));
		PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.DUEL_ASKED_BY(requester.getName()));
	}

	/**
	 * Asks confirmation for the duel request
	 * 
	 * @param requester
	 *           the player whose the duel was requested
	 * @param responder
	 *           the player whose the duel was responded
	 */
	public void confirmDuelWith(Player requester, Player responder)
	{
		/**
		 * Check if requester isn't already in a duel and responder is same race
		 */
		if(requester.isEnemyPlayer(responder))
			return;

		RequestResponseHandler rrh = new RequestResponseHandler(responder){
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				log.debug("[Duel] Player " + responder.getName() + " confirmed his duel with " + requester.getName());
			}

			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				cancelDuelRequest(responder, (Player) requester);
			}
		};
		requester.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_CONFIRM_DUEL, rrh);
		PacketSendUtility.sendPacket(requester, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_CONFIRM_DUEL,
			0, responder.getName()));
		PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_ASKED_TO(responder.getName()));
	}

	/**
	 * Rejects the duel request
	 * 
	 * @param requester
	 *           the duel requester
	 * @param responder
	 *           the duel responder
	 */
	private void rejectDuelRequest(Player requester, Player responder)
	{
		log.debug("[Duel] Player " + responder.getName() + " rejected duel request from " + requester.getName());
		PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_REJECTED_BY(responder.getName()));
		PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.DUEL_REJECT_DUEL_OF(requester.getName()));
	}

	/**
	 * Cancels the duel request
	 * 
	 * @param target
	 *           the duel target
	 * @param requester
	 */
	private void cancelDuelRequest(Player owner, Player target)
	{
		log.debug("[Duel] Player " + owner.getName() + " cancelled his duel request with " + target.getName());
		PacketSendUtility.sendPacket(target, SM_SYSTEM_MESSAGE.DUEL_CANCEL_DUEL_BY(owner.getName()));
		PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.DUEL_CANCEL_DUEL_WITH(target.getName()));
	}

	/**
	 * Starts the duel
	 * 
	 * @param requester
	 *           the player to start duel with
	 * @param responder
	 *           the other player
	 */
	private void startDuel(Player requester, Player responder)
	{
		PacketSendUtility.sendPacket(requester, SM_DUEL.SM_DUEL_STARTED(responder.getObjectId()));
		PacketSendUtility.sendPacket(responder, SM_DUEL.SM_DUEL_STARTED(requester.getObjectId()));
		createDuel(requester.getObjectId(), responder.getObjectId());
	}

	/**
	 * This method will make the selected player lose the duel
	 * 
	 * @param player
	 */
	public void loseDuel(Player player)
	{
		if(!isDueling(player.getObjectId()))
			return;

		/**
		 * all debuffs are removed from loser
		 * Stop casting or skill use
		 */
		player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
		player.getController().cancelCurrentSkill();
		
		int opponnentId = duels.get(player.getObjectId());
		Player opponent = World.getInstance().findPlayer(opponnentId);

		if(opponent != null)
		{
			/**
			 * all debuffs are removed from winner, but buffs will remain
			 * Stop casting or skill use
			 */
			opponent.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
			opponent.getController().cancelCurrentSkill();
			
			PacketSendUtility.sendPacket(opponent, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_WON, player.getName()));
			PacketSendUtility.sendPacket(player, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_LOST, opponent.getName()));
		}
		else
		{
			log.warn("CHECKPOINT : duel opponent is already out of world");
		}

		removeDuel(player.getObjectId(), opponnentId);
	}

	/**
	 * @param player
	 * @param lastAttacker
	 */
	public void onDie(Player player)
	{
		loseDuel(player);
		player.getLifeStats().setCurrentHp(1);
	}

	/**
	 * @param playerObjId
	 * @return true of player is dueling
	 */
	public boolean isDueling(int playerObjId)
	{
		return (duels.containsKey(playerObjId) && duels.containsValue(playerObjId));
	}

	/**
	 * @param playerObjId
	 * @param targetObjId
	 * @return true of player is dueling
	 */
	public boolean isDueling(int playerObjId, int targetObjId)
	{
		return duels.containsKey(playerObjId) && duels.get(playerObjId) == targetObjId;
	}

	/**
	 * @param requesterObjId
	 * @param responderObjId
	 */
	public void createDuel(int requesterObjId, int responderObjId)
	{
		duels.put(requesterObjId, responderObjId);
		duels.put(responderObjId, requesterObjId);
	}

	/**
	 * @param requesterObjId
	 * @param responderObjId
	 */
	private void removeDuel(int requesterObjId, int responderObjId)
	{
		duels.remove(requesterObjId);
		duels.remove(responderObjId);
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DuelService instance = new DuelService();
	}
}
