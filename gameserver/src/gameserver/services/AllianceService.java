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

package gameserver.services;

import gameserver.configs.main.GroupConfig;
import gameserver.model.Race;
import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.serverpackets.*;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.idfactory.IDFactory;
import gameserver.utils.stats.StatFunctions;
import gameserver.world.WorldType;
import javolution.util.FastMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class AllianceService
{
	/**
	 * @return альянс-сервис
	 */
	public static final AllianceService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	/**
	 * Кэширование расписания удаления членов группы.
	 */
	private FastMap<Integer, ScheduledFuture<?>>	playerAllianceRemovalTasks;

	/**
	 * Кэширование членов альянса
	 */
	private final FastMap<Integer, PlayerAlliance> allianceMembers;
	
	public AllianceService()
	{
		allianceMembers = new FastMap<Integer, PlayerAlliance>();
		playerAllianceRemovalTasks = new FastMap<Integer, ScheduledFuture<?>>();
	}

	/**
	 * Этот метод добавляет члена группы в кэш.
	 * 
	 * @param player
	 */
	private void addAllianceMemberToCache(Player player)
	{
		if(!allianceMembers.containsKey(player.getObjectId()))
			allianceMembers.put(player.getObjectId(), player.getPlayerAlliance());
	}
	
	/**
	 * @param playerObjId
	 */
	private void removeAllianceMemberFromCache(int playerObjId)
	{
		if(allianceMembers.containsKey(playerObjId))
			allianceMembers.remove(playerObjId);
	}
	
	/**
	 * @param playerObjId
	 * @return возвращает true, если игрок уже находится в кэше.
	 */
	public boolean isAllianceMember(int playerObjId)
	{
		return allianceMembers.containsKey(playerObjId);
	}

	/**
	 * Возвращает игрока в альянс - требуется при релоге персонажа.
	 * 
	 * @param playerObjId
	 * @return PlayerAlliance
	 */
	public PlayerAlliance getPlayerAlliance(int playerObjId)
	{
		return allianceMembers.get(playerObjId);
	}
	
	/**
	 * @param playerObjectId
	 * @param task
	 */
	private void addAllianceRemovalTask(int playerObjectId, ScheduledFuture<?> task)
	{
		if (!playerAllianceRemovalTasks.containsKey(playerObjectId))
			playerAllianceRemovalTasks.put(playerObjectId, task);
	}
	
	/**
	 * @param playerObjectId
	 */
	private void cancelRemovalTask(int playerObjectId)
	{
		if(playerAllianceRemovalTasks.containsKey(playerObjectId))
		{
			playerAllianceRemovalTasks.get(playerObjectId).cancel(true);
			playerAllianceRemovalTasks.remove(playerObjectId);
		}
	}
	
	/**
	 * @param player
	 */
	public void scheduleRemove(final Player player)
	{
		ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				removeMemberFromAlliance(player.getPlayerAlliance(), player.getObjectId(), PlayerAllianceEvent.LEAVE_TIMEOUT);
			}
		}, GroupConfig.ALLIANCE_REMOVE_TIME * 1000);
		
		addAllianceRemovalTask(player.getObjectId(), future);
		player.getPlayerAlliance().onPlayerDisconnect(player);
	}
	
	/**
	 * @param inviter
	 * @param invited
	 */
	public void invitePlayerToAlliance(final Player inviter, final Player invited)
	{
		if(RestrictionsManager.canInviteToAlliance(inviter, invited))
		{
			RequestResponseHandler responseHandler = getResponseHandler(inviter, invited);

			boolean result = invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_REQUEST_ALLIANCE_INVITE, responseHandler);
			
			if(result)
			{
				if (invited.isInGroup())
				{
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_INVITED_HIS_PARTY(invited.getName()));
				}
				else
				{
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITED_HIM(invited.getName()));
				}
				
				PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_REQUEST_ALLIANCE_INVITE, 0, inviter.getName()));
			}
		}
	}

	/**
	 * @param inviter
	 * @param invited
	 * @return requestResponseHandler
	 */
	private RequestResponseHandler getResponseHandler(final Player inviter, final Player invited)
	{
		RequestResponseHandler responseHandler = new RequestResponseHandler(inviter){
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				List<Player> playersToAdd = new ArrayList<Player>();
				PlayerAlliance alliance = inviter.getPlayerAlliance();
				
				if (alliance == null)
				{
					alliance = new PlayerAlliance(IDFactory.getInstance().nextId(), inviter.getObjectId());
					
					// Сбор группы торгов
					if (inviter.isInGroup())
					{
						PlayerGroup group = inviter.getPlayerGroup();

						for(Player pl : group.getMembers())
						{
							if(!pl.isInAlliance() && pl.isOnline())
							{
								GroupService.getInstance().removePlayerFromGroup(pl);
								playersToAdd.add(pl);
							}
						}
					}
					else
					{
						if(!inviter.isInAlliance() && inviter.isOnline())
							playersToAdd.add(inviter);
					}
				}
				else if (invited.isInGroup() && (invited.getPlayerGroup().size() + alliance.size()) > 24)
				{
					PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT());
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT());
					return;
				}
				else if (alliance.size() == 24)
				{
					PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER());
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER());
					return;
				}

				// Сбор приглашений в группу
				if (invited.isInGroup())
				{
					PlayerGroup group = invited.getPlayerGroup();

					for(Player pl : group.getMembers())
					{
						if(!pl.isInAlliance() && pl.isOnline())
						{
							playersToAdd.add(pl);
						}
					}
				}
				else
				{
					if(!invited.isInAlliance() && invited.isOnline())
						playersToAdd.add(invited);
				}
				
				// Посылка пакета на добавление игроков.
				for (Player member : playersToAdd)
				{
					addMemberToAlliance(alliance, member);
				}
			}

			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
			}
		};
		return responseHandler;
	}
	
	/**
	 * @param alliance
	 * @param newMember
	 */
	protected void addMemberToAlliance(PlayerAlliance alliance, Player newMember)
	{
		alliance.addMember(newMember);
		addAllianceMemberToCache(newMember);
		
		PacketSendUtility.sendPacket(newMember, new SM_ALLIANCE_INFO(alliance));
		PacketSendUtility.sendPacket(newMember, new SM_SHOW_BRAND(0, 0, 0));
		PacketSendUtility.sendPacket(newMember, SM_SYSTEM_MESSAGE.STR_FORCE_ENTERED_FORCE());
		
		broadcastAllianceMemberInfo(alliance, newMember.getObjectId(), PlayerAllianceEvent.ENTER);
		sendOtherMemberInfo(alliance, newMember);
	}


	/**
	 * @param alliance
	 * @param playerObjectId
	 * @param allianceGroupId
	 * @param secondObjectId
	 */
	public void handleGroupChange(PlayerAlliance alliance, int playerObjectId, int allianceGroupId, int secondObjectId)
	{
		if (allianceGroupId == 0)
		{
			alliance.swapPlayers(playerObjectId, secondObjectId);
			
			broadcastAllianceMemberInfo(alliance, playerObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
			broadcastAllianceMemberInfo(alliance, secondObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
		}
		else
		{
			alliance.setAllianceGroupFor(playerObjectId, allianceGroupId);
			broadcastAllianceMemberInfo(alliance, playerObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
		}
	}

	/**
	 * @param memberToUpdate
	 * @param event
	 * @param params
	 */
	private void broadcastAllianceMemberInfo(PlayerAlliance alliance, int playerObjectId, PlayerAllianceEvent event, String ... params)
	{
		PlayerAllianceMember memberToUpdate = alliance.getPlayer(playerObjectId);
		if (memberToUpdate != null)
			broadcastAllianceMemberInfo(alliance, memberToUpdate, event, params);
	}

	private void broadcastAllianceMemberInfo(PlayerAlliance alliance, PlayerAllianceMember memberToUpdate, PlayerAllianceEvent event, String ... params)
	{
		if(memberToUpdate.getPlayer() == null)
			return;

		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline() || allianceMember.getPlayer() == null)
				continue;

			Player member = allianceMember.getPlayer();

			PacketSendUtility.sendPacket(member, new SM_ALLIANCE_MEMBER_INFO(memberToUpdate, event));
			PacketSendUtility.sendPacket(member, new SM_INSTANCE_COOLDOWN(memberToUpdate.getPlayer()));

			switch(event)
			{
				case ENTER:
					if (!member.equals(memberToUpdate.getPlayer()))
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_ENTERED_FORCE(memberToUpdate.getName()));
					break;
				case LEAVE:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_LEAVED_PARTY(memberToUpdate.getName()));
					break;
				case LEAVE_TIMEOUT:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_BECOME_OFFLINE_TIMEOUT(memberToUpdate.getName()));
					break;
				case BANNED:
					if (member.equals(memberToUpdate.getPlayer()))
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_BAN_ME(params[0]));
					else
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_BAN_HIM(params[0], memberToUpdate.getName()));
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * @param alliance
	 * @param event
	 * @param params
	 */
	public void broadcastAllianceInfo(PlayerAlliance alliance, PlayerAllianceEvent event, String ... params)
	{
		SM_ALLIANCE_INFO packet = new SM_ALLIANCE_INFO(alliance);
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline()) continue;
			Player member = allianceMember.getPlayer();
			PacketSendUtility.sendPacket(member, packet);
			switch(event)
			{
				case APPOINT_CAPTAIN:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_CHANGE_LEADER(params[0], alliance.getCaptain().getName()));
					break;
				case APPOINT_VICE_CAPTAIN:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_PROMOTE_MANAGER(params[0]));
					break;
				case DEMOTE_VICE_CAPTAIN:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_DEMOTE_MANAGER(params[0]));
					break;
			}
		}
	}
	
	/**
	 * @param alliance
	 * @param member
	 */
	private void sendOtherMemberInfo(PlayerAlliance alliance, Player memberToSend)
	{
		
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline())
				continue;
			
			PacketSendUtility.sendPacket(memberToSend, new SM_ALLIANCE_MEMBER_INFO(allianceMember, PlayerAllianceEvent.UPDATE));
			PacketSendUtility.sendPacket(memberToSend, new SM_INSTANCE_COOLDOWN(allianceMember.getPlayer()));
		}
	}

	/**
	 * @param actingMember
	 * @param status
	 * @param playerObjId
	 */
	public void playerStatusInfo(Player actingMember, int status, int playerObjId)
	{
		PlayerAlliance alliance = actingMember.getPlayerAlliance();
		
		if (alliance == null)
		{
			PacketSendUtility.sendMessage(actingMember, "Your alliance does not exist or can not be found");
			PacketSendUtility.sendPacket(actingMember, new SM_LEAVE_GROUP_MEMBER());
			return;
		}
		
		switch(status)
		{
			case 14: // Выход из Альянса
				removeMemberFromAlliance(alliance, actingMember.getObjectId(), PlayerAllianceEvent.LEAVE);
				break;
			case 15: // Бан Альянса
				removeMemberFromAlliance(alliance, playerObjId, PlayerAllianceEvent.BANNED, actingMember.getName());
				break;
			case 16: // Определение капитана Альянса
				String oldLeader = alliance.getCaptain().getName();
				alliance.setLeader(playerObjId);
				broadcastAllianceInfo(alliance, PlayerAllianceEvent.APPOINT_CAPTAIN, oldLeader, alliance.getCaptain().getName());
				break;
			case 19: // Проверка состояния боевой готовности
				PacketSendUtility.sendMessage(actingMember, "Readiness check is not implmeneted yet. (ID: " + playerObjId + ")");
				break;
			case 23: // Назначение Вице-Капитана Альянса
				alliance.promoteViceLeader(playerObjId);
				broadcastAllianceInfo(alliance, PlayerAllianceEvent.APPOINT_VICE_CAPTAIN, alliance.getPlayer(playerObjId).getName());
				break;
			case 24: // Снятие полномочий Вице-Капитана Альянса
				alliance.demoteViceLeader(playerObjId);
				broadcastAllianceInfo(alliance, PlayerAllianceEvent.DEMOTE_VICE_CAPTAIN, alliance.getPlayer(playerObjId).getName());
				break;
		}
	}

	/**
	 * @param member
	 * @param event
	 * @param params
	 */
	public void removeMemberFromAlliance(PlayerAlliance alliance, int memberObjectId, PlayerAllianceEvent event, String ... params)
	{
		// Игрок
		PlayerAllianceMember allianceMember = alliance.getPlayer(memberObjectId);

		// TODO: Почему-то это иногда null (когда альянс запрещен).
		if (allianceMember == null)
			return;

		if (allianceMember.isOnline())
		{
			allianceMember.getPlayer().setPlayerAlliance(null);
			PacketSendUtility.sendPacket(allianceMember.getPlayer(), new SM_LEAVE_GROUP_MEMBER());
		}

		// Альянс
		broadcastAllianceMemberInfo(alliance, allianceMember, event, params);
		alliance.removeMember(memberObjectId);
		removeAllianceMemberFromCache(memberObjectId);

		broadcastAllianceMemberInfo(alliance, memberObjectId, PlayerAllianceEvent.BANNED);

		// Проверка на роспуск Альянса
		if (alliance.size() == 1)
		{
			Player player = alliance.getCaptain().getPlayer();
			removeMemberFromAlliance(alliance, alliance.getCaptainObjectId(), event);
			if (player != null)
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_DISPERSED());
		}
	}

	/**
	 * Неиспользованные до входа пакеты последовательности, которые могут быть определены
	 * 
	 * @param player
	 */
	public void setAlliance(Player player)
	{
		if(!isAllianceMember(player.getObjectId()))
			return;

		final PlayerAlliance alliance = getPlayerAlliance(player.getObjectId());
		
		// Альянс пуст.
		if(alliance.size() == 0)
		{
			removeAllianceMemberFromCache(player.getObjectId());
			return;
		}
		
		player.setPlayerAlliance(alliance);
		cancelRemovalTask(player.getObjectId());
	}

	/**
	 * Неиспользованные до входа пакеты последовательности, которые могут быть определены
	 * 
	 * @param player
	 */
	public void onLogin(Player player)
	{
		final PlayerAlliance alliance = player.getPlayerAlliance();

		alliance.onPlayerLogin(player);
		
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		
		PacketSendUtility.sendPacket(player, new SM_ALLIANCE_INFO(alliance));
		PacketSendUtility.sendPacket(player, new SM_SHOW_BRAND(0, 0, 0));
		
		broadcastAllianceMemberInfo(alliance, player.getObjectId(), PlayerAllianceEvent.RECONNECT);
		sendOtherMemberInfo(alliance, player);
	}
	
	/**
	 * @param player
	 */
	public void onLogout(Player player)
	{
		scheduleRemove(player);
	}
	
	/**
	 * В настоящее время посылает движения пакетов для всех членов альянса.
	 * 
	 * @param player
	 * @param event
	 */
	public void updateAllianceUIToEvent(Player player, PlayerAllianceEvent event)
	{
		PlayerAlliance alliance = player.getPlayerAlliance();
		
		switch(event)
		{
			case MOVEMENT:
			case UPDATE:
				PlayerAllianceMember member = alliance.getPlayer(player.getObjectId());
				if (member != null)
				{
					SM_ALLIANCE_MEMBER_INFO packet = new SM_ALLIANCE_MEMBER_INFO(member, event);
					for(PlayerAllianceMember allianceMember : alliance.getMembers())
					{
						if (allianceMember.isOnline() && !player.equals(allianceMember.getPlayer()))
							PacketSendUtility.sendPacket(allianceMember.getPlayer(), packet);
					}
				}
				break;
				
			default:
				// Не поддерживается!
				break;
		}
	}
	
	/**
	 * Посылает уникальную информацию для каждого члена Альянса.
	 * 
	 * @param alliance
	 * @param modeId
	 * @param brandId
	 * @param targetObjectId
	 */
	public void showBrand(PlayerAlliance alliance, int modeId, int brandId, int targetObjectId)
	{
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline()) continue;
			PacketSendUtility.sendPacket(allianceMember.getPlayer(), new SM_SHOW_BRAND(modeId, brandId, targetObjectId));
		}
	}

	/**
	 * @param winner
	 * @param owner
	 */
	public void doReward(PlayerAlliance alliance, Monster owner)
	{
		// TODO: Слиться с системой наград в группе. (Практически идентичные с GroupService doReward код.)
		// Плюс полностью переписать систему падения и exp.
		// http://www.aionsource.com/topic/40542-character-stats-xp-dp-origin-gerbatorteam-july-2009/
		
		// Поиск членов группы и сортировка по уровню.
		List<Player> players = new ArrayList<Player>();
		int partyLvlSum = 0;
		int highestLevel = 0;
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline()) continue;
			Player member = allianceMember.getPlayer();  
			if(MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE))
			{
				if (member.getLifeStats().isAlreadyDead())
					continue;
				players.add(member);
				partyLvlSum += member.getLevel();
				if (member.getLevel() > highestLevel)
					highestLevel = member.getLevel();
			}
		}
		
		// Все они погибли, или не рядом.
		if (players.size() == 0)
			return;
		
		//AP награда
		int apRewardPerMember = 0;
		WorldType worldType = owner.getWorldType();
		
		if(worldType == WorldType.ABYSS || 
		(worldType == WorldType.BALAUREA && (owner.getObjectTemplate().getRace() == Race.DRAKAN || owner.getObjectTemplate().getRace() == Race.LIZARDMAN)))
		{
			// Поровну
			apRewardPerMember = Math.round(StatFunctions.calculateGroupAPReward(highestLevel, owner) / players.size());
		}
		
		// Exp награда
		long expReward = StatFunctions.calculateGroupExperienceReward(highestLevel, owner);
		
		// Exp мод
		//
		// TODO: Добавить логику, чтобы предотвратить выравнивание власти. Игроки 10 уровней ниже 
		// высшей государство-член должно получить 0 exp.
		double mod = 1;
		if (players.size() == 0)
			return;
		else if (players.size() > 1)
			mod = 1+(((players.size()-1)*10)/100);
		
		expReward *= mod; 

		for(Player member : players)
		{
			if((highestLevel - member.getLevel()) < 10)
			{
				// Exp reward
				long currentExp = member.getCommonData().getExp();
				long reward = (expReward * member.getLevel())/partyLvlSum;
				reward *= member.getRates().getGroupXpRate();
				member.getCommonData().setExp(currentExp + reward);

				if(owner == null || owner.getObjectTemplate() == null)
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(Long.toString(reward)));
				else
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(reward, owner.getObjectTemplate().getNameId()));

				// DP награда
				int currentDp = member.getCommonData().getDp();
				int dpReward = StatFunctions.calculateGroupDPReward(member, owner);
				member.getCommonData().setDp(dpReward + currentDp);

				// AP награда
				if (apRewardPerMember > 0)
					member.getCommonData().addAp(Math.round(apRewardPerMember * member.getRates().getApNpcRate()));
			}
			QuestEngine.getInstance().onKill(new QuestCookie(owner, member, 0 , 0));
		}
		
		// Получение дропа
		Player leader = alliance.getCaptain().getPlayer();
		
		// TODO: Улучшить метод выпадения дропа в Альянсе/Группе
		if (leader == null) return;
		
		DropService.getInstance().registerDrop(owner, leader, highestLevel, players);
	}

	/**
	 * Этот метод позволит получить всех членов группы
	 *
	 * @param group
	 * @param except
	 * @return список членов группы
	 */
	public List<Integer> getAllianceMembers(final PlayerAlliance alliance, boolean except)
	{
		List<Integer> luckyMembers = new ArrayList<Integer>();

		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			int memberObjId = allianceMember.getObjectId();
			if(except)
			{
				if(alliance.getCaptain().getObjectId() != memberObjId)
					luckyMembers.add(memberObjId);
			}
			else
				luckyMembers.add(memberObjId);
		}
		return luckyMembers;
	}
	/**
	 * @return FastMap<Integer, Boolean>
	 */
	public List<Integer> getMembersToRegistrateByRules(Player player, PlayerAlliance alliance, Npc npc)
	{
		LootGroupRules lootRules = alliance.getLootAllianceRules();
		LootRuleType lootRule = lootRules.getLootRule();
		List<Integer> luckyMembers = new ArrayList<Integer>();

		switch(lootRule)
		{
			case ROUNDROBIN:
				int roundRobinMember = alliance.getRoundRobinMember(npc);
				if(roundRobinMember != 0)
				{
					luckyMembers.add(roundRobinMember);
					break;
				} 
			
			// Если ни один участник не найден, то дроп становится доступным любому персонажу.
			case FREEFORALL:
				luckyMembers = getAllianceMembers(alliance, false);
				break;
			case LEADER:
				luckyMembers.add(alliance.getCaptain().getObjectId());
				break;
		}
		return luckyMembers;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AllianceService instance = new AllianceService();
	}
}