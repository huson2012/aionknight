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

package gameserver.model.gameobjects.stats;

import gameserver.model.items.ItemSlot;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "StatEnum")
@XmlEnum
public enum StatEnum
{
	MAXDP(0, "maxdp"),
	MAXHP(18, "maxhp"),
	MAXMP(20, "maxmp"),

	AGILITY(107, "agility",true),
	BLOCK(33, "block"),
	EVASION(31, "dodge"),
	CONCENTRATION(41, "concentration"),
	WILL(0, "will",true),
	HEALTH(0, "health",true),
	ACCURACY(0, "accuracy",true),
	KNOWLEDGE(106, "knowledge",true),
	PARRY(32, "parry"),
	POWER(0, "strength",true),
	SPEED(36, "speed",true),
	HIT_COUNT(0, "hitcount",true),

	ATTACK_RANGE(0, "attackrange",true),
	ATTACK_SPEED(29, "attackdelay",-1,true),
	PHYSICAL_ATTACK(25, "phyattack"),
	PHYSICAL_ACCURACY(30, "hitaccuracy"),
	PHYSICAL_CRITICAL(34, "critical"),
	PHYSICAL_DEFENSE(26, "physicaldefend"),
	MAIN_HAND_HITS(0, "mainhandhits"),
	MAIN_HAND_ACCURACY(0, "mainhandaccuracy"),
	MAIN_HAND_CRITICAL(0, "mainhandcritical"),
	MAIN_HAND_PHYSICAL_ATTACK(0, "mainhandpattack"),
	SUB_HAND_HITS(0, "subhandhits"),
	SUB_HAND_ACCURACY(0, "subhandaccuracy"),
	SUB_HAND_CRITICAL(0, "subhandcritical"),
	SUB_HAND_PHYSICAL_ATTACK(0, "subhandpattack"),
	CRITICAL_RESIST(0, "physicalcriticalreducerate"),

	MAGICAL_ATTACK(27, "magicalattack"),
	MAGICAL_ACCURACY(105, "magicalhitaccuracy"),
	MAGICAL_CRITICAL(40, "magicalcritical"),
	MAGICAL_RESIST(28, "magicalresist"),
	MAX_DAMAGES(0, "maxdamages"),
	MIN_DAMAGES(0, "mindamages"),
	IS_MAGICAL_ATTACK(0, "ismagicalattack",true),

	EARTH_RESISTANCE(0, "elementaldefendearth"),
	FIRE_RESISTANCE(15, "elementaldefendfire"),
	WIND_RESISTANCE(0, "elementaldefendair"),
	WATER_RESISTANCE(0, "elementaldefendwater"),

	BOOST_MAGICAL_SKILL(104, "magicalskillboost"),
	BOOST_CASTING_TIME(0, "boostcastingtime"),
	BOOST_HATE(109, "boosthate"),
	BOOST_HEAL(0, "boostheal"),

	FLY_TIME(23, "maxfp"),
	FLY_SPEED(37, "flyspeed"),

	PVP_ATTACK_RATIO(0, "pvpattackratio"),
	PVP_DEFEND_RATIO(0, "pvpdefendratio"),

	DAMAGE_REDUCE(0, "damagereduce"),

	BLEED_RESISTANCE(0, "arbleed"),
	BLIND_RESISTANCE(0, "arblind"),
	CHARM_RESISTANCE(0, "archarm"),
	CONFUSE_RESISTANCE(0, "arconfuse"),
	CURSE_RESISTANCE(0, "arcurse"),
	DISEASE_RESISTANCE(0, "ardisease"),
	FEAR_RESISTANCE(0, "arfear"),
	OPENAREIAL_RESISTANCE(0, "aropenareial"),
	PARALYZE_RESISTANCE(0, "arparalyze"),
	PERIFICATION_RESISTANCE(0, "arperification"),
	POISON_RESISTANCE(0, "arpoison"),
	ROOT_RESISTANCE(0, "arroot"),
	SILENCE_RESISTANCE(0, "arsilence"),
	SLEEP_RESISTANCE(0, "arsleep"),
	SLOW_RESISTANCE(0, "arslow"),
	SNARE_RESISTANCE(0, "arsnare"),
	SPIN_RESISTANCE(0, "arspin"),
	STAGGER_RESISTANCE(0, "arstagger"),
	STUMBLE_RESISTANCE(0, "arstumble"),
	STUN_RESISTANCE(0, "arstun"),

	REGEN_MP(0, "mpregen"),
	REGEN_HP(0, "hpregen"),
	
