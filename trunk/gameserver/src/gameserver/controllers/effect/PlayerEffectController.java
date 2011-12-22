/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.controllers.effect;

import gameserver.dataholders.DataManager;
import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.GroupEvent;
import gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import gameserver.services.AllianceService;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.Collections;

public class PlayerEffectController extends EffectController
{
	/**
	 * Logger for this class.
	 */
	private static final Logger log = Logger.getLogger(PlayerEffectController.class);

	/**
	 * weapon mastery
	 */
	private int weaponEffects;
	
	/**
	 * armor mastery
	 */
	private int armorEffects;
	
	/**
	 * dual weapon mastery
	 */
	private int dualEffects;
	/**
	 * holds the value of DualMasteryEffect
	 */
	private int dualEffect;
	
	/**
	 * shield mastery
	 */
	private int shieldEffects;
	
	/**
	 * current food effect
	 */
	private Effect foodEffect;

	public PlayerEffectController(Creature owner)
	{
		super(owner);
	}
	
	@Override
	public void addEffect(Effect effect)
	{
		if(effect.isFood())
			addFoodEffect(effect);
		
		super.addEffect(effect);
		updatePlayerIconsAndGroup(effect);
	}
	
	@Override
	public void clearEffect(Effect effect)
	{
		if(effect.isFood())
			foodEffect = null;
	
		super.clearEffect(effect);
		updatePlayerIconsAndGroup(effect);
	}

	@Override
	public Player getOwner()
	{
		return (Player) super.getOwner();
	}

	/**
	 * @param effect
	 */
	private void updatePlayerIconsAndGroup(Effect effect)
	{
		if(!effect.isPassive())
		{
			updatePlayerEffectIcons();		
			if(getOwner().isInGroup())
				getOwner().getPlayerGroup().updateGroupUIToEvent(getOwner(), GroupEvent.UPDATE);
			if(getOwner().isInAlliance())
				AllianceService.getInstance().updateAllianceUIToEvent(getOwner(), PlayerAllianceEvent.UPDATE);
		}
	}
	
	/**
	 * @param effect
	 */
	private void addFoodEffect(Effect effect)
	{
		if(foodEffect != null)
			foodEffect.endEffect();
		foodEffect = effect;
	}

	/**
	 * Weapon mastery
	 */
	public void setWeaponMastery(int skillId)
	{
		weaponEffects = skillId;
	}

	public void unsetWeaponMastery()
	{
		weaponEffects = 0;
	}

	public int getWeaponMastery()
	{
		return weaponEffects;
	}
	
	public boolean isWeaponMasterySet(int skillId)
	{
		return weaponEffects == skillId;
	}
	
	/**
	 * Armor mastery
	 */
	public void setArmorMastery(int skillId)
	{
		armorEffects = skillId;
	}

	public void unsetArmorMastery()
	{
		armorEffects = 0;
	}

	public int getArmorMastery()
	{
		return armorEffects;
	}
	
	public boolean isArmorMasterySet(int skillId)
	{
		return armorEffects == skillId;
	}
	
	/**
	 * Dual Weapon mastery
	 */
	public boolean isDualMasterySet(int skillId)
	{
		return dualEffects == skillId;
	}
	
	public void setDualMastery(int skillId)
	{
		dualEffects = skillId;
	}

	public void unsetDualMastery()
	{
		dualEffects = 0;
	}

	public int getDualMastery()
	{
		return dualEffects;
	}
	
	/**
	 * Set dualEffect, used in calculation of subhand damage
	 * @param dualEffect
	 */
	
	public void setDualEffect(int dualEffect)
	{
		this.dualEffect = dualEffect;
	}

	public void unsetDualEffect()
	{
		dualEffect = 0;
	}

	public int getDualEffect()
	{
		return dualEffect;
	}
	
	/**
	 * Shield mastery
	 */
	public void setShieldMastery(int skillId)
	{
		shieldEffects = skillId;
	}

	public void unsetShieldMastery()
	{
		shieldEffects = 0;
	}

	public int getShieldMastery()
	{
		return shieldEffects;
	}
	
	public boolean isShieldMasterySet(int skillId)
	{
		return shieldEffects == skillId;
	}

	/**
	 * @param skillId
	 * @param skillLvl
	 * @param currentTime
	 * @param reuseDelay
	 */
	public void addSavedEffect(int skillId, int skillLvl, int remainingTime)
	{
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		
		if(template == null)
		{
			log.warn("The skill template: " + skillId + " was not found.");
			return;
		}
		
		if(remainingTime <= 0)
			return;
		
		Effect effect = new Effect(getOwner(), getOwner(), template, skillLvl, remainingTime);
		if(effect.isFood())
			addFoodEffect(effect);
		abnormalEffectMap.put(effect.getStack(), effect);
		effect.addAllEffectToSucess();
		effect.startEffect(true);
		
		PacketSendUtility.sendPacket(getOwner(),
			new SM_ABNORMAL_STATE(Collections.singletonList(effect), abnormals));
		
	}
	
}
