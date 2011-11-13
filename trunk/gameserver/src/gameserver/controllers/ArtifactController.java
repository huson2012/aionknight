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
package gameserver.controllers;

import org.apache.log4j.Logger;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.legion.Legion;
import gameserver.model.siege.Artifact;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.SiegeService;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Effect;
import gameserver.skill.model.Skill;
import gameserver.skill.model.SkillSubType;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import gameserver.world.WorldType;

import java.util.Collections;


/**
 * @author Sylar,newlife
 *
 */
public class ArtifactController extends NpcController
{
	public long lastArtifactActivation = 0;
	private SkillTemplate skillTemplate;
	private SiegeLocation siegeLocation;
	private ArtifactStatus status;
	
	private enum ArtifactActivationEvent
	{
		START_ACTIVATION,
		ACTIVATION_COMPLETE,
		ACTIVATION_FAIL,
		DISABLE
	}
	
	private enum ArtifactStatus
	{
		IDLE,
		ACTIVATING,
		CANCELLED,
		ACTIVATED
	}
	
	private int getRequiredStones()
	{
		// Reshanta Core
		if(siegeLocation.getLocationId() < 1100)
			return 3;
		
		// Lower Layer
		if(siegeLocation.getLocationId() < 1200)
			return 1;
		
		// Upper Layer Artifacts
		if(siegeLocation.getLocationId() < 2000 && siegeLocation.getSiegeType() == SiegeType.ARTIFACT)
			return 2;
		
		// Upper Layer Inner Artifacts
		if(siegeLocation.getLocationId() < 2000 && siegeLocation.getSiegeType() == SiegeType.FORTRESS)
			return 1;
		
		// Balaurea Artifacts
		if(siegeLocation.getSiegeType() == SiegeType.ARTIFACT)
			return 1;
		
		// Balaurea Inner Artifacts and default value
		else
			return 2;
		
	}
	