	/**
	 * New/Strange
	 */
    REGEN_FP(0, "fpregen"),
    STAGGER_BOOST(0, "stagger_arp"),
    STUMBLE_BOOST(0, "stumble_arp"),
    STUN_BOOST(0, "stun_arp"),
    HEAL_BOOST(0, "healskillboost"),
    ALLRESIST(0, "allresist"),
    STUNLIKE_RESISTANCE(0, "arstunlike"),
    ELEMENTAL_RESISTANCE_DARK(0, "elemental_resistance_dark"),
    ELEMENTAL_RESISTANCE_LIGHT(0, "elemental_resistance_light"),
    MAGICAL_CRITICAL_RESIST(0, "magicalcriticalresist"),
    MAGICAL_CRITICAL_DAMAGE_REDUCE(0, "magicalcriticaldamagereduce"),
    PHYSICAL_CRITICAL_RESIST(0, "physicalcriticalresist"),
    PHYSICAL_CRITICAL_DAMAGE_REDUCE(0, "physicalcriticalreducerate"),
    ERFIRE(0, "erfire"),
    ERAIR(0, "erair"),
    EREARTH(0, "erearth"),
    ERWATER(0, "erwater"),
    ABNORMAL_RESISTANCE_ALL(0, "abnormal_resistance_all"),
    MAGICAL_DEFEND(0, "magical_defend"),
    ALLPARA(0, "allpara"),
    KNOWIL(0, "knowil"),
    AGIDEX(0, "agidex"),
    STRVIT(0, "strvit"),
    
    /**
     * custom
     */
	
    MAIN_MAX_DAMAGES(0, "mainmaxdamages"),
	MAIN_MIN_DAMAGES(0, "mainmindamages"),
	SUB_MAX_DAMAGES(0, "submaxdamages"),
	SUB_MIN_DAMAGES(0, "submindamages");

	private String name;
	private boolean replace;
	private int sign;
	
	private int itemStoneMask;

	private StatEnum (int stoneMask, String name) 
	{
		this(stoneMask, name,1,false);
	}

	private StatEnum (int stoneMask, String name, boolean replace) 
	{
		this(stoneMask,name,1,replace);
	}

	private StatEnum (int stoneMask, String name, int sign) 
	{
		this(stoneMask, name,sign,false);
	}

	private StatEnum (int stoneMask, String name, int sign, boolean replace) 
	{
		this.itemStoneMask = stoneMask;
		this.name = name;
		this.replace = replace;
		this.sign = sign;
	}

	public String getName () 
	{
		return name;
	}

	public int getSign () 
	{
		return sign;
	}

	/**
	 * @return the itemStoneMask
	 */
	public int getItemStoneMask()
	{
		return itemStoneMask;
	}

	public static StatEnum find(String name)
	{
		for(StatEnum sEnum : values())
		{
			if(sEnum.name.toLowerCase().equals(name.toLowerCase()))
			{
				return sEnum;
			}
		}
		throw new IllegalArgumentException("Cannot find StatEnum for: " + name);
	}
	
	/**
	 * Used to find specific StatEnum by its item stone mask
	 *  
	 * @param mask
	 * @return StatEnum
	 */
	public static StatEnum findByItemStoneMask(int mask)
	{
		for(StatEnum sEnum : values())
		{
			if(sEnum.itemStoneMask == mask)
			{
				return sEnum;
			}
		}
		throw new IllegalArgumentException("Cannot find StatEnum for stone mask: " + mask);
	}

	public StatEnum getMainOrSubHandStat (ItemSlot slot, boolean magicalAttack) 
	{
		if(slot == null)
			return this;
		switch(this)
		{
			case PHYSICAL_ATTACK:
				if (magicalAttack)
					return MAGICAL_ATTACK;
				else
				{
					switch(slot)
					{
						case SUB_HAND:
							return SUB_HAND_PHYSICAL_ATTACK;
						case MAIN_HAND:
							return MAIN_HAND_PHYSICAL_ATTACK;
					}
				}
			case PHYSICAL_ACCURACY:
				switch(slot)
				{
					case SUB_HAND:
						return SUB_HAND_ACCURACY;
					case MAIN_HAND:
						return MAIN_HAND_ACCURACY;
				}
			case PHYSICAL_CRITICAL:
				switch(slot)
				{
					case SUB_HAND:
						return SUB_HAND_CRITICAL;
					case MAIN_HAND:
						return MAIN_HAND_CRITICAL;
				}
			case HIT_COUNT:
				switch(slot)
				{
					case SUB_HAND:
						return SUB_HAND_HITS;
					case MAIN_HAND:
						return MAIN_HAND_HITS;
				}
			case MIN_DAMAGES:
				switch(slot)
				{
					case SUB_HAND:
						return SUB_MIN_DAMAGES;
					case MAIN_HAND:
						return MAIN_MIN_DAMAGES;
				}
			case MAX_DAMAGES:
				switch(slot)
				{
					case SUB_HAND:
						return SUB_MAX_DAMAGES;
					case MAIN_HAND:
						return MAIN_MAX_DAMAGES;
				}
			default:
				return this;
		}
	}

	public boolean isMainOrSubHandStat( boolean magicalAttack)
	{
		switch(this)
		{
			case PHYSICAL_ATTACK:
				if (magicalAttack)
					return false;
			case PHYSICAL_ACCURACY:
			case PHYSICAL_CRITICAL:
				return true;

			default:
				return false;
		}
	}

	public boolean isReplace () {
		return replace;
	}
}
