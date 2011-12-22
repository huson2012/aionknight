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

package gameserver.controllers;

import gameserver.model.gameobjects.Npc;
import gameserver.model.siege.FortressGate;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.controllers.attack.AttackStatus;
import gameserver.model.gameobjects.Creature;
import gameserver.utils.PacketSendUtility;
import gameserver.model.Race;
import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Monster;
import gameserver.model.group.PlayerGroup;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.services.AllianceService;
import gameserver.services.DropService;
import gameserver.services.GroupService;
import gameserver.utils.stats.StatFunctions;
import gameserver.world.World;
import gameserver.world.WorldType;
import gameserver.model.gameobjects.VisibleObject;

public class weaponSiegeController extends NpcController
{
	@Override
	public void onAttack(final Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
		for(Npc general : getOwner().getKnownList().getNpcs()){
			if (general instanceof FortressGate){
				getOwner().getAggroList().addHate(general, 20000);
			}
		}
	}

	@Override
	public void onRespawn()
	{
		super.onRespawn();
		for(Npc general : getOwner().getKnownList().getNpcs()){
			if (general instanceof FortressGate){
				getOwner().getAggroList().addHate(general, 20000);
			}
		}
	}				
	
	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
	}
	@Override
	@SuppressWarnings("rawtypes")
	public void attackTarget(final Creature target, int atknumber, int time, int attackType)
	{
		Npc npc = (Npc) getOwner();
		if (target instanceof FortressGate){
			target.getLifeStats().increaseHp(TYPE.HP, -1000);
			super.attackTarget(target, 0, 274, 0);
		} else {
			for(Npc general : npc.getKnownList().getNpcs()){
				if (general instanceof FortressGate){
					getOwner().getAggroList().addHate(general, 20000);
				}
			}
		}
	}
	
	
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		Npc owner = getOwner();
		if(object instanceof FortressGate)
		{
			Npc general = (Npc) object;
			owner.getAggroList().addHate(general, 20000);
		}
	}
	
		
	
	@Override
	public void doReward()
	{
		AionObject winner = getOwner().getAggroList().getMostDamage(); 
		
		if(winner == null)
			return;
		
		// TODO: Split the EXP based on overall damage.
		
		if (winner instanceof PlayerAlliance)
		{
			AllianceService.getInstance().doReward((PlayerAlliance)winner,(Monster) getOwner());
		}
		else if (winner instanceof PlayerGroup)
		{
			GroupService.getInstance().doReward((PlayerGroup)winner,(Monster) getOwner());
		}
		else if (((Player)winner).isInGroup())
		{
			GroupService.getInstance().doReward(((Player)winner).getPlayerGroup(),(Monster) getOwner());
		}
		else
		{
			super.doReward();
			
			Player player = (Player)winner;
			
			// Exp reward
			long expReward = StatFunctions.calculateSoloExperienceReward(player, getOwner());
			// is there a boost ?
			if(player.getXpBoost() > 0)
			{
				long bonusValue = Math.round((expReward * player.getXpBoost()) / 100);
				long bonusXP = expReward + bonusValue;
				player.getCommonData().addExp(bonusXP, getOwner());
				PacketSendUtility.sendMessage(player, "Experience Boost item bonus : " + bonusValue + " XP"); // TODO : Find retail msg
			}
			else
			{
				player.getCommonData().addExp(expReward, getOwner());
			}

			// DP reward
			int currentDp = player.getCommonData().getDp();
			int dpReward = StatFunctions.calculateSoloDPReward(player, getOwner());
			player.getCommonData().setDp(dpReward + currentDp);
			
			// AP reward
			WorldType worldType = World.getInstance().getWorldMap(player.getWorldId()).getWorldType();
			if(worldType == WorldType.ABYSS || 
			(worldType == WorldType.BALAUREA && (getOwner().getObjectTemplate().getRace() == Race.DRAKAN || getOwner().getObjectTemplate().getRace() == Race.LIZARDMAN)))
			{
				int apReward = StatFunctions.calculateSoloAPReward(player, getOwner());
				player.getCommonData().addAp(apReward);
			}
			
			QuestEngine.getInstance().onKill(new QuestCookie(getOwner(), player, 0 , 0));
			
			// Give Drop
			DropService.getInstance().registerDrop(getOwner() , player, player.getLevel());			
		}
	}
}