	public void onDialogRequest(final Player player)
	{
		if(siegeLocation == null)
			siegeLocation = SiegeService.getInstance().getSiegeLocation(getOwner().getLocationId());
		if(skillTemplate == null)
			skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(getOwner().getTemplate().getEffectTemplate().getSkillId());
		RequestResponseHandler artifactHandler = new RequestResponseHandler(player){
			
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				// Close window
			}
			
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				RequestResponseHandler acceptItem = new RequestResponseHandler(player){
					
					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						// Refuse item do nothing
					}
					
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						onActivate(player);
					}
				};
				if(player.getResponseRequester().putRequest(160016, acceptItem))
				{
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160016, player.getObjectId(), new DescriptionId(2*716570+1), getRequiredStones()));
				}
			}
		};
		if(player.getResponseRequester().putRequest(160028, artifactHandler))
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160028, player.getObjectId()));
		}
	}
	
	public void onActivate(final Player player)
	{		
		
		if(siegeLocation.getLegionId() != 0)
		{
			if(player.getLegion() == null || player.getLegion().getLegionId() != siegeLocation.getLegionId() || !player.getLegionMember().hasRights(5))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300703));
				return;
			}
		}
		
		if(skillTemplate == null)
		{
			Logger.getLogger(ArtifactController.class).error("No skill template for artifact effect id : " + getOwner().getTemplate().getEffectTemplate().getSkillId());
			return;
		}
		
		if (getRemainingCooldownSecs() > 0)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300702));
			PacketSendUtility.sendMessage(player, "Cooldown: " + getRemainingCooldownSecs() + " sec");
			return;
		}
		
		final int requiredItem;
		if(player.getWorldType() == WorldType.ABYSS)
			requiredItem = 188020000;
		else if(player.getWorldType() == WorldType.BALAUREA)
			requiredItem = 188020001;
		else
			return;
		
		if(player.getInventory().getItemCountByItemId(requiredItem) < getRequiredStones())
			return;		
		
		final Item stone = player.getInventory().getFirstItemByItemId(requiredItem); 
		
		Logger.getLogger(ArtifactController.class).debug("Artifact " + getOwner().getLocationId() + " activated by " + player.getName());
        SkillTemplate sTemplate = DataManager.SKILL_DATA.getSkillTemplate(getOwner().getTemplate().getEffectTemplate().getSkillId());
		// Start Activation
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(getOwner(), player, stone, 0), true);
		broadcastActivationEventToAllies(player, ArtifactActivationEvent.START_ACTIVATION);
		setStatus(ArtifactStatus.ACTIVATING);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(getStatus() == ArtifactStatus.ACTIVATING)
				{
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(getOwner(), player, stone, 1), true);
					
					boolean removeResult = player.getInventory().removeFromBagByItemId(requiredItem, getRequiredStones());
					if(!removeResult)
						return;
					
					setLastArtifactActivation(System.currentTimeMillis());
					broadcastActivationEventToAllies(player, ArtifactActivationEvent.ACTIVATION_COMPLETE);
					castSkill();
					setStatus(ArtifactStatus.ACTIVATED);
					
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						
						@Override
						public void run()
						{
							// broadcast artifact disabled
							broadcastActivationEventToAllies(player, ArtifactActivationEvent.DISABLE);
							setStatus(ArtifactStatus.IDLE);
						}
					}, siegeLocation.getLocationTemplate().getArtifactCooldown() * 1000);
					
				}
				else
					setStatus(ArtifactStatus.IDLE);
			}
		}, 5000);
		
		player.getObserveController().attach(new ActionObserver(ObserverType.MOVE)
		{
			@Override
			public void moved()
			{		
				if(getStatus() == ArtifactStatus.ACTIVATING)
				{
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(getOwner(), player, stone, 1), true);
					setStatus(ArtifactStatus.CANCELLED);
					broadcastActivationEventToAllies(player, ArtifactActivationEvent.ACTIVATION_FAIL);
				}
			}
		});

	}
	
	/**
	 * @return the status
	 */
	public ArtifactStatus getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ArtifactStatus status)
	{
		this.status = status;
	}

	private void broadcastActivationEventToAllies(final Player player, ArtifactActivationEvent event)
	{
		final SM_SYSTEM_MESSAGE pkt;
		if(siegeLocation.getLegionId() != 0)
		{
			final Legion legion = (player == null) ? null : player.getLegion();
			switch(event)
			{
				case START_ACTIVATION: pkt = new SM_SYSTEM_MESSAGE(1301033, legion.getLegionName(), player.getName(), new DescriptionId(skillTemplate.getNameId())); break;
				case ACTIVATION_COMPLETE: pkt = new SM_SYSTEM_MESSAGE(1301036, legion.getLegionName(), player.getName(), new DescriptionId(skillTemplate.getNameId())); break;
				case ACTIVATION_FAIL: pkt = new SM_SYSTEM_MESSAGE(1301035, legion.getLegionName(), new DescriptionId(skillTemplate.getNameId())); break;
				case DISABLE: pkt = new SM_SYSTEM_MESSAGE(1301034, (legion == null) ? "" : legion.getLegionName(), new DescriptionId(skillTemplate.getNameId())); break;
				default: pkt = null; break;
			}
		}
		else
			pkt = null;
		if(pkt != null)
		{
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				
				@Override
				public boolean run(Player object)
				{
					if(object.getActiveRegion() == null)
						return true;
					if(object.getCommonData().getRace() == player.getCommonData().getRace())
					{
						if(siegeLocation.getSiegeType() == SiegeType.FORTRESS)
							PacketSendUtility.sendPacket(object, pkt);
						else if(siegeLocation.getSiegeType() == SiegeType.ARTIFACT && getOwner().getActiveRegion().getRegionId() == object.getActiveRegion().getRegionId())
							PacketSendUtility.sendPacket(object, pkt);
					}
					return true;
				}
			});
		}
	}
	
	// This function is called after skill effected list has been populated with players
	private void castSkill()
	{
		//artifact attack by newlife
		PacketSendUtility.broadcastPacket(getOwner(),
		         new SM_EMOTION(getOwner(), EmotionType.ATTACKMODE, 97, 0));
		

		//Cast the Artifact Skill 
		for(Player p : World.getInstance().getPlayers())
		{
			Skill skill = SkillEngine.getInstance().getSkill(getOwner(), skillTemplate.getSkillId(), 1, getOwner());
			skill.setFirstTargetRangeCheck(false);
			skill.getEffectedList().clear();
			if(checkStartConditions(p))
				{					
					skill.getEffectedList().add(new CreatureWithDistance(p, 0));
	}
			skill.endCast();
		}
		//pauza
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
		}
		//perehod v son by newlife
		PacketSendUtility.broadcastPacket(getOwner(),
		         new SM_EMOTION(getOwner(), EmotionType.NEUTRALMODE, 97, 0));



	}
	
	/**
	 * 
	 * @param player
	 * @return
	 * 
	 * Checking conditions before apply artifact effect on player
	 */
	private boolean checkStartConditions(Player player)
	{
		
		if(player == null || player.getActiveRegion() == null)
			return false;
		
		int artRegion = getOwner().getActiveRegion().getRegionId();
		int playerRegion = player.getActiveRegion().getRegionId();
		
		//NOTE: check if player already has effect from this artifact
		if(player.getEffectController() != null && player.getEffectController().hasAbnormalEffect(skillTemplate.getSkillId()))
			return false;
		
		//NOTE: BUFF & CHANT are applyes only on the players of same race(positive effects)
		if (skillTemplate.getSubType()==SkillSubType.BUFF || skillTemplate.getSubType()==SkillSubType.CHANT || skillTemplate.getSubType()==SkillSubType.NONE)
		{
			if (getOwner().getObjectTemplate().getRace()==player.getCommonData().getRace())
			{
				if(siegeLocation.getSiegeType() == SiegeType.FORTRESS)
					return true;
				if (siegeLocation.getSiegeType() == SiegeType.ARTIFACT && artRegion == playerRegion)
					return true;		
			}
			return false;
		}
		//NOTE: All other SkillSubTypes applyes only on enemy race(negative effects)_
		//TODO: Hit count on enemyes
		else
		{
			if (getOwner().getObjectTemplate().getRace()!=player.getCommonData().getRace())
			{
				if (siegeLocation.getSiegeType() == SiegeType.FORTRESS)
					return true;
				if (siegeLocation.getSiegeType() == SiegeType.ARTIFACT && artRegion == playerRegion)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * @param lastArtifactActivation the lastArtifactActivation to set
	 */
	public void setLastArtifactActivation(long lastArtifactActivation)
	{
		this.lastArtifactActivation = lastArtifactActivation;
	}
	
	public int getRemainingCooldownSecs()
	{
		int duration = siegeLocation.getLocationTemplate().getArtifactCooldown();
		long diff = (System.currentTimeMillis() - lastArtifactActivation) / 1000;
		if(diff > duration)
			return 0;
		else
			return (int)(duration - diff);
	}
	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
		status = ArtifactStatus.IDLE;
	}

	@Override
	public Artifact getOwner()
	{
		return (Artifact) super.getOwner();
	}
}
