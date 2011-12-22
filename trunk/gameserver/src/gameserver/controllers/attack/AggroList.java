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
package gameserver.controllers.attack;

import gameserver.ai.events.Event;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import java.util.Collection;

public class AggroList
{	
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(AggroList.class);
	
	private Creature owner;
	
	private FastMap<Creature, AggroInfo> aggroList = new FastMap<Creature, AggroInfo>().shared();
	
	public AggroList(Creature owner)
	{
		this.owner = owner;
	}

	/**
	 * Only add damage from enemies. (Verify this includes
	 * summons, traps, pets, and excludes fall damage.)
	 * @param creature
	 * @param damage
	 */
	public void addDamage(Creature creature, int damage)
	{
		if (creature == null ||
			!owner.isEnemy(creature))
			return;
			
		AggroInfo ai = getAggroInfo(creature);
		ai.addDamage(damage);
		/**
		 * For now we add hate equal to each damage received
		 * Additionally there will be broadcast of extra hate
		 */
		ai.addHate(damage);
		
		owner.getAi().handleEvent(Event.ATTACKED);
	}

	/**
	 * Extra hate that is received from using non-damange skill effects
	 * 
	 * @param creature
	 * @param hate
	 */
	public void addHate(Creature creature, int hate)
	{
		if (creature == null ||
			creature == owner ||
			!owner.isEnemy(creature))
			return;
	
		AggroInfo ai = getAggroInfo(creature);
		ai.addHate(hate);
		
		if(hate > 0)		
			owner.getAi().handleEvent(Event.ATTACKED);
	}

	/**
	 * @return player/group/alliance with most damage.
	 */
	public AionObject getMostDamage()
	{
		AionObject mostDamage = null;
		int maxDamage = 0;
		
		for (AggroInfo ai : getFinalDamageList(true))
		{
			if (ai.getAttacker() == null)
				continue;
			
			if (ai.getDamage() > maxDamage)
			{
				mostDamage = ai.getAttacker();
				maxDamage = ai.getDamage();
			}
		}
		
		return mostDamage;
	}
	
	/**
	 * @return player with most damage
	 */
	public Player getMostPlayerDamage()
	{
		if (aggroList.isEmpty())
			return null;
		
		Player mostDamage = null;
		int maxDamage = 0;
		
		// Use final damage list to get pet damage as well.
		for (AggroInfo ai : this.getFinalDamageList(false))
		{
			if (ai.getDamage() > maxDamage)
			{
				mostDamage = (Player)ai.getAttacker();
				maxDamage = ai.getDamage();
			}
		}
		
		return mostDamage;
	}

	/**
	 * 
	 * @return most hated creature
	 */
	public Creature getMostHated()
	{
		if (aggroList.isEmpty())
			return null;

		Creature mostHated = null;
		int maxHate = 0;

		for (AggroInfo ai : aggroList.values())
		{
			if (ai == null)
				continue;
			
			// aggroList will never contain anything but creatures
			Creature attacker = (Creature)ai.getAttacker();
			
			if(attacker.getLifeStats().isAlreadyDead()
				|| !owner.getKnownList().knowns(ai.getAttacker()))
				ai.setHate(0);

			if (ai.getHate() > maxHate)
			{
				mostHated = attacker;
				maxHate = ai.getHate();
			}
		}

		return mostHated;
	}	
	
	/**
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isMostHated(Creature creature)
	{
		if(creature == null || creature.getLifeStats().isAlreadyDead())
			return false;
		
		Creature mostHated = getMostHated();
		if(mostHated == null)
			return false;
		
		return mostHated.equals(creature);
	}

	/**
	 * @param creature
	 * @param value
	 */
	public void notifyHate(Creature creature, int value)
	{
		//transfer hate from SkillAreaNpc,Homing,Trap,Totem,Servant to master
		if (creature instanceof Npc && creature.getActingCreature() != null && creature.getActingCreature() != creature)
			creature = creature.getActingCreature();
		
		if(isHating(creature))
			addHate(creature, value);
	}
	
	/**
	 * 
	 * @param creature
	 */
	public void stopHating(Creature creature)
	{
		AggroInfo aggroInfo = aggroList.get(creature);
		if(aggroInfo != null)
			aggroInfo.setHate(0);
	}
	
	/**
	 * Remove completely creature from aggro list
	 * 
	 * @param creature
	 */
	public void remove(Creature creature)
	{
		aggroList.remove(creature);
	}
	
	/**
	 * Clear aggroList
	 */
	public void clear()
	{
		aggroList.clear();
	}
	
	/**
	 * 
	 * @param creature
	 * @return aggroInfo
	 */
	public AggroInfo getAggroInfo(Creature creature)
	{
		AggroInfo ai = aggroList.get(creature);
		if (ai == null)
		{
			ai = new AggroInfo(creature);
			aggroList.put(creature, ai);
		}
		return ai;
	}
	
	/**
	 * 
	 * @param creature
	 * @return boolean
	 */
	private boolean isHating(Creature creature)
	{
		return aggroList.containsKey(creature);
	}

	/**
	 * @return aggro list
	 */
	public Collection<AggroInfo> getList()
	{
		return aggroList.values();
	}

	/**
	 * @return total damage
	 */
	public int getTotalDamage()
	{
		int totalDamage = 0;
		for(AggroInfo ai : this.aggroList.values())
		{
			totalDamage += ai.getDamage();
		}
		return totalDamage;
	}

	/**
	 * Used to get a list of AggroInfo with player/group/alliance damages combined.
	 * - Includes only AggroInfo with PlayerAlliance, PlayerGroup, and Player objects.
	 * @return finalDamageList including players/groups/alliances
	 */
	public Collection<AggroInfo> getFinalDamageList(boolean mergeGroupDamage)
	{
		final FastMap<AionObject, AggroInfo> list = new FastMap<AionObject, AggroInfo>().shared();
		
		for(AggroInfo ai : this.aggroList.values())
		{
			if (!(ai.getAttacker() instanceof Creature))
				continue;
			
			// Check to see if this is a summon, if so add the damage to the group. 
			
			Creature master = ((Creature)ai.getAttacker()).getMaster();
			
			if (!(master instanceof Player))
				continue;
			
			Player player = (Player)master;
			
			
			// Don't include damage from players outside the known list.
			if (!owner.getKnownList().knowns(player))
				continue;
			
			if (mergeGroupDamage)
			{
				AionObject source;
				
				if (player.isInAlliance())
				{
					source = player.getPlayerAlliance();
				}
				else if (player.isInGroup())
				{
					source = player.getPlayerGroup();
				}
				else
				{
					source = player;
				}
				
				if (list.containsKey(source))
				{
					(list.get(source)).addDamage(ai.getDamage());
				}
				else
				{
					AggroInfo aggro = new AggroInfo(source);
					aggro.setDamage(ai.getDamage());
					list.put(source, aggro);
				}
			}
			else if (list.containsKey(player))
			{
				// Summon or other assistance
				list.get(player).addDamage(ai.getDamage());
			}
			else
			{
				// Create a separate object so we don't taint current list.
				AggroInfo aggro = new AggroInfo(player);
				aggro.addDamage(ai.getDamage());
				list.put(player, aggro);
			}
		}
		
		return list.values();
	}

	
	
}
