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

package gameserver.restrictions;

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.Effect;
import gameserver.skill.model.Skill;
import gameserver.skill.model.SkillType;
import gameserver.skill.model.TransformType;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldType;

public class PlayerRestrictions extends AbstractRestrictions
{	
	@Override
	public boolean canAffectBySkill(Player player, VisibleObject target)
	{
		Skill skill = player.getCastingSkill();
		if(skill == null)
			return false;
		
		if(((Creature) target).getLifeStats().isAlreadyDead() && !(skill.getSkillTemplate().hasResurrectEffect()) && !(skill.checkNonTargetAOE()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.INVALID_TARGET());
			return false;
		}
		
		if (skill.getSkillTemplate().hasResurrectEffect() && (!(target instanceof Player) || !((Creature)target).getLifeStats().isAlreadyDead()))
			return false;
		
		if(skill.getSkillTemplate().hasItemHealFpEffect() && !player.isInState(CreatureState.FLYING))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_RESTRICTION_FLY_ONLY);
			return false;
		}

		if( (player.getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE)) && !skill.getSkillTemplate().hasEvadeEffect() )
			return false;
		
		if (skill.getSkillTemplate().getStack().contains("SHAPE_IDELIM") && player.getWorldId() != 300190000)
			return false;
		
		if(player.isInState(CreatureState.PRIVATE_SHOP))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_USE_ITEM_WHILE_PRIVATE_STORE);
			return false;
		}

        return !(target instanceof Player && ((Player) target).isProtectionActive());


    }
	
	@Override
	public boolean canUseSkill(Player player, Skill skill)
	{
		if(player.isCasting())
			return false;
		if (player.getLifeStats().isAlreadyDead() || player.isInState(CreatureState.DEAD))
			return false;
		
		if ( (!player.canAttack()) && (!skill.getSkillTemplate().hasEvadeEffect()) )
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANT_CAST_IN_ABNORMAL_STATE());
			return false;
		}
		
		if(skill.getSkillTemplate().getType() == SkillType.MAGICAL 
			&& player.getEffectController().isAbnormalSet(EffectId.SILENCE))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANT_CAST_MAGIC_SKILL_WHILE_SILENCED());
			return false;
		}
		
		if(skill.getSkillTemplate().getType() == SkillType.PHYSICAL
			&& player.getEffectController().isAbnormalSet(EffectId.BIND))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANT_CAST_IN_ABNORMAL_STATE());
			return false;
		}
		
		if(player.isSkillDisabled(skill.getSkillTemplate().getDelayId()))
			return false;
		
		//check for abyss skill, those can be used only in abyss or in balaurea
		if (skill.getSkillTemplate().getStack().contains("ABYSS_RANKERSKILL"))
		{
			if (player.getWorldType() != WorldType.ABYSS && 
				player.getWorldType() != WorldType.BALAUREA ||
				player.isInInstance())
				return false;
		}
		if (skill.getSkillTemplate().getStack().contains("POLYMORPH_CROMEDE") && player.getWorldId() != 300230000)
				return false;

		boolean isAvatar = false;
		
		//cannot use skills while transformed
		for(Effect ef : player.getEffectController().getAbnormalEffects())
		{
			if (ef.getTransformType() == TransformType.NONE)
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_CAST_IN_SHAPECHANGE());
				return false;
			}
			if (ef.isAvatar())
				isAvatar = true;
		}
		
		//cannot use abyss skills without deity transfomation
        return !(!isAvatar && skill.getSkillTemplate().getStack().contains("ABYSS_RANKERSKILL")
                && !skill.getSkillTemplate().getStack().contains("ABYSS_RANKERSKILL_DARK_AVATAR")
                && !skill.getSkillTemplate().getStack().contains("ABYSS_RANKERSKILL_LIGHT_AVATAR"));


    }

	@Override
	public boolean canInviteToGroup(Player player, Player target)
	{
		final PlayerGroup group = player.getPlayerGroup();
		
		if(target == null)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.INVITED_PLAYER_OFFLINE());
			return false;
		}
		
		if(group != null && group.isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.FULL_GROUP());
			return false;
		}
		else if(group != null && player.getObjectId() != group.getGroupLeader().getObjectId())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.ONLY_GROUP_LEADER_CAN_INVITE());
			return false;
		}
		else if(target.getCommonData().getRace() != player.getCommonData().getRace())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CANT_INVITE_OTHER_RACE());
			return false;
		}
		else if(target.sameObjectId(player.getObjectId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.CANNOT_INVITE_YOURSELF());
			return false;
		}
		else if(target.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SELECTED_TARGET_DEAD());
			return false;
		}
		else if(player.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.CANNOT_INVITE_BECAUSE_YOU_DEAD());
			return false;
		}
		
		if(target.isInGroup())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.PLAYER_IN_ANOTHER_GROUP(target.getName()));
			return false;
		}
		
		if (target.isInAlliance())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_ALREADY_OTHER_FORCE(target.getName()));
			return false;
		}
		
		return true;
	}

	@Override
	public boolean canInviteToAlliance(Player player, Player target)
	{
		if(target == null)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_NO_USER_TO_INVITE());
			return false;
		}
		
		if(target.getCommonData().getRace() != player.getCommonData().getRace())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CANT_INVITE_OTHER_RACE());
			return false;
		}
		
		final PlayerAlliance alliance = player.getPlayerAlliance();
		
		if (target.isInAlliance())
		{
			if (target.getPlayerAlliance() == alliance)
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_IS_ALREADY_MEMBER_OF_OUR_ALLIANCE(target.getName()));
				return false;
			}
			else
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_ALREADY_OTHER_FORCE(target.getName()));
				return false;
			}
		}
		
		if(alliance != null && alliance.isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER());
			return false;
		}
		
		if(alliance != null && !alliance.hasAuthority(player.getObjectId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_ONLY_PARTY_LEADER_CAN_LEAVE_ALLIANCE());
			return false;
		}
		
		if(target.sameObjectId(player.getObjectId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_CAN_NOT_INVITE_SELF());
			return false;
		}
		
		if(target.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SELECTED_TARGET_DEAD());
			return false;
		}
		
		if(player.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_CANT_INVITE_WHEN_DEAD());
			return false;
		}
		
		if(target.isInGroup())
		{
			PlayerGroup targetGroup = target.getPlayerGroup();
			if (target != targetGroup.getGroupLeader())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_PARTY_HIM(target.getName(), targetGroup.getGroupLeader().getName()));
				return false;
			}
			if (alliance != null && (targetGroup.size() + alliance.size() >= 24))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT());
				return false;
			}
			
		}
		
		return true;
	}

	@Override
	public boolean canAttack(Player player, VisibleObject target)
	{
		if(target == null)
			return false;
		
		if(!(target instanceof Creature))
			return false;
		
		Creature creature = (Creature) target;
		
		if(creature.getLifeStats().isAlreadyDead())
			return false;

        return player.isEnemy(creature);

    }

	@Override
	public boolean canUseWarehouse(Player player)
	{
		if(player == null || !player.isOnline())
			return false;

        return !player.isTrading();

    }

	@Override
	public boolean canTrade(Player player)
	{
		if(player == null || !player.isOnline())
			return false;

        return !player.isTrading();

    }

	@Override
	public boolean canChat(Player player)
	{
		if(player == null || !player.isOnline())
			return false;
		
		return !player.isGagged();
	}
}
